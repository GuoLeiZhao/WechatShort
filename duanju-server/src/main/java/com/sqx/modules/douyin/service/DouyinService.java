package com.sqx.modules.douyin.service;

import com.sqx.modules.douyin.request.AdWebhookRequest;
import com.sqx.modules.douyin.request.Code2SessionRequest;
import com.sqx.modules.douyin.request.CreateOrderRequest;
import com.sqx.modules.douyin.response.AdWebhookResponse;
import com.sqx.modules.douyin.response.Code2SessionResponse;
import com.sqx.modules.douyin.response.CreateOrderResponse;
import com.sqx.modules.douyin.response.DouyinBaseResponse;

public interface DouyinService {
    String getAccessToken(String app);

    Code2SessionResponse code2Session(String app, Code2SessionRequest request);

    CreateOrderResponse createOrder(String app, CreateOrderRequest request);

    AdWebhookResponse adWebhook(AdWebhookRequest adWebhookRequest);
}
