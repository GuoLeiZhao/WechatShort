package com.sqx.modules.ad.service;

import com.sqx.modules.ad.request.EventTrackRequest;

/**
 * 小程序生命周期事件服务接口
 */
public interface MiniProgramLifecycleService {

    /**
     * 小程序初始化事件 (onLaunch)
     * 小程序冷启动完成时触发，全局只触发一次
     *
     * @param request 事件请求
     */
    void trackAppLaunch(EventTrackRequest request);

    /**
     * 小程序显示事件 (onShow)
     * 当小程序启动，或从后台进入前台显示时触发
     *
     * @param request 事件请求
     */
    void trackAppShow(EventTrackRequest request);

    /**
     * 小程序隐藏事件 (onHide)
     * 当小程序从前台进入后台时触发
     *
     * @param request 事件请求
     */
    void trackAppHide(EventTrackRequest request);

    /**
     * 小程序错误事件 (onError)
     * 当小程序发生脚本错误，或者API调用失败时触发
     *
     * @param request 事件请求
     */
    void trackAppError(EventTrackRequest request);

    /**
     * 用户进入小程序事件 (兼容原有格式)
     *
     * @param request 事件请求
     */
    void trackAppEnter(EventTrackRequest request);

    /**
     * 用户退出小程序事件 (兼容原有格式)
     *
     * @param request 事件请求
     */
    void trackAppExit(EventTrackRequest request);
}
