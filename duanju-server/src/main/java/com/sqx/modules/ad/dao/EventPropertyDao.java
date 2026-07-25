package com.sqx.modules.ad.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.ad.entity.EventProperty;
import org.apache.ibatis.annotations.Mapper;

/**
 * 事件属性表DAO
 */
@Mapper
public interface EventPropertyDao extends BaseMapper<EventProperty> {
}
