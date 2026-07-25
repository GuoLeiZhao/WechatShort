package com.sqx.modules.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.shop.entity.Item;
import com.sqx.modules.shop.entity.Order;
import com.sqx.modules.shop.request.ItemQueryReq;
import com.sqx.modules.shop.request.OrderQueryReq;

public interface OrderService extends IService<Order> {

    /**
     * 根据id删除
     * @param id
     * @return
     */
    Result delete(String id);

    /**
     * 分页查询
     * @param req
     * @return
     */
    IPage<Order> page(OrderQueryReq req);

}
