package com.sqx.modules.app.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.app.entity.UserMoneyDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMoneyDetailsDao extends BaseMapper<UserMoneyDetails> {


    Double monthIncome(@Param("date") String date,@Param("userId") Long userId);

}
