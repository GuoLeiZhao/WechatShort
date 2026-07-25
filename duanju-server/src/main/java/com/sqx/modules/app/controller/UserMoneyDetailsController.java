package com.sqx.modules.app.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/moneyDetails")
@AllArgsConstructor
@Api("钱包明细")
public class UserMoneyDetailsController {
    private UserMoneyDetailsService userMoneyDetailsService;
    private UserMoneyService userMoneyService;


    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(Integer page, Integer limit, Long userId,Integer classify,Integer type) {
        return userMoneyDetailsService.queryUserMoneyDetails(page, limit, userId,classify,type);
    }

    @GetMapping("/selectUserMoney")
    @ApiOperation("我的钱包")
    public Result selectUserMoney(Long userId){
        return Result.success().put("data",userMoneyService.selectUserMoneyByUserId(userId));
    }


}
