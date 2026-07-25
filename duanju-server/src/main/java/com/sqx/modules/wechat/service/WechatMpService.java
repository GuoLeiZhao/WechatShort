package com.sqx.modules.wechat.service;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.Method;
import com.sqx.common.utils.ConfigConstant;
import com.sqx.modules.wechat.request.GetMediaLinkRequest;
import com.sqx.modules.wechat.request.GetUserPhoneNumberRequest;
import com.sqx.modules.wechat.request.ListMediaRequest;
import com.sqx.modules.wechat.response.GetMediaLinkResponse;
import com.sqx.modules.wechat.response.GetUserPhoneNumberResponse;
import com.sqx.modules.wechat.response.ListMediaResponse;
import com.sqx.modules.wechat.service.impl.WechatMpServiceImpl;

import java.util.function.Function;

public interface WechatMpService {

    /**
     * 获取access_token
     * @return
     */
    String getAccessToken();

    String getAccessToken(ConfigConstant.WechatConfig wechatConfig);

    /**
     * 获取用户手机号
     *
     * @param getUserPhoneNumberRequest
     * @return
     */
    GetUserPhoneNumberResponse getUserPhoneNumber(GetUserPhoneNumberRequest getUserPhoneNumberRequest);

    ListMediaResponse listMedia(ListMediaRequest listMediaRequest);

    GetMediaLinkResponse getMediaLink(GetMediaLinkRequest getMediaLinkRequest);

    <R> R executeWechatApi(Method method, String url, Object reqParam, String apiName, String token, Function<HttpResponse, ? extends R> mapper);
}
