package com.sqx.modules.wechat.service;

import com.sqx.modules.wechat.response.GetUserInfoResponse;
import com.sqx.modules.wechat.response.WechatOfficeAccountSignResponse;

public interface WechatOfficeAccountService {
    String getAccessToken();

    GetUserInfoResponse getUserInfo(String code);

    WechatOfficeAccountSignResponse sign(String url);
}
