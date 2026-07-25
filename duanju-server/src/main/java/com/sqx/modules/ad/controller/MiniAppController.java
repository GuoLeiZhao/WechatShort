package com.sqx.modules.ad.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.service.AdAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(value = "小程序接口", tags = {"小程序接口"})
@RequestMapping(value = "/mini_app")
public class MiniAppController {
    @Autowired
    private AdAppService adAppService;

    @GetMapping()
    @ApiOperation("查询")
    public Result getByAppId(String appId){
        return Result.success().setData(adAppService.selectById(appId));
    }
}
