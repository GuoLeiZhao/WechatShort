package com.sqx.modules.orders.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.entity.PayRequest;

public interface OrdersService extends IService<Orders> {
    /**
     * 处理所有订单
     *
     * @param orders
     * @return
     */
    Result insertOrders(Orders orders);

    Result payMoney(Long orderId);
//    Result payMoney(PayRequest payRequest);

    Result insertCourseOrders(Long courseId, Long courseDetailsId,Long userId);

    Result insertVipOrders(Long vipDetailsId, Long userId);

    Result refundOrder(Long ordersId, String refundContent);

    Result selectOrders(Integer page, Integer limit, String ordersNo,Integer status,Long userId,Long courseId,Integer flag,String time);

    Result selectOrderByUserId(Integer page, Integer limit, Long userId);

    Result deleteOrders(String ids);

    Orders selectOrderById(Long orderId);

    Orders selectOrderByOrdersNo(String ordersNo);

    Double statisticsIncomeMoney(String time,Integer flag,Integer ordersType);

    Orders selectOrdersByCourseIdAndUserId(Long userId,Long courseId);

    Result selectOrdersMoneyList(Integer page,Integer limit,Integer flag,String time);

    Integer selectOrdersCount(Integer status,Integer ordersType,Integer flag,String time);

    Double selectOrdersMoney(Integer status,Integer ordersType,Integer flag,String time,Long courseId);

}
