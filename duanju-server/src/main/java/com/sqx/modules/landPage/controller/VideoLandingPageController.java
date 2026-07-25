package com.sqx.modules.landPage.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.landPage.entity.VideoLandingPage;
import com.sqx.modules.landPage.request.VideoLandingPageQueryReq;
import com.sqx.modules.landPage.request.VideoLandingPageShareQueryReq;
import com.sqx.modules.landPage.service.VideoLandingPageService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "落地页短剧排名信息", tags = {"落地页短剧排名信息"})
@RequestMapping(value = "/videoLandingPage")
@RequiredArgsConstructor
public class VideoLandingPageController extends AbstractController {

    private final VideoLandingPageService videoLandingPageService;

    @PostMapping()
    @ApiOperation("添加短剧信息")
    public Result insertVideoLandingPage(@RequestBody VideoLandingPage videoLandingPage) {
        return Result.success().setData(videoLandingPageService.save(videoLandingPage));
    }

    @PutMapping()
    @ApiOperation("修改短剧信息")
    public Result updateVideoLandingPage(@RequestBody VideoLandingPage videoLandingPage) {
        return Result.success().setData(videoLandingPageService.saveOrUpdate(videoLandingPage));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("假删除")
    public Result updateDelete(@PathVariable Long id) {
        VideoLandingPage videoLandingPage = new VideoLandingPage();
        videoLandingPage.setId(id);
        videoLandingPage.setDeleted(Boolean.TRUE);
        return Result.success().setData(videoLandingPageService.updateById(videoLandingPage));
    }

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
