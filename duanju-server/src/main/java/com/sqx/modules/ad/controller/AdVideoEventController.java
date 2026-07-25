package com.sqx.modules.ad.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.request.AdVideoEventQueryReq;
import com.sqx.modules.ad.service.AdVideoEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "小程序视频事件管理", tags = {"小程序视频事件管理"})
@RequestMapping(value = "/ad_video_event")
@RequiredArgsConstructor
public class AdVideoEventController {
    private final AdVideoEventService adVideoEventService;

    @GetMapping()
    @ApiOperation("查询")
    public Result page(AdVideoEventQueryReq req) {
        return Result.success().setData(adVideoEventService.page(req));
    }


}
