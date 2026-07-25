package com.sqx.modules.app.controller.app;

import cn.hutool.core.util.StrUtil;
import com.sqx.common.utils.Result;
import com.sqx.modules.landPage.entity.VideoLandingPage;
import com.sqx.modules.landPage.entity.VideoLandingPageClickStatic;
import com.sqx.modules.landPage.service.VideoLandingPageClickStaticService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.wechat.request.GetUserPhoneNumberRequest;
import com.sqx.modules.wechat.response.GetUserPhoneNumberResponse;
import com.sqx.modules.wechat.service.WechatMpService;
import com.sun.org.apache.bcel.internal.generic.NEW;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "落地分享页短剧", tags = {"落地分享页短剧"})
@RequestMapping(value = "/app/videoLandingPageClickStatic")
@RequiredArgsConstructor
public class AppVideoLandingPageClickStaticController extends AbstractController {

    private final VideoLandingPageClickStaticService videoLandingPageClickStaticService;
    private final WechatMpService wechatMpService;

    @PostMapping()
    @ApiOperation("添加短剧统计信息")
    public Result insertVideoLandingPage(@RequestBody VideoLandingPageClickStatic videoLandingPageClickStatic) {
        GetUserPhoneNumberResponse userPhoneNumber = wechatMpService.getUserPhoneNumber(new GetUserPhoneNumberRequest(videoLandingPageClickStatic.getPhone()));
        if (null != userPhoneNumber && null != userPhoneNumber.getPhoneInfo()) {
            videoLandingPageClickStatic.setPhone(userPhoneNumber.getPhoneInfo().getPhoneNumber());
        }
        return Result.success().setData(videoLandingPageClickStaticService.save(videoLandingPageClickStatic));
    }

    @PostMapping("/v2")
    @ApiOperation("添加短剧统计信息")
    public Result insertVideoLandingPageV2(@RequestBody VideoLandingPageClickStatic videoLandingPageClickStatic) {
        if (StrUtil.isNotBlank(videoLandingPageClickStatic.getPhoneCode())) {
            GetUserPhoneNumberResponse userPhoneNumber = wechatMpService.getUserPhoneNumber(new GetUserPhoneNumberRequest(videoLandingPageClickStatic.getPhoneCode()));
            if (null != userPhoneNumber && null != userPhoneNumber.getPhoneInfo()) {
                videoLandingPageClickStatic.setPhone(userPhoneNumber.getPhoneInfo().getPhoneNumber());
            }
        }
        return Result.success().setData(videoLandingPageClickStaticService.save(videoLandingPageClickStatic));
    }

}
