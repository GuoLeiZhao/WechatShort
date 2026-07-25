package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.base.BaseDropList;
import com.sqx.modules.app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {


    IPage<UserEntity> selectUserPage(Page<UserEntity> page, String search, Integer sex, String platform,
                                     String sysPhone, Integer status,Integer member,String inviterCode,String userName);

    int queryInviterCount(@Param("inviterCode") String inviterCode);

    int queryUserCount(@Param("type") int type, @Param("date") String date,String platform);

    Double queryPayMoney(@Param("type") int type, @Param("date") String date);

    IPage<Map<String, Object>> queryCourseOrder(Page iPage,@Param("type") int type, @Param("date") String date);

    int userMessage( String date, int type);

    int insertUser(UserEntity userEntity);

    IPage<UserEntity> selectInviteUserList(Page<UserEntity> page,String userName,String phone);

    int selectUserOnLineCount();

    List<BaseDropList> selectUserDropList();
    List<UserEntity> selectUserList(List<Long> list);
}
