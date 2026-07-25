package com.sqx.modules.place.controller;

import com.sqx.modules.place.request.oceanengine.CallbackRequest;
import com.sqx.modules.place.service.OceanEngineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(value = "巨量引擎", tags = {"巨量引擎"})
@RequestMapping(value = "/ocean")
public class OceanEngineController {

    @Autowired
    private OceanEngineService oceanEngineService;

    @GetMapping("/callback")
    @ApiOperation("投放BI回调")
    public void callback(CallbackRequest callbackRequest) {
        oceanEngineService.appCallbackGet(callbackRequest);
    }

}
