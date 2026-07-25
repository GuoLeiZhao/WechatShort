package com.sqx.modules.common.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.common.entity.CommonInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 */
@Mapper
public interface CommonInfoDao extends BaseMapper<CommonInfo> {

    List<CommonInfo> findByCondition(@Param("condition") String condition);

    CommonInfo findOne(@Param("type") int type);

    CommonInfo findByConditionAndMax(String max, String condition);
    CommonInfo findByConditionAndValue(String value, String condition);
}