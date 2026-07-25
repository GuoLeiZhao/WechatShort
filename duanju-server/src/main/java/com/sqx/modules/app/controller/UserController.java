package com.sqx.modules.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.entity.UserVip;
import com.sqx.modules.app.response.HomeMessageResponse;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.invite.dao.InviteMoneyDao;
import com.sqx.modules.invite.entity.InviteMoney;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.pay.dao.CashOutDao;
import com.sqx.modules.pay.service.PayDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 */
@RestController
@Api(value = "用户管理", tags = {"用户管理"})
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CashOutDao cashOutDao;
    @Autowired
    private PayDetailsService payDetailsService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private InviteMoneyDao inviteMoneyDao;
    @Autowired
    private UserVipService userVipService;

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@ApiParam("用户id") @PathVariable Long userId) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByUserId(userId);
        //查询用户钱包
//        Double money = cashOutDao.selectMayMoney(userId);
        InviteMoney inviteMoney = inviteMoneyService.selectInviteMoneyByUserId(userId);
        Double money = inviteMoney.getMoney();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
        Double income = userMoneyDetailsService.monthIncome(date, userId);
        //查询邀请人数
        int count = userService.queryInviterCount(userEntity.getInvitationCode());
        UserVip userVip = userVipService.selectUserVipByUserId(userId);
        if(userVip!=null){
            userEntity.setMember(userVip.getIsVip());
            userEntity.setEndTime(userVip.getEndTime());
        }
        map.put("userEntity", userEntity);
        map.put("money", money);
        map.put("consume", consume);
        map.put("income", income);
        map.put("count", count);
        return Result.success().put("data", map);
    }

    @RequestMapping(value = "/selectUserList", method = RequestMethod.GET)
    @ApiOperation("查询所有用户列表")
    @ResponseBody
    public Result selectUserList(Integer page, Integer limit,
                                 @ApiParam("用户id 手机号 昵称 模糊搜索") String phone,
                                 @ApiParam("性别 1男 2女") Integer sex,
                                 @ApiParam("来源  ") String platform,
                                 @ApiParam("手机终端") String sysPhone,
                                 @ApiParam("状态 1正常 2封禁") Integer status,
                                 Integer member,String inviterCode,String userName) {
        return Result.success().put("data", userService.selectUserPage(page, limit, phone, sex, platform, sysPhone, status,member,inviterCode,userName));
    }


    @RequestMapping(value = "/deleteUserByUserId/{userId}", method = RequestMethod.POST)
    @ApiOperation("删除用户")
    @ResponseBody
    public Result deleteUserByUserId(@PathVariable("userId") Long userId) {
        userService.removeById(userId);
        return Result.success();
    }

    @RequestMapping(value = "/updateUserByUserId", method = RequestMethod.POST)
    @ApiOperation("修改用户")
    @ResponseBody
    public Result updateUserByUserId(@RequestBody UserEntity userEntity) {
        if(StringUtils.isNotEmpty(userEntity.getPhone())){
            UserEntity phoneUser = userService.queryByPhone(userEntity.getPhone());
            if(phoneUser!=null && !phoneUser.getUserId().equals(userEntity.getUserId())){
                return Result.error("手机号已被其他用户绑定！");
            }
        }
        userService.updateById(userEntity);
        return Result.success();
    }

    @RequestMapping(value = "/updateUserStatusByUserId", method = RequestMethod.GET)
    @ApiOperation("禁用或启用用户")
    @ResponseBody
    public Result updateUserByUserId(Long userId) {
        UserEntity byId = userService.getById(userId);
        if (byId.getStatus().equals(1)) {
            byId.setStatus(2);
        } else {
            byId.setStatus(1);
        }
        userService.updateById(byId);
        return Result.success();
    }


    /**
     * 获取openid
     *
     * @param code 微信code
     * @return openid
     */
    @GetMapping("/openId/{code:.+}/{userId}")
    @ApiOperation("根据code获取openid")
    public Result getOpenid(@PathVariable("code") String code, @PathVariable("userId") Long userId) {
        return userService.getOpenId(code, userId);
    }

    @GetMapping("/openId/{code:.+}/{userId}/v2")
    @ApiOperation("根据code获取openid")
    public Result getOpenidV2(@PathVariable("code") String code, @PathVariable("userId") Long userId) {
        return userService.getOpenIdV2(code, userId);
    }

    /**
     * 信息分析
     *
     * @return
     */
    @GetMapping("/homeMessage")
    @ApiOperation("信息分析")
    public Result homeMessage() {
        HomeMessageResponse homeMessageResponse = new HomeMessageResponse();
        //   0查总   1查天  2查月  3查年
        //设置总用户人数
        homeMessageResponse.setTotalUsers(userService.queryUserCount(0, null,null));
        //设置今日新增
        homeMessageResponse.setNewToday(userService.queryUserCount(1, null,null));
        //设置本月新增
        homeMessageResponse.setNewMonth(userService.queryUserCount(2, null,null));
        //设置本年新增
        homeMessageResponse.setNewYear(userService.queryUserCount(3, null,null));
        //设置总收入
        homeMessageResponse.setTotalRevenue(userService.queryPayMoney(0));
        //设置今日收入
        homeMessageResponse.setTodayRevenue(userService.queryPayMoney(1));
        //设置本月收入
        homeMessageResponse.setMonthRevenue(userService.queryPayMoney(2));
        //设置本年收入
        homeMessageResponse.setYearRevenue(userService.queryPayMoney(3));
        //查询指定日期下的短剧购买的 量
        return Result.success().put("data", homeMessageResponse);
    }

    /**
     * 短剧分析
     *
     * @return
     */
    @GetMapping("/courseMessage")
    @ApiOperation("短剧分析")
    public Result courseMessage(Long page, Long limit, String date, int type) {
        Page<Map<String, Object>> iPage = new Page<>(page, limit);
        IPage<Map<String, Object>> mapIPage = userService.queryCourseOrder(iPage, type, date);
        return Result.success().put("data", new PageUtils(mapIPage));
    }

    /**
     * 用户分析
     */
    @GetMapping("/userMessage")
    @ApiOperation("用户分析")
    public Result userMessage(String date, int type) {
        int sumUserCount = userService.queryUserCount(type, date,null);
        int h5Count = userService.queryUserCount(type, date,"h5");
        int appCount = userService.queryUserCount(type, date,"app");
        int wxCount = userService.queryUserCount(type, date,"小程序");
        int memberCount = userService.userMessage(date, type);
        int userCount = sumUserCount-memberCount;
        Map<String,Integer> result=new HashMap<>();
        result.put("sumUserCount",sumUserCount);
        result.put("h5Count",h5Count);
        result.put("appCount",appCount);
        result.put("wxCount",wxCount);
        result.put("memberCount",memberCount);
        result.put("userCount",userCount);
        return Result.success().put("data", result);
    }


    @PostMapping("addCannotMoney/{userId}/{money}")
    @ApiOperation("添加金额")
    public Result addCannotMoney(@PathVariable("userId") Long userId, @PathVariable("money") Double money) {
        userMoneyService.updateMoney(1, userId, money);
        //inviteMoneyDao.updateInviteMoneySum(money,userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setTitle("[增加金额]平台增加金额：" + money);
        userMoneyDetails.setContent("[增加金额]平台增加金额：" + money);
        userMoneyDetails.setType(1);
        userMoneyDetails.setMoney(new BigDecimal(money));
        userMoneyDetails.setCreateTime(sdf.format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);
        return Result.success();
    }

    @PostMapping("subCannotMoney/{userId}/{money}")
    @ApiOperation("减少金额")
    public Result subCannotMoney(@PathVariable("userId") Long userId, @PathVariable("money") Double money) {
        userMoneyService.updateMoney(2, userId, money);
        //inviteMoneyDao.updateInviteMoneySumSub(money,userId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setTitle("[减少金额]平台减少金额：" + money);
        userMoneyDetails.setContent("平台减少金额：" + money);
        userMoneyDetails.setType(1);
        userMoneyDetails.setMoney(new BigDecimal(money));
        userMoneyDetails.setCreateTime(sdf.format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);
        return Result.success();
    }

    @GetMapping("/selectInviteUserList")
    @ApiOperation("邀请用户排行榜")
    public Result selectInviteUserList(Integer page,Integer limit,String phone,String userName){
        return userService.selectInviteUserList(page, limit, userName, phone);
    }

    @GetMapping("/selectUserOnLineCount")
    @ApiOperation("统计当前在线人数")
    public Result selectUserCount(){
        return userService.selectUserOnLineCount();
    }


    @GetMapping("/userDropList")
    @ApiOperation("统计当前在线人数")
    public Result userDropList(){
        return Result.success().setData(userService.queryUserDropList());
    }
}