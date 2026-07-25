package com.sqx.modules.place.service;

import com.sqx.modules.place.request.oceanengine.CallbackRequest;

public interface OceanEngineService {

    void appCallbackGet(CallbackRequest callbackRequest);
}
