package com.sqx.modules.ad.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.ad.dao.EventPropertyDao;
import com.sqx.modules.ad.entity.EventProperty;
import com.sqx.modules.ad.service.EventPropertyService;
import org.springframework.stereotype.Service;

/**
 * 事件属性服务实现类
 */
@Service
public class EventPropertyServiceImpl extends ServiceImpl<EventPropertyDao, EventProperty> implements EventPropertyService {
}
