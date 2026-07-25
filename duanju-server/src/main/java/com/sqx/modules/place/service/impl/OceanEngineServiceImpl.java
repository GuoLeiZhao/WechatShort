package com.sqx.modules.place.service.impl;

import com.sqx.modules.place.request.oceanengine.CallbackRequest;
import com.sqx.modules.place.service.OceanEngineService;
import com.sqx.modules.place.service.manager.OceanEngineManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OceanEngineServiceImpl implements OceanEngineService {

    private final OceanEngineManager oceanEngineManager;

    @Override
    public void appCallbackGet(CallbackRequest callbackRequest) {
        oceanEngineManager.appCallbackGet(callbackRequest);
    }

}
