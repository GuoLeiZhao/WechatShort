package com.sqx.modules.app.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.landPage.request.VideoLandingPageShareQueryReq;
import com.sqx.modules.landPage.service.VideoLandingPageShareService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "落地分享页短剧", tags = {"落地分享页短剧"})
@RequestMapping(value = "/app/videoLandingPageShare")
@RequiredArgsConstructor
public class AppVideoLandingPageShareController extends AbstractController {

    private final VideoLandingPageShareService videoLandingPageShareService;

    @GetMapping("/selectPage")
    @ApiOperation("查询信息")
    public Result selectVideoLandingPage(VideoLandingPageShareQueryReq req) {
        return Result.success().setData(videoLandingPageShareService.page(req));
    }

    @GetMapping("/selectById/{id}")
    @ApiOperation("根据id查询详细信息")
    public Result selectVideoLandingPageById(@PathVariable Long id) {
        return Result.success().setData(videoLandingPageShareService.getById(id));
    }

}
