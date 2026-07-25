package com.sqx.modules.wechat.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.ConfigConstant;
import com.sqx.common.utils.RedisUtils;
import com.sqx.modules.wechat.request.GetMediaLinkRequest;
import com.sqx.modules.wechat.request.GetUserPhoneNumberRequest;
import com.sqx.modules.wechat.request.ListMediaRequest;
import com.sqx.modules.wechat.response.GetMediaLinkResponse;
import com.sqx.modules.wechat.response.GetUserPhoneNumberResponse;
import com.sqx.modules.wechat.response.ListMediaResponse;
import com.sqx.modules.wechat.service.WechatMpService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.Serializable;
import java.util.function.Function;


@Slf4j
@Service
@RequiredArgsConstructor
public class WechatMpServiceImpl implements WechatMpService {
    private final RedisUtils redisUtils;

    // 甄选剧排行
//    private static final String APP_ID = "__WX_APPID_RANK__";
//    private static final String SECRET = "__WX_SECRET_RANK__";


    private static final String BASE_URL = "https://api.weixin.qq.com";

    private static final String AUTH_TOKEN_URL = "/cgi-bin/token?grant_type=client_credential&appid={}&secret={}";
    private static final String GET_USER_PHONE_NUMBER_URL = "/wxa/business/getuserphonenumber?access_token={}";
    private static final String LIST_MEDIA_URL = "/wxa/sec/vod/listmedia?access_token={}";
    private static final String GET_MEDIA_LINK_URL = "/wxa/sec/vod/getmedialink?access_token={}";

    @Override
    public String getAccessToken() {
        return getAccessToken(new ConfigConstant.App1());
    }

    @Override
    public String getAccessToken(ConfigConstant.WechatConfig wechatConfig) {
        String token = redisUtils.get(wechatConfig.getRedisKey());
        if (StrUtil.isNotBlank(token)) {
            return token;
        }

        synchronized (wechatConfig.getAppId()) {
            token = redisUtils.get(wechatConfig.getRedisKey());
            if (StrUtil.isNotBlank(token)) {
                return token;
            }
            return accessTokenExecute(wechatConfig);
        }
    }

    @Override
    public GetUserPhoneNumberResponse getUserPhoneNumber(GetUserPhoneNumberRequest getUserPhoneNumberRequest) {
        return executeWechatApi(Method.POST, GET_USER_PHONE_NUMBER_URL, getUserPhoneNumberRequest, "企业获取标签列表", httpResponse -> {
            ParserConfig parserConfig = new ParserConfig();
            parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            return JSONObject.parseObject(httpResponse.body(), GetUserPhoneNumberResponse.class, parserConfig);
        });
    }

    @Override
    public ListMediaResponse listMedia(ListMediaRequest listMediaRequest) {
        return executeWechatApi(Method.POST, LIST_MEDIA_URL, listMediaRequest, "获取媒资列表", httpResponse -> {
            ParserConfig parserConfig = new ParserConfig();
            parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            return JSONObject.parseObject(httpResponse.body(), ListMediaResponse.class, parserConfig);
        });
    }

    @Override
    public GetMediaLinkResponse getMediaLink(GetMediaLinkRequest getMediaLinkRequest) {
        return executeWechatApi(Method.POST, GET_MEDIA_LINK_URL, getMediaLinkRequest, "获取媒资链接", httpResponse -> {
            ParserConfig parserConfig = new ParserConfig();
            parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            return JSONObject.parseObject(httpResponse.body(), GetMediaLinkResponse.class, parserConfig);
        });
    }

    @Override
    public <R> R executeWechatApi(Method method, String url, Object reqParam, String apiName, String token, Function<HttpResponse, ? extends R> mapper) {
        HttpRequest request;
        if (url.contains("cgi-bin/token")) {
            request = HttpUtil.createRequest(method, BASE_URL + url);
        } else {
            request = HttpUtil.createRequest(method, BASE_URL + StrUtil.format(url, token));
        }
        request.contentType(ContentType.JSON.toString());

        if (null != reqParam) {
            if (reqParam instanceof File) {
                // file
                request.contentType(ContentType.MULTIPART.toString());
                request.form("file", reqParam);
                request.contentLength(Long.valueOf(((File) reqParam).length()).intValue());
            } else if (reqParam instanceof String && StrUtil.isNotBlank(reqParam.toString())) {
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

        try (HttpResponse httpResponse = request.execute()){
            if (200 == httpResponse.getStatus()) {
                if (url.contains("/media/get")) {
                    return mapper.apply(httpResponse);
                }
                // 200 成功
                JSONObject responseJson = JSONObject.parseObject(httpResponse.body());
                if (!responseJson.containsKey("errcode") || 0 == responseJson.getInteger("errcode")) {
//                    // 0 成功
                    return mapper.apply(httpResponse);
                } else {
                    logReqAndRes(request, httpResponse);
                    throw new SqxException(apiName + " 接口错误，请查看日志并检查配置！");
                }
            }else {
                logReqAndRes(request, httpResponse);
                throw new SqxException(apiName + " 接口错误，请查看日志并检查配置！");
            }
        }
    }


    /**
     * 执行公共方法
     *
     * @param method
     * @param url
     * @param reqParam
     * @param apiName
     * @param mapper
     * @return
     */
    private <R> R executeWechatApi(Method method, String url, Object reqParam, String apiName, Function<HttpResponse, ? extends R> mapper){
        if (url.contains("cgi-bin/token")) {
            return executeWechatApi(method, url, reqParam, apiName, null, mapper);
        }
        return executeWechatApi(method, url, reqParam, apiName, getAccessToken(), mapper);
    }

    /**
     * 发起获取 access_token 的请求
     *
     * @return
     */
    private String accessTokenExecute(ConfigConstant.WechatConfig wechatConfig) {
        String url = StrUtil.format(AUTH_TOKEN_URL, wechatConfig.getAppId(), wechatConfig.getSecret());
        return executeWechatApi(Method.GET, url, null, "小程序获取 access_token", httpResponse -> {
            JSONObject responseJson = JSONObject.parseObject(httpResponse.body());
            redisUtils.set(wechatConfig.getRedisKey(), responseJson.getString("access_token"), responseJson.getLong("expires_in"));
            return responseJson.getString("access_token");
        });
    }

    private static void logReqAndRes(HttpRequest request, HttpResponse response) {
        log.info("request: " + request);
        log.info("response: "+ response);
    }

}
