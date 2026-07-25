package com.sqx.modules.common.controller;

import cn.hutool.core.date.DateUtil;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "通用配置管理", tags = {"通用配置管理"})
@RequestMapping(value = "/common")
public class CommonController {
    @Autowired
    private CommonInfoService commonService;

    @GetMapping(value = "/{id}")
    @ApiOperation("管理平台通用配置详情")
    @ResponseBody
    public Result getCommon(@PathVariable Integer id) {
        return Result.success().put("data",commonService.findOne(id));
    }

    @PostMapping(value = "/update")
    @ApiOperation("管理平台添加通用配置")
    @ResponseBody
    public Result updateCommon(@RequestBody CommonInfo app) {
        return commonService.update(app);
    }

    @GetMapping(value = "/delete/{id}")
    @ApiOperation("管理平台删除通用配置")
    public Result deleteCommon(@PathVariable int id) {
        return commonService.delete(id);
    }

    @RequestMapping(value = "/type/{type}", method = RequestMethod.GET)
    @ApiOperation("用户端根据type获取对象  1客服二维码\n" +
            "                   2公众号二维码\n" +
            "                   3佣金开启\n" +
            "                   4注册邀请码\n" +
            "                    5微信APPID\n" +
            "                    21微信秘钥\n" +
            "                    6淘宝APPID\n" +
            "                   7淘宝秘钥\n" +
            "                   8淘宝授权地址\n" +
            "                    9淘宝PID\n" +
            "                    10好单库key\n" +
            "                    11淘宝名\n" +
            "                    12后台服务名称\n" +
            "                    13京东APPID\n" +
            "                    14京东秘钥\n" +
            "                    15私域邀请码（唯一不变）\n" +
            "                    16公众号Token\n" +
            "                    17公众号EncodingAESKey\n" +
            "                    18提现通知管理员openid\n" +
            "                   19后台服务域名配置\n" +
            "                    20后台管理平台域名配置\n" +
            "                    22拼多多优惠券地址")
    @ResponseBody
    public Result getCommonList(@PathVariable Integer type) {
        return commonService.findByType(type);
    }


    @GetMapping(value = "/type/condition/{condition}")
    @ApiOperation("根据condition去查询  xitong  xitongs  shouye")
    @ResponseBody
    public Result findByTypeAndCondition(@PathVariable String condition) {
        return commonService.findByTypeAndCondition(condition);
    }


    @PostMapping
    @ApiOperation("管理平台添加通用配置")
    @ResponseBody
    public Result addCommon(@RequestBody CommonInfo app) {
        app.setCreateAt(DateUtil.now());
        return Result.success().put("data",commonService.save(app));
    }
    @DeleteMapping("{id}")
    @ApiOperation("管理平台删除通用配置")
    @ResponseBody
    public Result delCommon(@PathVariable Long id) {
        return Result.success().put("data",commonService.delete(id));
    }


}
