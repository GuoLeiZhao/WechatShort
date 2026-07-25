package com.sqx.modules.message.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 */
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/app/message")
public class AppMessageController {

    @Autowired
    private MessageService messageService;

    @Login
    @RequestMapping(value = "/selectMessageByUserId", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectUserRecharge(int page, int limit,@RequestAttribute("userId") Long userId,Integer state){
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("userId",userId);
        map.put("state",state);
        return Result.success().put("data",messageService.selectMessageList(map));
    }

    @RequestMapping(value = "/selectMessage", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectMessage(int page, int limit,Integer state){
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("state",state);
        return Result.success().put("data",messageService.selectMessageList(map));
    }

    @Login
    @PostMapping("/insertMessage")
    @ApiOperation("添加投诉")
    public Result insertMessage(@RequestBody MessageInfo messageInfo){
        messageService.saveBody(messageInfo);
        return Result.success();
    }




}