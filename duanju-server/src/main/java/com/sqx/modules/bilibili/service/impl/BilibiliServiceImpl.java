package com.sqx.modules.bilibili.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.RedisUtils;
import com.sqx.config.BilibiliConfig;
import com.sqx.modules.bilibili.request.Code2SessionRequest;
import com.sqx.modules.bilibili.response.BilibiliBaseResponse;
import com.sqx.modules.bilibili.response.Code2SessionResponse;
import com.sqx.modules.bilibili.service.BilibiliService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class BilibiliServiceImpl implements BilibiliService {

    static final String ACCESS_TOKEN_KEY_PREFIX = "bilibili:access_key:";
    static final String BASE_URL = "https://miniapp.bilibili.com";
    static final String CODE_2_SESSION_URL = "/api/sns/jscode2session";

    private final RedisUtils redisUtils;

    // https://miniapp.bilibili.com/miniprogram-docs/open/login
    @Override
    public String getAccessToken(String app) {
        String token = redisUtils.get(ACCESS_TOKEN_KEY_PREFIX + app);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }

        return null;
    }

    @Override
    public Code2SessionResponse code2Session(String app, Code2SessionRequest request) {

        String appid = BilibiliConfig.getBilibiliConfig(app).getAppid();
        String secret = BilibiliConfig.getBilibiliConfig(app).getSecret();

        request.setAppid(appid);
        request.setSecret(secret);

        Code2SessionResponse response = executeBilibiliApi(Method.GET, CODE_2_SESSION_URL, request, "bilibilicode2Session",
                (httpResponse) -> JSONObject.parseObject(httpResponse.body(), new TypeReference<Code2SessionResponse>() {
                }, Feature.DisableFieldSmartMatch));
        redisUtils.set(ACCESS_TOKEN_KEY_PREFIX + app, response.getSession_key());
        return response;
    }


    /**
     * 执行b站api
     *
     * @param method
     * @param url
     * @param reqParam
     * @param apiName
     * @param mapper
     * @param <R>
     * @return
     */
    private <R> R executeBilibiliApi(Method method, String url, Object reqParam, String apiName, Function<HttpResponse, ? extends R> mapper) {
        HttpRequest request = HttpUtil.createRequest(method, BASE_URL + url);
        request.contentType(ContentType.JSON.toString());

        // 将 reqParam 转换为 Map<String, Object>
        HashMap<String, Object> paramMap = new HashMap<>();
        if (reqParam != null) {
            if (reqParam instanceof Map) {
                // 如果 reqParam 本身是 Map，直接添加
                paramMap.putAll((Map<String, Object>) reqParam);
            } else {
                // 使用 Hutool 将 POJO 转为 Map（驼峰转下划线可禁用）
                Map<String, Object> reqParamMap = BeanUtil.beanToMap(reqParam, false, false);
                paramMap.putAll(reqParamMap);
            }
        }

        if (null != reqParam) {
            if (reqParam instanceof String && StrUtil.isNotBlank(reqParam.toString())) {
                // 是 string 直接塞进去
                if (method.equals(Method.GET)) {

                } else {
                    request.body(reqParam.toString());
                }
            } else {
                // others
                SerializeConfig serializeConfig = new SerializeConfig();
                // 驼峰转下划线
                serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
                if (method.equals(Method.GET)) {
                    request.form(paramMap);
                } else {
                    request.body(JSON.toJSONString(reqParam, serializeConfig));
                }
            }
        }

        try (HttpResponse httpResponse = request.execute()) {
            if (200 == httpResponse.getStatus()) {
                // 200 成功
                JSONObject responseJson = JSONObject.parseObject(httpResponse.body());
                if (!responseJson.containsKey("errcode") || 0 == responseJson.getInteger("errcode")) {
//                    // 0 成功
                    return mapper.apply(httpResponse);
                } else {
                    logReqAndRes(request, httpResponse);
                    throw new SqxException(apiName + " 接口错误，请查看日志并检查配置！");
                }
            } else {
                logReqAndRes(request, httpResponse);
                throw new SqxException(apiName + " 接口错误，请查看日志并检查配置！");
            }
        }
    }

    private void logReqAndRes(HttpRequest request, HttpResponse response) {
        log.error("bb request: {}", request);
        log.error("bb response: {}", response);
    }
}
