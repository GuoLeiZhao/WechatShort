package com.sqx.modules.course.controller.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.course.service.CourseDetailsService;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@Api(value = "APP短剧信息", tags = {"APP短剧信息"})
@RequestMapping(value = "/app/course")
public class AppCourseController extends AbstractController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseDetailsService courseDetailsService;
    @Autowired
    private CommonInfoService commonInfoService;

    @GetMapping("/debug")
    @ApiOperation("查询debug模式")
    public Result debug(){
        Map<String, Boolean> re = new HashMap<>();
        CommonInfo info = commonInfoService.findOne(825);
        if (info==null){
            re.put("debug", false);
        }else {
            String value = info.getValue();
            re.put("debug", Objects.equals(value, "1"));
        }
        return Result.success().setData(re);
    }

    @GetMapping("/debug/switch")
    @ApiOperation("设置debug模式")
    public Result switchDebug(String debug){
        CommonInfo info = commonInfoService.findOne(825);
        info.setValue(debug);
        commonInfoService.updateById(info);
        return Result.success();
    }

    @GetMapping("/selectCount")
    @ApiOperation("查询短剧数量")
    public Result selectCount() {
        return courseService.selectCount();
    }

    @GetMapping("/selectCourse")
    @ApiOperation("查询短剧信息")
    public Result selectCourse(@ApiParam("页") Integer page, @ApiParam("条") Integer limit, @ApiParam("分类id") Long classifyId,
                               @ApiParam("搜索内容") String title,Long bannerId,Integer sort,String token, Integer isPrice) {
        return courseService.selectCourse(page, limit, classifyId, title,null,1,bannerId,sort,token,isPrice,null);
    }


    @GetMapping("/selectCourseDetailsById")
    @ApiOperation("根据id查询短剧详情")
    public Result selectCourseDetailsById(Long id,String token){
        return courseDetailsService.selectCourseDetailsById(id,token);
    }

    @GetMapping("/selectCourseDetailsByWxMediaId/{dramaId}")
    @ApiOperation("根据微信短剧id查询短剧详情")
    public Result selectCourseDetailsByWxMediaId(@PathVariable Long dramaId, String name){
        return courseDetailsService.selectCourseDetailsByCourseName(dramaId, name);
    }

    @GetMapping("/selectCourseDetailsList")
    @ApiOperation("查询推荐视频")
    public Result selectCourseDetailsList(Integer page,Integer limit,String token){
        return courseDetailsService.selectCourseDetailsList(page, limit, token);
    }

    @Login
    @GetMapping("/selectCourseTitle")
    @ApiOperation("模糊根据短剧标题查询短剧")
    public Result selectCourseTitle(@ApiParam("页") Integer page, @ApiParam("条") Integer limit, @ApiParam("分类id") Long classifyId,
                               @ApiParam("搜索内容") String title,Long bannerId,Integer sort,String token, Integer isPrice) {
        return courseService.selectCourse(page, limit, classifyId, title,null,1,bannerId,sort,token,isPrice,null);
    }

    @GetMapping("/selectCourseTitles")
    @ApiOperation("模糊根据短剧标题查询短剧")
    public Result selectCourseTitles(@ApiParam("页") Integer page, @ApiParam("条") Integer limit, @ApiParam("分类id") Long classifyId,
                                    @ApiParam("搜索内容") String title,Long bannerId,Integer sort,String token, Integer isPrice) {
        return courseService.selectCourse(page, limit, classifyId, title,null,1,bannerId,sort,token,isPrice,null);
    }

    @Login
    @PostMapping("/courseNotify")
    @ApiOperation("看广告解锁视频")
    public Result courseNotify(@RequestAttribute Long userId, Long courseId, Long courseDetailsId){
        return courseService.courseNotify(userId, courseId, courseDetailsId);
    }


    @GetMapping("/initWxCourseMedia")
    @ApiOperation("模糊根据短剧标题查询短剧")
    public Result initWxCourseMedia(@ApiParam("页") Integer page, @ApiParam("条") Integer limit, @ApiParam("分类id") Long classifyId,
                                     @ApiParam("搜索内容") String title,Long bannerId,Integer sort,String token, Integer isPrice) {
        courseService.initWxCourseMedia();
        return Result.success();
    }

}
