package com.sqx.modules.course.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseDetails;
import com.sqx.modules.course.entity.CourseDetailsMulti;
import com.sqx.modules.course.service.CourseDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Api(value = "短剧视频信息", tags = {"短剧视频信息"})
@RequestMapping(value = "/courseDetails")
public class CourseDetailsController {
    @Autowired
    private CourseDetailsService courseDetailsService;

    @PostMapping("/insertCourseDetails")
    @ApiOperation("添加短剧视频信息")
    public Result insertCourseDetails(@RequestBody CourseDetails courseDetails) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        courseDetails.setCreateTime(sdf.format(new Date()));
        courseDetailsService.insert(courseDetails);
        return Result.success();
    }

    @PostMapping("/insertCourseDetailsMulti")
    @ApiOperation("添加短剧视频信息")
    public Result insertCourseDetailsMulti(@RequestBody CourseDetailsMulti courseDetailsMulti) {
        courseDetailsService.insertMulti(courseDetailsMulti);
        return Result.success();
    }

    @PostMapping("/updateCourseDetails")
    @ApiOperation("修改短剧视频信息")
    public Result updateCourseDetails(@RequestBody CourseDetails courseDetails) {
        courseDetailsService.updateCourseDetails(courseDetails);
        return Result.success();
    }

    @PostMapping("/deleteCourseDetails")
    @ApiOperation("删除短剧视频信息")
    public Result deleteCourseDetails(String ids) {
        courseDetailsService.deleteCourseDetails(ids);
        return Result.success();
    }


}
