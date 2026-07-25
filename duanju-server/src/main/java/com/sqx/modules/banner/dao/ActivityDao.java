package com.sqx.modules.banner.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.banner.entity.Activity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 */
@Mapper
public interface ActivityDao extends BaseMapper<Activity> {


    List<Activity> selectByState(String state);

}
