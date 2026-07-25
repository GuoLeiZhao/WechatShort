package com.sqx.modules.course.controller.app;

import cn.hutool.core.util.StrUtil;
import com.sqx.common.utils.RedisUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.course.entity.CourseCollect;
import com.sqx.modules.course.service.CourseCollectService;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "app短剧收藏", tags = {"app短剧收藏"})
@RequestMapping(value = "/app/courseCollect")
@RequiredArgsConstructor
public class AppCourseCollectController extends AbstractController {

    private final CourseCollectService courseCollectService;
    private final RedisUtils redisUtils;
    private final UserIntegralDetailsService userIntegralDetailsService;
    private static final String USER_TASK_DAY_PREFIX = "USER_TASK_DAY_";

    @Login
    @PostMapping("/insertCourseCollect")
    @ApiOperation("app收藏短剧信息")
    public Result insertCourseCollect(@RequestBody CourseCollect courseCollect,@RequestAttribute("userId") Long userId){
        courseCollect.setUserId(userId);
        if (1==courseCollect.getClassify() && 1 == courseCollect.getType()) {
            userIntegralDetailsService.completeTask(815, userId);
        }
        if (2==courseCollect.getClassify() && 1 == courseCollect.getType()) {
            userIntegralDetailsService.completeTask(824, userId);
        }
        if (2==courseCollect.getClassify() && 0 == courseCollect.getType()) {
            // 取消点赞，这里要相应扣掉任务的点赞数
            String count = redisUtils.get(USER_TASK_DAY_PREFIX + userId + "_" + 824);
            if (StrUtil.isNotBlank(count) && !"0".equals(count) && !"10".equals(count)) {
                // 这里要相应扣掉任务的点赞数
                int countI = Integer.parseInt(count) - 1;
                redisUtils.set(USER_TASK_DAY_PREFIX + userId + "_" + 824, countI);
            }
        }

        return courseCollectService.insertCourseCollect(courseCollect);
    }

    @Login
    @GetMapping("/selectByUserId")
    @ApiOperation("app查询收藏短剧信息")
    public Result selectByUserId(Integer page, Integer limit,@RequestAttribute("userId") Long userId,Integer classify){
        return courseCollectService.selectByUserId(page,limit,userId,classify);
    }


}
