package com.sqx.modules.orders.controller;

import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value = "订单信息", tags = {"订单信息"})
@RequestMapping(value = "/order")
public class OrdersController extends AbstractController {
    @Autowired
    private OrdersService ordersService;

    @GetMapping("/selectOrders")
    @ApiOperation("订单信息列表")
    public Result selectOrders(Integer page,Integer limit,String ordersNo,Integer status,Long userId,Long courseId,Integer flag,String time){
        return ordersService.selectOrders(page,limit,ordersNo,status,userId,courseId,flag,time);
    }

    @GetMapping("/deleteOrders")
    @ApiOperation("删除订单")
    public Result deleteOrders(String ids){
        return ordersService.deleteOrders(ids);
    }

    @GetMapping("/selectOrderByUserId")
    @ApiOperation("我的订单")
    public Result selectOrderByUserId(Integer page, Integer limit, Long userId) {
        return ordersService.selectOrderByUserId(page, limit, userId);
    }

    @GetMapping("/selectOrdersMoneyList")
    @ApiOperation("订单收入分析")
    public Result selectOrdersMoneyList(Integer page,Integer limit,Integer flag,String time){
        return ordersService.selectOrdersMoneyList(page, limit, flag, time);
    }

    @PostMapping("/refundOrders")
    @ApiOperation("退款订单")
    public Result refundOrders(Long ordersId){
        return ordersService.refundOrder(ordersId,"系统退款");
    }


    @GetMapping("/selectOrdersCount")
    @ApiOperation("订单统计")
    public Result selectOrdersCount(Integer flag,String time){
        //短剧订单 总  待  完  退
        Integer sumCourseOrdersCount = ordersService.selectOrdersCount(null, 3, flag, time);
        Integer daiCourseKeOrdersCount = ordersService.selectOrdersCount(0, 3, flag, time);
        Integer wanCourseKeOrdersCount = ordersService.selectOrdersCount(1, 3, flag, time);
        Integer tuiCourseOrdersCount = ordersService.selectOrdersCount(2, 3, flag, time);
        //短剧钱
        Double sumCourseOrdersMoney = ordersService.selectOrdersMoney(null, 3, flag, time,null);
        Double daiCourseOrdersMoney = ordersService.selectOrdersMoney(0, 3, flag, time,null);
        Double wanCourseOrdersMoney = ordersService.selectOrdersMoney(1, 3, flag, time,null);
        Double tuiCourseOrdersMoney = ordersService.selectOrdersMoney(2, 3, flag, time,null);
        //会员订单 总  待  完  退
        Integer sumMemberOrdersCount = ordersService.selectOrdersCount(null, 2, flag, time);
        Integer daiMemberKeOrdersCount = ordersService.selectOrdersCount(0, 2, flag, time);
        Integer wanMemberKeOrdersCount = ordersService.selectOrdersCount(1, 2, flag, time);
        Integer tuiMemberOrdersCount = ordersService.selectOrdersCount(2, 2, flag, time);
        //会员钱
        Double sumMemberOrdersMoney = ordersService.selectOrdersMoney(null, 2, flag, time,null);
        Double daiMemberOrdersMoney = ordersService.selectOrdersMoney(0, 2, flag, time,null);
        Double wanMemberOrdersMoney = ordersService.selectOrdersMoney(1, 2, flag, time,null);
        Double tuiMemberOrdersMoney = ordersService.selectOrdersMoney(2, 2, flag, time,null);
        Map<String,Object> result=new HashMap<>();
        result.put("sumCourseOrdersCount",sumCourseOrdersCount);result.put("daiCourseKeOrdersCount",daiCourseKeOrdersCount);
        result.put("wanCourseKeOrdersCount",wanCourseKeOrdersCount);result.put("tuiCourseOrdersCount",tuiCourseOrdersCount);
        result.put("sumCourseOrdersMoney",sumCourseOrdersMoney);result.put("daiCourseOrdersMoney",daiCourseOrdersMoney);
        result.put("wanCourseOrdersMoney",wanCourseOrdersMoney);result.put("tuiCourseOrdersMoney",tuiCourseOrdersMoney);
        result.put("sumMemberOrdersCount",sumMemberOrdersCount);result.put("daiMemberKeOrdersCount",daiMemberKeOrdersCount);
        result.put("wanMemberKeOrdersCount",wanMemberKeOrdersCount);result.put("tuiMemberOrdersCount",tuiMemberOrdersCount);
        result.put("sumMemberOrdersMoney",sumMemberOrdersMoney);result.put("daiMemberOrdersMoney",daiMemberOrdersMoney);
        result.put("wanMemberOrdersMoney",wanMemberOrdersMoney);result.put("tuiMemberOrdersMoney",tuiMemberOrdersMoney);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectCourseOrdersMoneyCount")
    @ApiOperation("统计具体剧的收益")
    public Result selectCourseOrdersMoneyCount(Long courseId){
        //总收益
        Double sumMoney = ordersService.selectOrdersMoney(1, 1, null, null,courseId);
        String dateTime = DateUtils.format(new Date());
        //年
        Double yearMoney = ordersService.selectOrdersMoney(1, 1, 3, dateTime,courseId);
        //月
        Double monthMoney = ordersService.selectOrdersMoney(1, 1, 2, dateTime,courseId);
        //日
        Double dayMoney = ordersService.selectOrdersMoney(1, 1, 1, dateTime,courseId);
        Map<String,Double> result=new HashMap<>();
        result.put("sumMoney",sumMoney);
        result.put("yearMoney",yearMoney);
        result.put("monthMoney",monthMoney);
        result.put("dayMoney",dayMoney);
        return Result.success().put("data",result);
    }



}
