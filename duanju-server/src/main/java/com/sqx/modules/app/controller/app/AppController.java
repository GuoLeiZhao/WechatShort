package com.sqx.modules.app.controller.app;


import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.AppService;
import com.sqx.modules.app.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * APP登录授权
 *
 */
@RestController
@RequestMapping("/app/user")
@Api(value = "APP管理", tags = {"APP管理"})
public class AppController {

    @Autowired
    private UserService userService;
    @Autowired
    private AppService appService;

    @Login
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("用户端修改密码")
    public Result updatePwd(@LoginUser UserEntity user,String pwd,String oldPwd) {
        if(!user.getPassword().equals(DigestUtils.sha256Hex(oldPwd))){
            return Result.error("原始密码不正确！");
        }
        if(pwd.equals(oldPwd)){
            return Result.error("新密码不能与旧密码相同！");
        }
        user.setPassword(DigestUtils.sha256Hex(pwd));
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    @ApiOperation("用户端换绑手机号")
    @ResponseBody
    public Result updatePhone(@RequestAttribute("userId") Long userId,@RequestParam String phone, @RequestParam String msg) {
        return userService.updatePhone(phone, msg,userId);
    }

    @Login
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ApiOperation("用户修改个人信息")
    @ResponseBody
    public Result updateUserImageUrl(@RequestAttribute("userId") Long userId,String zhiFuBao,String zhiFuBaoName) {
        UserEntity userEntity=new UserEntity();
        userEntity.setZhiFuBao(zhiFuBao);
        userEntity.setZhiFuBaoName(zhiFuBaoName);
        userEntity.setUserId(userId);
        userService.updateById(userEntity);
        return Result.success();
    }



    @Login
    @RequestMapping(value = "/updateUsers", method = RequestMethod.POST)
    @ApiOperation("用户修改个人信息")
    @ResponseBody
    public Result updateUsers(@RequestAttribute("userId") Long userId,@RequestBody UserEntity userEntity) {
        userEntity.setUserId(userId);
        userService.updateById(userEntity);
        return Result.success();
    }


    /*@Login
    @RequestMapping(value = "/updateUsers", method = RequestMethod.POST)
    @ApiOperation("用户修改个人信息")
    @ResponseBody
    public Result updateUsers(@RequestAttribute("userId") Long userId,String userName,String avatar,String phone) {
        UserEntity userEntity=new UserEntity();
        userEntity.setUserId(userId);
        userEntity.setUserName(userName);
        userEntity.setAvatar(avatar);
        userEntity.setPhone(phone);
        userService.updateById(userEntity);
        return Result.success();
    }*/


    @Login
    @RequestMapping(value = "/updateUserImageUrl", method = RequestMethod.POST)
    @ApiOperation("用户修改头像")
    @ResponseBody
    public Result updateUserImageUrl(@LoginUser UserEntity user,String avatar) {
        user.setAvatar(avatar);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    @ApiOperation("用户修改昵称")
    @ResponseBody
    public Result updateUserName(@LoginUser UserEntity user,String userName) {
        user.setUserName(userName);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/selectUserById", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@LoginUser UserEntity user) {
        return Result.success().put("data",user);
    }


    @RequestMapping(value = "/selectNewApp", method = RequestMethod.GET)
    @ApiOperation("升级检测")
    @ResponseBody
    public Result selectNewApp() {
        return Result.success().put("data",appService.selectNewApp());
    }

    @GetMapping("/openId/{code:.+}/{userId}")
    @ApiOperation("根据code获取openid")
    public Result getOpenid(@PathVariable("code") String code,@PathVariable("userId")Long userId) {
        return userService.getOpenId(code,userId);
    }

}
