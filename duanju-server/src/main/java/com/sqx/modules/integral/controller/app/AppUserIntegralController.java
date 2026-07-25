package com.sqx.modules.integral.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.service.UserSignService;
import com.sqx.modules.course.entity.CourseUser;
import com.sqx.modules.course.service.CourseUserService;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.integral.service.UserIntegralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "用户积分", tags = {"用户积分"})
@RequestMapping(value = "/app/integral")
public class AppUserIntegralController {

    @Autowired
    private UserIntegralService userIntegralService;
    @Autowired
    private UserIntegralDetailsService userIntegralDetailsService;
    @Autowired
    private CourseUserService courseUserService;
    @Autowired
    private UserSignService userSignService;

    @Login
    @RequestMapping(value = "/selectByUserId", method = RequestMethod.GET)
    @ApiOperation("查看用户积分")
    @ResponseBody
    public Result selectByUserId(@RequestAttribute Long userId) {
        return Result.success().put("data", userIntegralService.selectById(userId));
    }

    @Login
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ApiOperation("查看用户积分详细列表信息")
    @ResponseBody
    public Result selectUserIntegralDetailsByUserId(int page, int limit, @RequestAttribute Long userId) {
        return Result.success().put("data", userIntegralDetailsService.selectUserIntegralDetailsByUserId(page, limit, userId));
    }
    @Login
    @RequestMapping(value = "/payDetails", method = RequestMethod.GET)
    @ApiOperation("查看用户积分详细列表信息")
    @ResponseBody
    public Result payDetails(int page, int limit, @RequestAttribute Long userId) {
        return Result.success().put("data", userIntegralDetailsService.payDetails(page, limit, userId));
    }

    @Login
    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    @ApiOperation("签到")
    @ResponseBody
    public Result signIn(@RequestAttribute Long userId) {
        return userIntegralDetailsService.signIn(userId);
    }
    @Login
    @RequestMapping(value = "/selectIntegralDay", method = RequestMethod.GET)
    @ApiOperation("获取签到记录")
    @ResponseBody
    public Result selectIntegralDay(@RequestAttribute Long userId) {
        return userIntegralDetailsService.selectIntegralDay(userId);
    }

    @Login
    @RequestMapping(value = "/signInNew/{dayId}", method = RequestMethod.GET)
    @ApiOperation("新签到")
    @ResponseBody
    public Result signInNew(@RequestAttribute Long userId, @PathVariable Integer dayId) {
        return userIntegralDetailsService.signInNew(userId, dayId);
    }

    @Login
    @RequestMapping(value = "/selectIntegralDayNew", method = RequestMethod.GET)
    @ApiOperation("获取签到记录")
    @ResponseBody
    public Result selectIntegralDayNew(@RequestAttribute Long userId) {
        return userIntegralDetailsService.selectIntegralDayNew(userId);
    }

    @Login
    @RequestMapping(value = "/creditsExchange", method = RequestMethod.POST)
    @ApiOperation("积分兑换")
    @ResponseBody
    public Result creditsExchange(@RequestAttribute Long userId,Integer integral) {
        return userIntegralDetailsService.creditsExchange(userId,integral);
    }

    @Login
    @PostMapping(value = "/creditsExchange/v2")
    @ApiOperation("积分兑换")
    @ResponseBody
    public Result creditsExchange(@RequestBody UserIntegralDetails userIntegralDetails) {
        return userIntegralDetailsService.creditsExchange(userIntegralDetails);
    }

    @Login
    @PostMapping(value = "/creditsExchangeCourse")
    @ApiOperation("积分兑换")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result creditsExchangeCourse(@RequestBody UserIntegralDetails userIntegralDetails) {
        if (userIntegralDetailsService.creditsExchange(userIntegralDetails).get("code").equals(0)) {
            //将短剧加入到我的列表
            CourseUser courseUser = new CourseUser();
            //设置短剧id
            courseUser.setCourseId(userIntegralDetails.getGoodPId());
            if (UserIntegralDetails.Constants.CLASSIFY.COURSE_SINGLE.equals(userIntegralDetails.getClassify())) {
                courseUser.setClassify(2);
                courseUser.setCourseDetailsId(userIntegralDetails.getGoodId());
            } else {
                courseUser.setClassify(1);
            }

            //设置用户id
            courseUser.setUserId(userIntegralDetails.getUserId());
            //加入我的列表
            courseUserService.insertCourseUser(courseUser);
            return Result.success("积分兑换成功！");
        }else{
            return Result.error("积分数量不足！");
        }
    }

    @Login
    @GetMapping(value = "/creditsExchangeList/v1")
    @ApiOperation("积分兑换明细")
    @ResponseBody
    public Result creditsExchangeList(int page, int limit, @RequestAttribute Long userId) {
        return Result.success().put("data", userIntegralDetailsService.creditsExchangeList(page, limit, userId));
    }

    @Login
    @PostMapping(value = "/completeTask/{taskId}")
    @ApiOperation("完成积分任务")
    @ResponseBody
    public Result completeTask(@PathVariable Integer taskId, @RequestAttribute Long userId) {

       return  userIntegralDetailsService.completeTask(taskId, userId);
    }

    @Login
    @GetMapping(value = "/isCompleteTask/{taskId}")
    @ApiOperation("是否完成积分任务")
    @ResponseBody
    public Result isCompleteTask(@PathVariable Integer taskId, @RequestAttribute Long userId) {
       return  userIntegralDetailsService.isCompleteTask(taskId, userId);
    }
}
