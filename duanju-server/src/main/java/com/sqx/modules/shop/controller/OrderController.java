package com.sqx.modules.shop.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.shop.entity.Order;
import com.sqx.modules.shop.request.OrderQueryReq;
import com.sqx.modules.shop.service.OrderService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.utils.ID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "商品订单信息", tags = {"商品订单信息"})
@RequestMapping(value = "/shop/order")
@RequiredArgsConstructor
public class OrderController extends AbstractController {

    private final OrderService orderService;

    @PostMapping()
    @ApiOperation("添加订单信息")
    public Result insertOrder(@RequestBody Order order) {
        order.setId(ID.get());
        return Result.success().setData(orderService.save(order));
    }

    @PutMapping()
    @ApiOperation("修改订单信息")
    public Result updateOrder(@RequestBody Order order) {
        return Result.success().setData(orderService.saveOrUpdate(order));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("假删除")
    public Result updateDelete(@PathVariable String id) {
        return Result.success().setData(orderService.delete(id));
    }

    @GetMapping("/selectPage")
    @ApiOperation("查询订单信息")
    public Result selectOrder(OrderQueryReq req) {
        return Result.success().setData(orderService.page(req));
    }

    @GetMapping("/selectById/{id}")
    @ApiOperation("根据id查询订单详细信息")
    public Result selectOrderById(@PathVariable String id) {
        return Result.success().setData(orderService.getById(id));
    }


}