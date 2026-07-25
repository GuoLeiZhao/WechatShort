package com.sqx.modules.douyin.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.douyin.request.CreateOrderRequest;
import com.sqx.modules.douyin.request.UserAssetFlowRequest;
import com.sqx.modules.douyin.service.DouyinService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(value = "头条", tags = {"头条"})
@RequestMapping(value = "/app/douyin/{app}")
@RequiredArgsConstructor
public class AppDouyinController {

    private final DouyinService douyinService;

    @PostMapping
    public Result payNotify() {
        return Result.success();
    }

    @PostMapping(value = "/createOrder")
    public Result createOrder(@PathVariable String app, @RequestBody CreateOrderRequest request) {
        return Result.success().setData(douyinService.createOrder(app, request));
    }

}
