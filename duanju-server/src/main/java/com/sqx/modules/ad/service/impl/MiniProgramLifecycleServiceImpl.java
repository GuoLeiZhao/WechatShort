package com.sqx.modules.ad.service.impl;

import com.sqx.modules.ad.constant.MiniProgramEventConstant;
import com.sqx.modules.ad.request.EventTrackRequest;
import com.sqx.modules.ad.service.EventTrackService;
import com.sqx.modules.ad.service.MiniProgramLifecycleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 小程序生命周期事件服务实现类
 */
@Slf4j
@Service
public class MiniProgramLifecycleServiceImpl implements MiniProgramLifecycleService {

    @Autowired
    private EventTrackService eventTrackService;

    @Override
    public void trackAppLaunch(EventTrackRequest request) {
        try {
            // 设置事件ID
            request.setEventId(MiniProgramEventConstant.EventId.APP_LAUNCH);
            
            // 如果没有设置事件时间，使用当前时间
            if (request.getEventTime() == null) {
                request.setEventTime(new Date());
            }

            // 确保平台类型为小程序
            request.setPlatform("miniprogram");

            // 保存事件数据
            eventTrackService.saveEventTrack(request);
            
            log.info("小程序初始化事件上报成功: userId={}, sessionId={}", 
                    request.getUserId(), request.getSessionId());
        } catch (Exception e) {
            log.error("小程序初始化事件上报失败", e);
            throw e;
        }
    }

    @Override
    public void trackAppShow(EventTrackRequest request) {
        try {
            // 设置事件ID
            request.setEventId(MiniProgramEventConstant.EventId.APP_SHOW);
            
            // 如果没有设置事件时间，使用当前时间
            if (request.getEventTime() == null) {
                request.setEventTime(new Date());
            }

            // 确保平台类型为小程序
            request.setPlatform("miniprogram");

            // 保存事件数据
            eventTrackService.saveEventTrack(request);
            
            log.info("小程序显示事件上报成功: userId={}, sessionId={}", 
                    request.getUserId(), request.getSessionId());
        } catch (Exception e) {
            log.error("小程序显示事件上报失败", e);
            throw e;
        }
    }

    @Override
    public void trackAppHide(EventTrackRequest request) {
        try {
            // 设置事件ID
            request.setEventId(MiniProgramEventConstant.EventId.APP_HIDE);
            
            // 如果没有设置事件时间，使用当前时间
            if (request.getEventTime() == null) {
                request.setEventTime(new Date());
            }

            // 确保平台类型为小程序
            request.setPlatform("miniprogram");

            // 保存事件数据
            eventTrackService.saveEventTrack(request);
            
            log.info("小程序隐藏事件上报成功: userId={}, sessionId={}", 
                    request.getUserId(), request.getSessionId());
        } catch (Exception e) {
            log.error("小程序隐藏事件上报失败", e);
            throw e;
        }
    }

    @Override
    public void trackAppError(EventTrackRequest request) {
        try {
            // 设置事件ID
            request.setEventId(MiniProgramEventConstant.EventId.APP_ERROR);
            
            // 如果没有设置事件时间，使用当前时间
            if (request.getEventTime() == null) {
                request.setEventTime(new Date());
            }

            // 确保平台类型为小程序
            request.setPlatform("miniprogram");

            // 保存事件数据
            eventTrackService.saveEventTrack(request);
            
            log.info("小程序错误事件上报成功: userId={}, sessionId={}", 
                    request.getUserId(), request.getSessionId());
        } catch (Exception e) {
            log.error("小程序错误事件上报失败", e);
            throw e;
        }
    }

    @Override
    public void trackAppEnter(EventTrackRequest request) {
        try {
            // 设置事件ID
            request.setEventId(MiniProgramEventConstant.EventId.APP_ENTER);
            
            // 如果没有设置事件时间，使用当前时间
            if (request.getEventTime() == null) {
                request.setEventTime(new Date());
            }

            // 确保平台类型为小程序
            request.setPlatform("miniprogram");

            // 保存事件数据
            eventTrackService.saveEventTrack(request);
            
            log.info("用户进入小程序事件上报成功: userId={}, sessionId={}", 
                    request.getUserId(), request.getSessionId());
        } catch (Exception e) {
            log.error("用户进入小程序事件上报失败", e);
            throw e;
        }
    }

    @Override
    public void trackAppExit(EventTrackRequest request) {
        try {
            // 设置事件ID
            request.setEventId(MiniProgramEventConstant.EventId.APP_EXIT);
            
            // 如果没有设置事件时间，使用当前时间
            if (request.getEventTime() == null) {
                request.setEventTime(new Date());
            }

            // 确保平台类型为小程序
            request.setPlatform("miniprogram");

            // 保存事件数据
            eventTrackService.saveEventTrack(request);
            
            log.info("用户退出小程序事件上报成功: userId={}, sessionId={}", 
                    request.getUserId(), request.getSessionId());
        } catch (Exception e) {
            log.error("用户退出小程序事件上报失败", e);
            throw e;
        }
    }
}
