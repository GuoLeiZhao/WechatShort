package com.sqx.modules.ad.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdAdvertEvent;
import com.sqx.modules.ad.entity.AdVideoEvent;
import com.sqx.modules.ad.service.AdAdvertEventService;
import com.sqx.modules.ad.service.AdVideoEventService;
import com.sqx.modules.app.annotation.Login;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(value = "小程序事件管理", tags = {"小程序事件管理"})
@RequestMapping(value = "/app/ad_event")
@RequiredArgsConstructor
public class AppAdEventController {
    private final AdVideoEventService adVideoEventService;
    private final AdAdvertEventService adAdvertEventService;

    @Login
    @PostMapping("/video")
    @ApiOperation("视频事件回传")
    public Result save(@RequestBody AdVideoEvent adVideoEvent) {
        return adVideoEventService.insert(adVideoEvent);
    }

    @Login
    @PostMapping("/advert")
    @ApiOperation("广告事件回传")
    public Result save(@RequestBody AdAdvertEvent adAdvertEvent) {
        return adAdvertEventService.insert(adAdvertEvent);
    }

}
