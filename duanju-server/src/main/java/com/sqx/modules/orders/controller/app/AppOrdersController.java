package com.sqx.modules.orders.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.orders.entity.PayRequest;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 订单信息
 *
 * @since 2021-07-17
 */
@RestController
@Api(value = "订单信息", tags = {"订单信息"})
@RequestMapping(value = "/app/order")
@Slf4j
public class AppOrdersController extends AbstractController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 生成商品订单
     *
     * @param courseId
     * @param userId
     * @return
     */
    @Login
    @GetMapping("/insertCourseOrders")
    @ApiOperation("生成商品订单")
    public Result insertCourseOrders(Long courseId,Long courseDetailsId, @RequestAttribute("userId") Long userId) {
        return ordersService.insertCourseOrders(courseId, courseDetailsId,userId);
    }

    @Login
    @GetMapping("/insertVipOrders")
    @ApiOperation("生成会员订单")
    public Result insertVipOrders(@ApiParam("会员详情信息") Long vipDetailsId, @RequestAttribute("userId") Long userId) {
        return ordersService.insertVipOrders(vipDetailsId, userId);
    }

    @Login
    @PostMapping("/payOrders")
    @ApiOperation("支付订单")
    public Result payOrders(Long orderId){
        return ordersService.payMoney(orderId);
    }

//    @Login
//    @PostMapping("/payOrders/v2")
//    @ApiOperation("支付订单")
//    public Result payOrders(@RequestBody PayRequest payRequest){
//        return ordersService.payMoney(payRequest);
//    }

    @Login
    @GetMapping("/refundOrder")
    @ApiOperation("退款订单")
    public Result refundOrder(Long orderId, String refundContent) {
        return ordersService.refundOrder(orderId, refundContent);
    }

    @Login
    @GetMapping("/selectOrderByUserId")
    @ApiOperation("我的订单")
    public Result selectOrderByUserId(Integer page, Integer limit, @RequestAttribute("userId") Long userId) {
        return ordersService.selectOrderByUserId(page, limit, userId);
    }

}
