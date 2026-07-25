package com.sqx.modules.invite.controller.app;


import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 */
@Slf4j
@RestController
@Api(value = "邀请收益", tags = {"邀请收益"})
@RequestMapping(value = "/app/invite")
public class AppInviteController {

    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserService userService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @RequestMapping(value = "/selectInviteCount", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员数量")
    @ResponseBody
    public Result selectInviteCount(Integer state,Long userId){
        return Result.success().put("data",inviteService.selectInviteCount(state,userId));
    }

    @Login
    @RequestMapping(value = "/selectUserMoney", method = RequestMethod.GET)
    @ApiOperation("查看我的钱包")
    @ResponseBody
    public Result selectUserMoney(@RequestAttribute("userId") Long userId){
        return Result.success().put("data",userMoneyService.selectUserMoneyByUserId(userId));
    }



    @RequestMapping(value = "/selectInviteAndPoster", method = RequestMethod.GET)
    @ApiOperation("查看我的邀请码和海报二维码")
    @ResponseBody
    public Result selectInviteAndPoster(Long userId){
        UserEntity userEntity = userService.queryByUserId(userId);
        CommonInfo one = commonInfoService.findOne(19);
        Map<String,Object> map=new HashMap<>();
        map.put("url",one.getValue());
        map.put("user",userEntity);
        return Result.success().put("data",map);
    }

    @Login
    @RequestMapping(value = "/selectInviteMoney", method = RequestMethod.GET)
    @ApiOperation("我的收益")
    @ResponseBody
    public Result selectInviteMoney(@RequestAttribute("userId") Long userId){
        InviteMoney inviteMoney = inviteMoneyService.selectInviteMoneyByUserId(userId);
        Integer inviteCount = inviteService.selectInviteCount(-1, userId);
        Map<String,Object> result=new HashMap<>();
        result.put("inviteMoney",inviteMoney);
        result.put("inviteCount",inviteCount);
        return Result.success().put("data",result);
    }

    @GetMapping("/mpCreateQr")
    @ApiOperation("小程序推广二维码")
    public void mpCreateQr(String invitationCode,String page, HttpServletResponse response) {
        SenInfoCheckUtil.getPoster(invitationCode,page,response);
    }


    @Login
    @RequestMapping(value = "/selectInviteByUserIdLists", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员列表(查看所有邀请列表)")
    @ResponseBody
    public Result selectInviteByUserIdLists(int page,int limit,@RequestAttribute("userId") Long userId){
        PageUtils pageUtils = inviteService.selectInviteUser(page, limit, userId,null);
        return Result.success().put("data",pageUtils);
    }

    @Login
    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(Integer page, Integer limit,@RequestAttribute("userId") Long userId,Integer classify,Integer type) {
        return userMoneyDetailsService.queryUserMoneyDetails(page, limit, userId,classify,type);
    }

}