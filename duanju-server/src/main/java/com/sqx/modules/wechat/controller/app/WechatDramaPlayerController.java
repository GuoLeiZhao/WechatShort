package com.sqx.modules.wechat.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.wechat.response.CanPlayDramaResponse;
import com.sqx.modules.wechat.service.WechatDramaPlayerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(value = "微信短剧播放器", tags = {"微信短剧播放器"})
@RequestMapping(value = "/app/wechatDrama")
@RequiredArgsConstructor
public class WechatDramaPlayerController {

    private final WechatDramaPlayerService wechatDramaPlayerService;

    @ApiOperation("获取用户可用的剧集")
    @GetMapping("/getUserCanPlayDramaSerialNo/{dramaId}")
    public Result getUserCanPlayDramaSerialNo(@PathVariable Long dramaId, Long userId, String sessionKey){
        return Result.success().put("data", wechatDramaPlayerService.getCanPlayDramaSerialNo(dramaId, userId, sessionKey));
    }



}
