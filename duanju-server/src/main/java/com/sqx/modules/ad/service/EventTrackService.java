package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.ad.entity.EventTrack;
import com.sqx.modules.ad.request.EventTrackRequest;

/**
 * 事件监控服务接口
 */
public interface EventTrackService extends IService<EventTrack> {

    /**
     * 保存事件追踪数据
     *
     * @param request 事件上报请求
     */
    void saveEventTrack(EventTrackRequest request);
}
