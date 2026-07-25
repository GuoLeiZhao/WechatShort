package com.sqx.modules.course.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.course.response.ClassificationResponse;
import com.sqx.modules.course.response.CurriculumResponse;
import com.sqx.modules.course.service.CourseClassificationService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 教育首页面展示
 *
 * @since 2021-07-15
 */
@RestController
@Api(value = "App短剧分类信息", tags = {"App短剧分类信息"})
@RequestMapping(value = "/app/courseClassification")
@Slf4j
public class AppClassificationController extends AbstractController {
    @Autowired
    private CourseClassificationService courseClassificationService;

    @GetMapping("/selectClassification")
    @ApiOperation("查询短剧信息")
    public Result selectClassification() {
        return Result.success().put("data", courseClassificationService.selectClassification());
    }

    /**
     * 查询短剧的分类信息 （未删除）
     *
     * @return
     */
    @RequestMapping(value = "/queryClassification", method = RequestMethod.GET)
    public Result queryClassification() {
        try {
            List<ClassificationResponse> classificationResponses = courseClassificationService.queryClassification();
            return Result.success().put("data", classificationResponses);
        } catch (Exception e) {
            log.error("系统发生异常！");
            return Result.error("系统发生异常！");
        }
    }




}
