package com.sqx.modules.applog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.applog.entity.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogDao extends BaseMapper<Log> {
}
