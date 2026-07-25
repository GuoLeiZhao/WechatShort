package com.sqx.modules.ad.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.request.EventTrackRequest;
import com.sqx.modules.ad.service.EventTrackService;
import com.sqx.modules.ad.service.MiniProgramLifecycleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 事件监控控制器
 */
@Slf4j
@RestController
@RequestMapping("/app/event")
@Api(tags = "事件监控")
public class AppEventTrackController {

    @Autowired
    private EventTrackService eventTrackService;

    @Autowired
    private MiniProgramLifecycleService miniProgramLifecycleService;

    /**
     * 事件上报接口
     */
    @PostMapping("/track")
    @ApiOperation("事件上报")
    public Result trackEvent(@RequestBody @Validated EventTrackRequest request, HttpServletRequest httpRequest) {
        try {
            // 如果没有设置事件时间，使用当前时间
            if (request.getEventTime() == null) {
                request.setEventTime(new Date());
            }

            // 如果没有设置IP地址，从请求中获取
            if (request.getIpAddress() == null) {
                request.setIpAddress(getClientIpAddress(httpRequest));
            }

            // 如果没有设置用户代理，从请求中获取
            if (request.getUserAgent() == null) {
                request.setUserAgent(httpRequest.getHeader("User-Agent"));
            }

            // 保存事件数据
            eventTrackService.saveEventTrack(request);

            return Result.success("事件上报成功");
        } catch (Exception e) {
            log.error("事件上报失败", e);
            return Result.error("事件上报失败：" + e.getMessage());
        }
    }

    /**
     * 小程序初始化事件上报 (onLaunch)
     */
    @PostMapping("/miniprogram/launch")
    @ApiOperation("小程序初始化事件上报")
    public Result trackAppLaunch(@RequestBody @Validated EventTrackRequest request, HttpServletRequest httpRequest) {
        try {
            // 补充请求信息
            enrichRequest(request, httpRequest);
            
            // 调用小程序生命周期服务
            miniProgramLifecycleService.trackAppLaunch(request);
            
            return Result.success("小程序初始化事件上报成功");
        } catch (Exception e) {
            log.error("小程序初始化事件上报失败", e);
            return Result.error("小程序初始化事件上报失败：" + e.getMessage());
        }
    }

    /**
     * 小程序显示事件上报 (onShow)
     */
    @PostMapping("/miniprogram/show")
    @ApiOperation("小程序显示事件上报")
    public Result trackAppShow(@RequestBody @Validated EventTrackRequest request, HttpServletRequest httpRequest) {
        try {
            // 补充请求信息
            enrichRequest(request, httpRequest);
            
            // 调用小程序生命周期服务
            miniProgramLifecycleService.trackAppShow(request);
            
            return Result.success("小程序显示事件上报成功");
        } catch (Exception e) {
            log.error("小程序显示事件上报失败", e);
            return Result.error("小程序显示事件上报失败：" + e.getMessage());
        }
    }

    /**
     * 小程序隐藏事件上报 (onHide)
     */
    @PostMapping("/miniprogram/hide")
    @ApiOperation("小程序隐藏事件上报")
    public Result trackAppHide(@RequestBody @Validated EventTrackRequest request, HttpServletRequest httpRequest) {
        try {
            // 补充请求信息
            enrichRequest(request, httpRequest);
            
            // 调用小程序生命周期服务
            miniProgramLifecycleService.trackAppHide(request);
            
            return Result.success("小程序隐藏事件上报成功");
        } catch (Exception e) {
            log.error("小程序隐藏事件上报失败", e);
            return Result.error("小程序隐藏事件上报失败：" + e.getMessage());
        }
    }

    /**
     * 小程序错误事件上报 (onError)
     */
    @PostMapping("/miniprogram/error")
    @ApiOperation("小程序错误事件上报")
    public Result trackAppError(@RequestBody @Validated EventTrackRequest request, HttpServletRequest httpRequest) {
        try {
            // 补充请求信息
            enrichRequest(request, httpRequest);
            
            // 调用小程序生命周期服务
            miniProgramLifecycleService.trackAppError(request);
            
            return Result.success("小程序错误事件上报成功");
        } catch (Exception e) {
            log.error("小程序错误事件上报失败", e);
            return Result.error("小程序错误事件上报失败：" + e.getMessage());
        }
    }

    /**
     * 用户进入小程序事件上报 (兼容原有格式)
     */
    @PostMapping("/miniprogram/enter")
    @ApiOperation("用户进入小程序事件上报")
    public Result trackAppEnter(@RequestBody @Validated EventTrackRequest request, HttpServletRequest httpRequest) {
        try {
            // 补充请求信息
            enrichRequest(request, httpRequest);
            
            // 调用小程序生命周期服务
            miniProgramLifecycleService.trackAppEnter(request);
            
            return Result.success("用户进入小程序事件上报成功");
        } catch (Exception e) {
            log.error("用户进入小程序事件上报失败", e);
            return Result.error("用户进入小程序事件上报失败：" + e.getMessage());
        }
    }

    /**
     * 用户退出小程序事件上报 (兼容原有格式)
     */
    @PostMapping("/miniprogram/exit")
    @ApiOperation("用户退出小程序事件上报")
    public Result trackAppExit(@RequestBody @Validated EventTrackRequest request, HttpServletRequest httpRequest) {
        try {
            // 补充请求信息
            enrichRequest(request, httpRequest);
            
            // 调用小程序生命周期服务
            miniProgramLifecycleService.trackAppExit(request);
            
            return Result.success("用户退出小程序事件上报成功");
        } catch (Exception e) {
            log.error("用户退出小程序事件上报失败", e);
            return Result.error("用户退出小程序事件上报失败：" + e.getMessage());
        }
    }

    /**
     * 补充请求信息
     */
    private void enrichRequest(EventTrackRequest request, HttpServletRequest httpRequest) {
        // 如果没有设置事件时间，使用当前时间
        if (request.getEventTime() == null) {
            request.setEventTime(new Date());
        }

        // 如果没有设置IP地址，从请求中获取
        if (request.getIpAddress() == null) {
            request.setIpAddress(getClientIpAddress(httpRequest));
        }

        // 如果没有设置用户代理，从请求中获取
        if (request.getUserAgent() == null) {
            request.setUserAgent(httpRequest.getHeader("User-Agent"));
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
