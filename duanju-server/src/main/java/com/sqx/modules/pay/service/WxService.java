package com.sqx.modules.pay.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.orders.entity.Orders;

import java.math.BigDecimal;


/**
 */
public interface WxService {

    Result payOrder(Long orderId, Integer type) throws Exception;

    Result payMoney(BigDecimal money, Long userId, Integer classify, Long courseId,Long courseDetailsId) throws Exception;

    String payBack(String resXml,Integer type);

    boolean refund(Orders orders);

}