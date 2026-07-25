package com.sqx.modules.activity.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.activity.service.InviteActivityService;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "邀请活动", tags = {"邀请活动"})
@RequestMapping(value = "/app/activity/invite")
@Slf4j
@RequiredArgsConstructor
public class AppInviteActivityController extends AbstractController {

    private final InviteActivityService inviteActivityService;

    @Login
    @GetMapping("/receiveByHash/{hash}")
    @ApiOperation("根据hash查询")
    public Result receiveByHash(@LoginUser UserEntity user, @PathVariable String hash) {
        return Result.success().setData(inviteActivityService.receiveByHash(hash, user.getUserId()));
    }

    @GetMapping("/selectByHash/{hash}")
    @ApiOperation("根据id查询活动详细信息")
    public Result selectItemById(@PathVariable String hash) {
        return inviteActivityService.getByHash(hash);
    }


}
