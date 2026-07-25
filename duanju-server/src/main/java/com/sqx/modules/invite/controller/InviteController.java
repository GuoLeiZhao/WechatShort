package com.sqx.modules.invite.controller;


import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.utils.InvitationCodeUtil;
import com.sqx.modules.utils.SenInfoCheckUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping(value = "/invite")
public class InviteController {

    @Autowired
    private InviteService inviteService;
    @Autowired
    private UserService userService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private CommonInfoService commonInfoService;

    @RequestMapping(value = "/selectInviteCount", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员数量")
    @ResponseBody
    public Result selectInviteCount(Integer state,Long userId){
        return Result.success().put("data",inviteService.selectInviteCount(state,userId));
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

    @RequestMapping(value = "/selectInviteByUserIdList", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员列表(只查看邀请成功成为会员))")
    @ResponseBody
    public Result selectInviteByUserIdList(int page,int limit,Long userId){
        PageUtils pageUtils = inviteService.selectInviteUser(page, limit, userId,1);
        InviteMoney inviteMoney = inviteMoneyService.selectInviteMoneyByUserId(userId);
        Map<String,Object> map=new HashMap<>();
        map.put("pageUtils",pageUtils);
        map.put("inviteMoney",inviteMoney);
        return Result.success().put("data",map);
    }

    @GetMapping("/mpCreateQr")
    @ApiOperation("小程序推广二维码")
    public void mpCreateQr(String relation, String page,HttpServletResponse response) {
        SenInfoCheckUtil.getPoster(relation,page,response);
    }




    /*@RequestMapping(value = "/selectZhiFeiMoney", method = RequestMethod.GET)
    @ApiOperation("查询直属非直属邀请收益")
    @ResponseBody
    public Result selectZhiFeiMoney(Long userId){
        UserEntity userEntity = userService.queryByUserId(userId);
        //查询直属邀请人数数量
        Integer zhiUserInviteCount = userService.selectZhiUserInviteCount(userEntity.getInvitationCode());
        //查询非直属邀请人数量
        Integer feiUserInviteCount = userService.selectFeiUserInviteCount(userEntity.getInvitationCode());
        Map<String,Object> map=new HashMap<>();
        map.put("zhiUserInviteCount",zhiUserInviteCount);
        map.put("feiUserInviteCount",feiUserInviteCount);
        return Result.success().put("data",map);
    }*/

    /*@RequestMapping(value = "/selectZhiInviteByUserIdList", method = RequestMethod.GET)
    @ApiOperation("直属")
    @ResponseBody
    public Result selectZhiInviteByUserIdList(int page,int limit,Long userId){
        return userService.selectZhiInviteByUserIdList(page,limit,userId);
    }

    @RequestMapping(value = "/selectFeiInviteByUserIdList", method = RequestMethod.GET)
    @ApiOperation("非直属用户")
    @ResponseBody
    public Result selectFeiInviteByUserIdList(int page,int limit,Long userId){
        return userService.selectFeiInviteByUserIdList(page,limit,userId);
    }*/


    @RequestMapping(value = "/selectInviteByUserIdLists", method = RequestMethod.GET)
    @ApiOperation("查看我邀请的人员列表(查看所有邀请列表)")
    @ResponseBody
    public Result selectInviteByUserIdLists(int page,int limit,Long userId){
        PageUtils pageUtils = inviteService.selectInviteUser(page, limit, userId,null);
        Map<String,Object> map=new HashMap<>();
        map.put("pageUtils",pageUtils);
        return Result.success().put("data",map);
    }

    @RequestMapping(value = "/insertInvitationCode", method = RequestMethod.POST)
    @ApiOperation("填写邀请码")
    @ResponseBody
    public Result insertInvitationCode(Long userId,String invitationCode)
    {
        if(StringUtils.isBlank(invitationCode)){
            return Result.error("邀请码不能为空！");
        }
        long inviteeUserId = InvitationCodeUtil.codeToId(invitationCode);
        UserEntity userEntity = userService.queryByUserId(inviteeUserId);
        if(userEntity==null){
            return Result.error("邀请码填写错误！");
        }
        inviteService.saveBody(userId,userEntity);
        return Result.success();
    }


}