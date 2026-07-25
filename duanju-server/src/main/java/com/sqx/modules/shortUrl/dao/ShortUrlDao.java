package com.sqx.modules.shortUrl.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.shortUrl.entity.ShortUrl;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShortUrlDao extends BaseMapper<ShortUrl> {
}
