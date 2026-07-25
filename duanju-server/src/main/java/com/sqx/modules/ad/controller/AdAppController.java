package com.sqx.modules.ad.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdApp;
import com.sqx.modules.ad.request.AdAppQueryReq;
import com.sqx.modules.ad.service.AdAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(value = "小程序管理", tags = {"小程序管理"})
@RequestMapping(value = "/ad_app")
@RequiredArgsConstructor
public class AdAppController {
    private final AdAppService adAppService;

    @GetMapping()
    @ApiOperation("查询")
    public Result page(AdAppQueryReq req) {
        return Result.success().setData(adAppService.page(req));
    }

    @GetMapping("/dropList")
    @ApiOperation("下拉列表")
    public Result dropList(AdAppQueryReq req) {
        return Result.success().setData(adAppService.dropList(req));
    }

    @PostMapping()
    @ApiOperation("新建")
    public Result save(@RequestBody AdApp adApp) {
        return adAppService.insert(adApp);
    }

    @DeleteMapping()
    @ApiOperation("删除")
    public Result deleteById(String appId) {
        return adAppService.deleteById(appId);
    }

    @PutMapping()
    @ApiOperation("编辑")
    public Result update(@RequestBody AdApp adApp) {
        return adAppService.update(adApp);
    }

}
