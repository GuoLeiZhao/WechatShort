package com.sqx.modules.ad.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdPlay;
import com.sqx.modules.ad.request.AdPlayNotifyReq;
import com.sqx.modules.ad.request.AdPlayQueryReq;
import com.sqx.modules.ad.service.AdPlayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(value = "主剧管理", tags = {"主剧管理"})
@RequestMapping(value = "/ad_play")
@RequiredArgsConstructor
public class AdPlayController {
    private final AdPlayService adPlayService;

    @GetMapping()
    @ApiOperation("查询")
    public Result page(AdPlayQueryReq req) {
        return Result.success().setData(adPlayService.page(req));
    }

    @GetMapping("/dropList")
    @ApiOperation("下拉列表")
    public Result dropList(AdPlayQueryReq req) {
        return Result.success().setData(adPlayService.dropList(req));
    }

    @PostMapping()
    @ApiOperation("新建")
    public Result save(@RequestBody AdPlay adPlay) {
        return adPlayService.insert(adPlay);
    }

    @DeleteMapping()
    @ApiOperation("删除")
    public Result deleteById(String playId) {
        return adPlayService.deleteById(playId);
    }

    @PutMapping()
    @ApiOperation("编辑")
    public Result update(@RequestBody AdPlay adPlay) {
        return adPlayService.update(adPlay);
    }

    @PostMapping("/notify_callback")
    @ApiOperation("抖音短剧内容库回调")
    public Object adWebhook(@RequestBody AdPlayNotifyReq adPlayNotifyReq) {
        log.info("douyin notify_callback:{}", adPlayNotifyReq);
        return "{\"err_tips\": \"success\", \"err_no\": 0}";
    }

}
