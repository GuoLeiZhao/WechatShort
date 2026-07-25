package com.sqx.modules.shop.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.shop.entity.Order;
import com.sqx.modules.shop.service.ItemService;
import com.sqx.modules.shop.service.OrderService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.utils.ID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(value = "订单信息", tags = {"订单信息"})
@RequestMapping(value = "/app/shop/order")
@RequiredArgsConstructor
public class AppShopOrderController extends AbstractController {

    private final OrderService orderService;
    private final ItemService itemService;
    private final UserService userService;

    @Login
    @PostMapping()
    @ApiOperation("购买商品")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Result buyItem(@RequestBody Order order) {
        Long itemId = order.getItemId();

        // 扣除库存
        Long userId = order.getOrderUserId();
        String orderId = ID.get();
        if (itemService.buy(itemId, userId, orderId)) {
            // 新增订单
            order.setId(orderId);
            order.setOrderStatus(Order.OrderStatus.PAID);
            order.setCreatedBy(userService.queryByUserId(userId).getUserName());
            return Result.success().setData(orderService.save(order));
        }

        return Result.error("兑换失败，请联系客服！");

    }

    @Login
    @GetMapping("/{orderId}")
    @ApiOperation("查询订单")
    public Result buyItem(@PathVariable String orderId) {
        return Result.success().setData(orderService.getById(orderId));
    }


}
