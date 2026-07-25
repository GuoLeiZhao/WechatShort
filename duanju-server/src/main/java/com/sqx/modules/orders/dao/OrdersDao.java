package com.sqx.modules.orders.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.orders.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrdersDao extends BaseMapper<Orders> {

    String selectMaxCode(String newData);

    int insertOrders(Orders orders);

    IPage<Orders> selectOrdersByOrdersNo(Page<Orders> pages, @Param("ordersNo") String ordersNo,@Param("status") Integer status,
                                         @Param("userId") Long userId,@Param("courseId") Long courseId,@Param("flag") Integer flag,
                                         @Param("time") String time);

    int deleteOrders(String[] ids);

    Double statisticsIncomeMoney(@Param("time") String time, @Param("flag") Integer flag, @Param("ordersType") Integer ordersType);

    Orders selectOrdersByCourseIdAndUserId(Long userId,Long courseId);

    IPage<Orders> selectOrdersMoneyList(Page<Orders> page,Integer flag,String time);

    Integer selectOrdersCount(Integer status,Integer ordersType,Integer flag,String time);

    Double selectOrdersMoney(Integer status,Integer ordersType,Integer flag,String time,Long courseId);

    List<Orders> selectOrdersCourseCollectByCourseAndUser(Long courseId, Long userId);
}
