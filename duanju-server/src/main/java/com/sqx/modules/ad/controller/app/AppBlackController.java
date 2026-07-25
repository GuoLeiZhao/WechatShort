package com.sqx.modules.ad.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdBlackList;
import com.sqx.modules.ad.service.AdBlackListService;
import com.sqx.modules.app.annotation.Login;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(value = "小程序黑名单管理", tags = {"小程序黑名单管理"})
@RequestMapping(value = "/app/ad_black")
@RequiredArgsConstructor
public class AppBlackController {
    private final AdBlackListService adBlackListService;

    @Login
    @PostMapping("/isBlack")
    @ApiOperation("判断该用户是否黑名单")
    public Result save(@RequestBody AdBlackList adBlackList) {
        return adBlackListService.selectOne(adBlackList);
    }

}
