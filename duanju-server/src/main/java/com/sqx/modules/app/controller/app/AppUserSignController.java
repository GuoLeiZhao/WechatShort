package com.sqx.modules.app.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.service.UserSignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户签到
 */
@RestController
@RequestMapping("/app/sign")
@Api("用户签到")
@Slf4j
@RequiredArgsConstructor
public class AppUserSignController {

    private final UserSignService userSignService;
    @Login
    @RequestMapping(value = "/signIn/v2", method = RequestMethod.GET)
    @ApiOperation("新签到")
    @ResponseBody
    public Result signInNew(@RequestAttribute Long userId) {
        return userSignService.signIn(userId);
    }

    @Login
    @RequestMapping(value = "/selectIntegralDay/v2", method = RequestMethod.GET)
    @ApiOperation("获取签到记录")
    @ResponseBody
    public Result selectIntegralDayV2(@RequestAttribute Long userId) {
        return userSignService.selectSignDay(userId);
    }
}
