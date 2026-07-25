package com.sqx.modules.shop.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.base.BasePagingRequest;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.shop.dao.ItemDao;
import com.sqx.modules.shop.dao.OrderDao;
import com.sqx.modules.shop.entity.Item;
import com.sqx.modules.shop.entity.Order;
import com.sqx.modules.shop.request.ItemQueryReq;
import com.sqx.modules.shop.request.OrderQueryReq;
import com.sqx.modules.shop.service.ItemService;
import com.sqx.modules.shop.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderDao, Order> implements OrderService {

    @Override
    public Result delete(String id) {
        if (StrUtil.isBlank(id)) {
            return Result.error("更新失败，请联系管理员");
        }
        Order order = new Order();
        order.setId(id);
        order.setDeleted(Boolean.FALSE);
        int rows = super.baseMapper.updateById(order);
        if (0 < rows) {
            return Result.success();
        }
        return Result.error("更新失败，请确认订单是否存在");
    }

    @Override
    public IPage<Order> page(OrderQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("id", true)));
        return baseMapper.selectPage(
                new Query<Order>().getPage(req),
                new QueryWrapper<Order>().lambda()
                        .eq(null != req.getId() && 0L != req.getId(), Order::getId, req.getId())
                        .like(StrUtil.isNotBlank(req.getNameLike()), Order::getItemName, req.getNameLike())
                        .lt(null != req.getCreatedEnd(), Order::getCreatedAt, req.getCreatedEnd())
                        .gt(null != req.getCreatedStart(), Order::getCreatedAt, req.getCreatedStart())
                        .eq(Order::getDeleted, Boolean.FALSE)
        );
    }
}
