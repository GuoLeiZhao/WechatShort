package com.sqx.modules.ad.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.ad.dao.EventTrackDao;
import com.sqx.modules.ad.entity.EventProperty;
import com.sqx.modules.ad.entity.EventTrack;
import com.sqx.modules.ad.request.EventTrackRequest;
import com.sqx.modules.ad.service.EventPropertyService;
import com.sqx.modules.ad.service.EventTrackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * 事件监控服务实现类
 */
@Slf4j
@Service
public class EventTrackServiceImpl extends ServiceImpl<EventTrackDao, EventTrack> implements EventTrackService {

    @Autowired
    private EventPropertyService eventPropertyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEventTrack(EventTrackRequest request) {
        // 1. 保存事件主表数据
        EventTrack eventTrack = new EventTrack();
        eventTrack.setEventId(request.getEventId());
        eventTrack.setUserId(request.getUserId());
        eventTrack.setSessionId(request.getSessionId());
        eventTrack.setDeviceId(request.getDeviceId());
        eventTrack.setPlatform(request.getPlatform());
        eventTrack.setAppVersion(request.getAppVersion());
        eventTrack.setChannel(request.getChannel());
        eventTrack.setIpAddress(request.getIpAddress());
        eventTrack.setUserAgent(request.getUserAgent());
        eventTrack.setEventTime(request.getEventTime());
        eventTrack.setCreatedAt(new Date());
        eventTrack.setUpdatedAt(new Date());
        eventTrack.setDeleted(false);

        // 保存事件记录
        this.save(eventTrack);

        // 2. 保存事件属性数据
        if (request.getProperties() != null && !request.getProperties().isEmpty()) {
            for (Map.Entry<String, Object> entry : request.getProperties().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                // 验证属性键名长度
                if (key != null && key.length() > 256) {
                    log.warn("属性键名长度超过256字符，已跳过: {}", key);
                    continue;
                }

                // 验证属性值长度（base64后不超过1024字符）
                String valueStr = value != null ? value.toString() : "";
                if (valueStr.length() > 1024) {
                    log.warn("属性值长度超过1024字符，已跳过: key={}, value={}", key, valueStr);
                    continue;
                }

                // 判断属性类型
                String propertyType = "string";
                if (value instanceof Number) {
                    propertyType = "number";
                }

                EventProperty eventProperty = new EventProperty();
                eventProperty.setEventTrackId(eventTrack.getId());
                eventProperty.setPropertyKey(key);
                eventProperty.setPropertyValue(valueStr);
                eventProperty.setPropertyType(propertyType);
                eventProperty.setCreatedAt(new Date());

                eventPropertyService.save(eventProperty);
            }
        }

        log.info("事件上报成功: eventId={}, userId={}", request.getEventId(), request.getUserId());
    }
}
