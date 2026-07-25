package com.sqx.modules.ad.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.ad.entity.EventTrack;
import org.apache.ibatis.annotations.Mapper;

/**
 * 事件主表DAO
 */
@Mapper
public interface EventTrackDao extends BaseMapper<EventTrack> {
}
