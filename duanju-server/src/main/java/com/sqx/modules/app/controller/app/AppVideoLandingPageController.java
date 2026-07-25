package com.sqx.modules.app.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.landPage.request.VideoLandingPageQueryReq;
import com.sqx.modules.landPage.service.VideoLandingPageService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "落地页短剧排名信息", tags = {"落地页短剧排名信息"})
@RequestMapping(value = "/app/videoLandingPage")
@RequiredArgsConstructor
public class AppVideoLandingPageController extends AbstractController {

    private final VideoLandingPageService videoLandingPageService;

    @GetMapping("/selectPage")
    @ApiOperation("查询短剧信息")
    public Result selectVideoLandingPage(VideoLandingPageQueryReq req) {
        return Result.success().setData(videoLandingPageService.page(req));
    }

    @GetMapping("/selectById/{id}")
    @ApiOperation("根据id查询短剧详细信息")
    public Result selectVideoLandingPageById(@PathVariable Long id) {
        return Result.success().setData(videoLandingPageService.getById(id));
    }

}
