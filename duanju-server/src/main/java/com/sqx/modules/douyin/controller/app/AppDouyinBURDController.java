package com.sqx.modules.douyin.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.douyin.request.UserAssetFlowRequest;
import com.sqx.modules.douyin.service.DouyinBURDService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(value = "抖音短剧小程序", tags = {"抖音短剧小程序"})
@RequestMapping(value = "/app/douyin/burd/{app}")
@RequiredArgsConstructor
public class AppDouyinBURDController {

    private final DouyinBURDService douyinBURDService;

    @PostMapping("/userAsset")
    public Result uploadUserAsset(@PathVariable String app, @RequestBody UserAssetFlowRequest userAssetFlowRequest) {
        return douyinBURDService.uploadUserAsset(app, userAssetFlowRequest);
    }

}
