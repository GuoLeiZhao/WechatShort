package com.sqx.modules.shortUrl.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.shortUrl.entity.ShortUrlLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShortUrlLogDao extends BaseMapper<ShortUrlLog> {
}
