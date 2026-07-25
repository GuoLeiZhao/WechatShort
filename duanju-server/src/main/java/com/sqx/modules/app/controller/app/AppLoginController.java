package com.sqx.modules.app.controller.app;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.IAppleService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.utils.UserConstantInterface;
import com.sqx.modules.app.utils.WxPhone;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.douyin.request.Code2SessionRequest;
import com.sqx.modules.douyin.service.DouyinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import weixin.popular.api.SnsAPI;
import weixin.popular.bean.sns.SnsToken;

/**
 * APP登录授权
 *
 */
@RestController
@RequestMapping("/app/Login")
@Api("APP登录接口")
@Slf4j
public class AppLoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private IAppleService appleService;
    @Autowired
    private CommonInfoService commonInfoService;

    @ApiOperation("微信小程序登陆")
    @RequestMapping(value = "/wxLogin", method = RequestMethod.GET)
    public Result wxLogin(@ApiParam("小程序code码") String code){
        return userService.wxLogin(code);
    }

    @ApiOperation("抖音小程序登陆 / 注册")
    @PostMapping(value = "/{app}/dyLogin")
    public Result wxLogin(@PathVariable String app, @RequestBody Code2SessionRequest request){
        try {
            return userService.dyLogin(app, request);
        } catch (Exception e) {
            log.error("dyLogin: {}", app, e);
            return Result.error("登录失败，请退出重试！");
        }
    }

    @ApiOperation("B站小程序登陆 / 注册")
    @PostMapping(value = "/{app}/bilibiliLogin")
    public Result wxLogin(@PathVariable String app, @RequestBody com.sqx.modules.bilibili.request.Code2SessionRequest request){
        return userService.bilibiliLogin(app, request);
    }


    @ApiOperation("小程序登录新增或修改个人信息")
    @RequestMapping(value = "/insertWxUser", method = RequestMethod.POST)
    public Result insertWxUser(@RequestBody UserEntity userInfo){
        return userService.wxRegister(userInfo);
    }


    @RequestMapping(value = "/appleLogin", method = RequestMethod.GET)
    @ApiOperation("苹果登陆获取appleUserId")
    public Result loginVerify(@RequestParam("identityToken") String identityToken) {
        try {
            log.info("苹果token：{}", identityToken);
            JSONObject jsonObject = JSON.parseObject(identityToken);
            JSONObject userInfo = jsonObject.getJSONObject("userInfo");
            String identityTokens = userInfo.getString("identityToken");
            return appleService.getAppleUserInfo(identityTokens);
        } catch (Exception e) {
            log.error("苹果token校验失败：{}", identityToken, e);
            return Result.error("苹果账号验证失败，请退出重试！");
        }
    }


    @ApiOperation("苹果登录")
    @RequestMapping(value = "/insertAppleUser", method = RequestMethod.GET)
    public Result insertAppleUser(@RequestParam String appleId){
        return userService.iosRegister(appleId);
    }

    @RequestMapping(value = "/iosBindMobile", method = RequestMethod.POST)
    @ApiOperation("苹果登录绑定手机号")
    @ResponseBody
    public Result iosBindMobile(@RequestParam String phone,@RequestParam String code,@RequestParam String appleId,@RequestParam String platform,@RequestParam Integer sysPhone) {
        return userService.iosBindMobile(phone, code, appleId, platform, sysPhone);
    }


    @RequestMapping(value = "/wxAppLogin", method = RequestMethod.POST)
    @ApiOperation("微信APP登录")
    @ResponseBody
    public Result wxAppLogin(@RequestParam String wxOpenId,@RequestParam String token) {
        return userService.wxAppLogin(wxOpenId,token);
    }


    @RequestMapping(value = "/wxBindMobile", method = RequestMethod.POST)
    @ApiOperation("微信登录绑定手机号")
    @ResponseBody
    public Result wxBindMobile(@RequestParam String phone,@RequestParam String code,@RequestParam String wxOpenId,@RequestParam String token,@RequestParam String platform,@RequestParam Integer sysPhone) {
        return userService.wxBindMobile(phone, code, wxOpenId, token, platform, sysPhone);
    }


    @RequestMapping(value = "/registerCode", method = RequestMethod.POST)
    @ApiOperation("app或h5注册或登录")
    @ResponseBody
    public Result registerCode(@RequestParam String phone,String msg,String platform,Integer sysPhone,String password,String inviterCode,String wxId) {
        return userService.registerCode(phone,msg,platform,sysPhone,password,inviterCode,wxId);
    }

    @PostMapping("/bindWxOpenPhone")
    @ApiOperation("微信公众号绑定手机号")
    public Result bindWxOpenPhone(Long userId,String phone,String msg){
        return userService.bindWxOpenPhone(userId, phone, msg);
    }

    @ApiOperation("用户端发送验证码")
    @RequestMapping(value = "/sendMsg/{phone}/{state}", method = RequestMethod.GET)
    @ResponseBody
    public Result sendMsg(@PathVariable String phone, @PathVariable String state) {
        return userService.sendMsg(phone, state);
    }

    @ApiOperation("解密手机号")
    @RequestMapping(value = "/selectPhone",method = RequestMethod.POST)
    public Result getPhoneNumberBeanS5(@RequestBody WxPhone wxPhone) {
        return UserConstantInterface.decryptS5(wxPhone.getDecryptData(), wxPhone.getKey(), wxPhone.getIv());
    }

    @ApiOperation("用户端忘记密码")
    @RequestMapping(value = "/forgetPwd", method = RequestMethod.POST)
    @ResponseBody
    public Result forgetPwd(String pwd, String phone, String msg) {
        return userService.forgetPwd(pwd, phone, msg);
    }


    @GetMapping("/getOpenId")
    @ApiOperation("公众号根据code换取openId")
    public Result getOpenId(String code,Long userId) {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            //微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            SnsToken snsToken = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code);
            String openid = snsToken.getOpenid();
            return Result.success().put("data",openid);
        } catch (Exception e) {
            throw new RuntimeException("GET_OPENID_FAIL");
        }

    }

    @GetMapping("/getOpenId/v2")
    @ApiOperation("公众号根据code换取openId")
    public Result getOpenIdV2(String code) {
        try {
            //微信appid
            CommonInfo one = commonInfoService.findOne(5);
            //微信秘钥
            CommonInfo two = commonInfoService.findOne(21);
            SnsToken snsToken = SnsAPI.oauth2AccessToken(one.getValue(), two.getValue(), code);
            return Result.success().put("data", snsToken);
        } catch (Exception e) {
            throw new RuntimeException("GET_OPENID_FAIL");
        }
    }

    @ApiOperation("用户端openid登录呢")
    @RequestMapping(value = "/openid/login", method = RequestMethod.GET)
    @ResponseBody
    public Result loginByOpenId(@RequestParam String openId, @RequestParam String unionId) {
        if ((null == openId || "null".equals(openId)) && (null == unionId || "null".equals(unionId))) {
            return Result.error();
        }
        return userService.loginByOpenId(openId, unionId);
    }

    @ApiOperation("用户端手机号一键登录")
    @PostMapping( "/uniappLoginByPhone")
    @ResponseBody
    public Result uniappLoginByPhone(@RequestParam String phone) {
        if (StrUtil.isBlank(phone)) {
            return Result.error("手机号不能为空");
        }
        return userService.uniappLoginByPhone(phone);
    }







}
