package com.sqx.modules.wechat.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.wechat.request.CurrencyPayRequest;
import com.sqx.modules.wechat.request.SignRequest;
import com.sqx.modules.wechat.request.XpayCoinPayNotifyRequest;
import com.sqx.modules.wechat.response.XPayPushMessageCommonResponse;

public interface WechatVirtualPaymentService {
    void init(String key);

    Result pay(CurrencyPayRequest currencyPayRequest);

    Result sign(SignRequest signRequest);
    String userSign(String sessionKey, String postBody);
    String paySign(String uri, String postBody);
    XPayPushMessageCommonResponse payCallBack(XpayCoinPayNotifyRequest xpayCoinPayNotifyRequest);
}
