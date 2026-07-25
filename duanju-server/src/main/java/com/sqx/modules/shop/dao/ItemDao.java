package com.sqx.modules.shop.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.shop.entity.Item;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemDao extends BaseMapper<Item> {
}
