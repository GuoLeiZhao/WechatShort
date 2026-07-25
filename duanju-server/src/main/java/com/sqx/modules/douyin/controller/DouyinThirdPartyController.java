package com.sqx.modules.douyin.controller;

import com.sqx.modules.douyin.request.thirdparty.ComponentTicketReq;
import com.sqx.modules.douyin.service.DouyinThirdPartyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(value = "服务商平台", tags = {"服务商平台"})
@RequestMapping(value = "/ad_dy_tp")
@RequiredArgsConstructor
public class DouyinThirdPartyController {

    private final DouyinThirdPartyService douyinThirdPartyService;

    @PostMapping("/component_ticket")
    @ApiOperation("接收 component_ticket")
    public String getComponentTicket(@RequestBody ComponentTicketReq componentTicketReq) {
        douyinThirdPartyService.getComponentTicket(componentTicketReq);
        return "success";
    }

}
