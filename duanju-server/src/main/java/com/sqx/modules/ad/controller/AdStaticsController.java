package com.sqx.modules.ad.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.request.AdStaticsHourReq;
import com.sqx.modules.ad.request.AdStaticsQueryReq;
import com.sqx.modules.ad.request.AdStaticsReq;
import com.sqx.modules.ad.service.AdStaticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(value = "数据总览", tags = {"数据总览"})
@RequestMapping(value = "/ad_statics")
@RequiredArgsConstructor
public class AdStaticsController {
    private final AdStaticsService adStaticsService;

    @GetMapping()
    @ApiOperation("查询")
    public Result page(AdStaticsQueryReq req) {
        return Result.success().setData(adStaticsService.page(req));
    }

    @GetMapping("/info")
    @ApiOperation("基础信息")
    public Result getInfo(AdStaticsReq req) {
        return Result.success().setData(adStaticsService.getInfo(req));
    }

    @GetMapping("/hour")
    @ApiOperation("按小时数据")
    public Result getHourData(AdStaticsHourReq req) {
        return Result.success().setData(adStaticsService.getHourData(req));
    }

}
