package com.sqx.modules.douyin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.Result;
import com.sqx.modules.douyin.request.ConversionRequest;
import com.sqx.modules.douyin.response.DouyinOcAdBaseResponse;
import com.sqx.modules.douyin.service.DouyinOcAdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinOcAdServiceImpl implements DouyinOcAdService {

    static final String BASE_URL = "https://analytics.oceanengine.com";
    static final String CONVERSION_BACK = "/api/v2/conversion";

    /**
     * 执行抖音api
     *
     * @param method
     * @param url
     * @param reqParam
     * @param apiName
     * @param mapper
     * @param <R>
     * @return
     */
    private <R> R executeDouyinApi(Method method, String url, Object reqParam, String apiName, Function<HttpResponse, ? extends R> mapper) {
        HttpRequest request = HttpUtil.createRequest(method, BASE_URL + url);
        request.contentType(ContentType.JSON.toString());
        request.timeout(1500);
        if (null != reqParam) {
            if (reqParam instanceof String && StrUtil.isNotBlank(reqParam.toString())) {
                // 是 string 直接塞进去
                request.body(reqParam.toString());
            } else {
                // others
                SerializeConfig serializeConfig = new SerializeConfig();
                // 驼峰转下划线
                serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
                request.body(JSON.toJSONString(reqParam, serializeConfig));
            }
        }

        try (HttpResponse httpResponse = request.execute()) {
            if (200 == httpResponse.getStatus()) {
                // 200 成功
                JSONObject responseJson = JSONObject.parseObject(httpResponse.body());
                if (!responseJson.containsKey("err_no") || 0 == responseJson.getInteger("err_no")) {
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
        log.error("DouyinOcAd request: {}", request);
        log.error("DouyinOcAd response: {}", response);
    }


    @Override
    public Result uploadConversion(ConversionRequest conversionRequest) {
        try {
            log.info("conversionRequest: {}", conversionRequest);
            DouyinOcAdBaseResponse<Object> response = executeDouyinApi(Method.POST, CONVERSION_BACK, conversionRequest, "uploadConversion", (httpResponse) ->
                    JSONObject.parseObject(httpResponse.body(), new TypeReference<DouyinOcAdBaseResponse<Object>>() {
                    }, Feature.DisableFieldSmartMatch));
            log.info("conversionResponse: {}", response);
            if (response.getCode().equals(0)) {
                return Result.success();
            } else {
                return Result.error(response.getMessage());
            }
        } catch (Exception e) {
            log.error("uploadConversion", e);
            return Result.error("权益上传失败");
        }
    }

}
