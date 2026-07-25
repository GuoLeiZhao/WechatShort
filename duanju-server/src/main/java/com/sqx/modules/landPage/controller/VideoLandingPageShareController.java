package com.sqx.modules.landPage.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.landPage.entity.VideoLandingPageShare;
import com.sqx.modules.landPage.request.VideoLandingPageShareQueryReq;
import com.sqx.modules.landPage.service.VideoLandingPageShareService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "落地分享页短剧", tags = {"落地分享页短剧"})
@RequestMapping(value = "/videoLandingPageShare")
@RequiredArgsConstructor
public class VideoLandingPageShareController extends AbstractController {

    private final VideoLandingPageShareService videoLandingPageShareService;

    @PostMapping()
    @ApiOperation("添加信息")
    public Result insertVideoLandingPage(@RequestBody VideoLandingPageShare videoLandingPageShare) {
        return Result.success().setData(videoLandingPageShareService.save(videoLandingPageShare));
    }

    @PutMapping()
    @ApiOperation("修改信息")
    public Result updateVideoLandingPage(@RequestBody VideoLandingPageShare videoLandingPageShare) {
        return Result.success().setData(videoLandingPageShareService.saveOrUpdate(videoLandingPageShare));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("假删除")
    public Result updateDelete(@PathVariable Long id) {
        VideoLandingPageShare videoLandingPageShare = new VideoLandingPageShare();
        videoLandingPageShare.setId(id);
        videoLandingPageShare.setDeleted(Boolean.TRUE);
        return Result.success().setData(videoLandingPageShareService.updateById(videoLandingPageShare));
    }

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
