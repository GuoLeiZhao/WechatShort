package com.sqx.modules.douyin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.BeanUtil;
import com.sqx.common.utils.RedisUtils;
import com.sqx.common.utils.Result;
import com.sqx.config.DouyinConfig;
import com.sqx.modules.ad.entity.AdUserAsset;
import com.sqx.modules.ad.service.AdUserAssetService;
import com.sqx.modules.douyin.request.*;
import com.sqx.modules.douyin.response.*;
import com.sqx.modules.douyin.service.DouyinBURDService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinBURDServiceImpl implements DouyinBURDService {

    static final String ACCESS_TOKEN_KEY_PREFIX = "douyinburd:access_key:";
    static final String BASE_URL = "https://open.douyin.com";
    static final String ACCESS_TOKEN_URL = "/oauth/client_token/";
    static final String USER_ASSET_FLOW = "/api/apps/v1/user_asset/user_asset_flow";
    static final String UPLOAD_VIDEO = "/api/playlet/v2/resource/upload/";

    private final RedisUtils redisUtils;
    private final AdUserAssetService adUserAssetService;

    @Override
    public String getAccessToken(String app) {
        String token = redisUtils.get(ACCESS_TOKEN_KEY_PREFIX + app);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }

        String appid = DouyinConfig.getDouyinConfig(app).getAppid();

        synchronized (appid) {
            token = redisUtils.get(ACCESS_TOKEN_KEY_PREFIX + app);
            if (StrUtil.isNotBlank(token)) {
                return token;
            }
            return accessTokenExecute(app);
        }
    }

    /**
     * 获取access_token
     *
     * @param app
     * @return
     */
    private String accessTokenExecute(String app) {
        String appid = DouyinConfig.getDouyinConfig(app).getAppid();
        String secret = DouyinConfig.getDouyinConfig(app).getSecret();

        DouyinBaseResponse<GetAccessTokenResponse> response = executeDouyinApi(Method.POST, ACCESS_TOKEN_URL, new GetAccessTokenRequest(appid, secret), "获取access_token", "", (httpResponse) -> {
            ParserConfig parserConfig = new ParserConfig();
            parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            return JSONObject.parseObject(
                    httpResponse.body(),
                    new TypeReference<DouyinBaseResponse<GetAccessTokenResponse>>() {
                    }.getType(),
                    parserConfig
            );
        });
        GetAccessTokenResponse responseData = response.getData();
        redisUtils.set(ACCESS_TOKEN_KEY_PREFIX + app, responseData.getAccessToken(), responseData.getExpiresIn());
        return responseData.getAccessToken();
    }

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
    private <R> R executeDouyinApi(Method method, String url, Object reqParam, String apiName, String token, Function<HttpResponse, ? extends R> mapper) {
        HttpRequest request = HttpUtil.createRequest(method, BASE_URL + url);
        request.contentType(ContentType.JSON.toString());
        request.timeout(1500);
        if (StrUtil.isNotEmpty(token)) {
            request.header("access-token", token);
        }

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
        log.error("DouyinBURD request: {}", request);
        log.error("DouyinBURD response: {}", response);
    }


    @Override
    public Result uploadUserAsset(String app, UserAssetFlowRequest userAssetFlowRequest) {
        log.info("userAssetFlowRequest: {}", userAssetFlowRequest);
        String accessToken = getAccessToken(app);
        DouyinBaseResponse<Object> response = executeDouyinApi(Method.POST, USER_ASSET_FLOW, userAssetFlowRequest, "uploadUserAsset", accessToken, (httpResponse) ->
                JSONObject.parseObject(httpResponse.body(), new TypeReference<DouyinBaseResponse<Object>>() {
                }, Feature.DisableFieldSmartMatch));
        log.info("userAssetFlowResponse: {}", response);
        if (response.getErr_no().equals(0)) {
            AdUserAsset adUserAsset = BeanUtil.convert(userAssetFlowRequest, AdUserAsset.class);
            return adUserAssetService.insert(adUserAsset);
        } else {
            return Result.error(response.getErr_tips());
        }
    }

    @Override
    public UploadTTVideoResponse upLoadTTVideo(String app, UploadTTVideoRequest uploadTTVideoRequest) {
        log.info("upLoadTTVideo request: {}", uploadTTVideoRequest);
        String accessToken = getAccessToken(app);
        uploadTTVideoRequest.setMaAppId(DouyinConfig.getDouyinConfig(app).getAppid());
        DouyinBaseResponse<UploadTTVideoResponse> response = executeDouyinApi(Method.POST, UPLOAD_VIDEO, uploadTTVideoRequest, "upLoadTTVideo", accessToken, (httpResponse) ->
                JSONObject.parseObject(httpResponse.body(), new TypeReference<DouyinBaseResponse<UploadTTVideoResponse>>() {
                }, Feature.DisableFieldSmartMatch));
        log.info("upLoadTTVideo response: {}", response);
        return response.getErr_no().equals(0) ? response.getData() : null;
    }

}
