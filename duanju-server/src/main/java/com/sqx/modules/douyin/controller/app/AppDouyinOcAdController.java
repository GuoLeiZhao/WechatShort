package com.sqx.modules.douyin.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.douyin.request.ConversionRequest;
import com.sqx.modules.douyin.service.DouyinOcAdService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(value = "巨量广告", tags = {"巨量广告"})
@RequestMapping(value = "/app/douyin/OcAd/{app}")
@RequiredArgsConstructor
public class AppDouyinOcAdController {

    private final DouyinOcAdService douyinOcAdService;

    @PostMapping(value = "/uploadConversion")
    public Result uploadConversion(@PathVariable String app, @RequestBody ConversionRequest request) {
        return Result.success().setData(douyinOcAdService.uploadConversion(request));
    }

}
