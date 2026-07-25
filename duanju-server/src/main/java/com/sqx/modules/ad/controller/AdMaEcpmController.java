package com.sqx.modules.ad.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.request.AdMaEcpmQueryReq;
import com.sqx.modules.ad.service.AdMaEcpmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(value = "广告收入管理", tags = {"广告收入管理"})
@RequestMapping(value = "/ad_ma_ecpm")
@RequiredArgsConstructor
public class AdMaEcpmController {
    private final AdMaEcpmService adMaEcpmService;

    @GetMapping()
    @ApiOperation("查询")
    public Result page(AdMaEcpmQueryReq req) {
        return Result.success().setData(adMaEcpmService.page(req));
    }

    @GetMapping("/download")
    @ApiOperation("下载")
    public Result download(AdMaEcpmQueryReq req) {
        return adMaEcpmService.download(req);
    }

}
