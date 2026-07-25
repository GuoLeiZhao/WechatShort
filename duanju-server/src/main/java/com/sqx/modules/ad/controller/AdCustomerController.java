package com.sqx.modules.ad.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.request.AdCustomerQueryReq;
import com.sqx.modules.ad.service.AdCustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(value = "广告账户管理", tags = {"广告账户管理"})
@RequestMapping(value = "/ad_customer")
@RequiredArgsConstructor
public class AdCustomerController {
    private final AdCustomerService adCustomerService;

    @GetMapping()
    @ApiOperation("查询")
    public Result page(AdCustomerQueryReq req) {
        return Result.success().setData(adCustomerService.page(req));
    }

    @GetMapping("/dropList")
    @ApiOperation("下拉列表")
    public Result dropList(AdCustomerQueryReq req) {
        return Result.success().setData(adCustomerService.dropList(req));
    }

    @GetMapping("/customer_list")
    @ApiOperation("获取账户列表")
    public void syncAdCustomerList() {
        adCustomerService.syncAdCustomerList();
    }

}
