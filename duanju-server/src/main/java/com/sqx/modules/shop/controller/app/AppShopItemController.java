package com.sqx.modules.shop.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.shop.request.ItemQueryReq;
import com.sqx.modules.shop.service.ItemService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(value = "商品信息", tags = {"商品信息"})
@RequestMapping(value = "/app/shop/item")
@RequiredArgsConstructor
public class AppShopItemController extends AbstractController {

    private final ItemService itemService;
    @GetMapping("/selectPage")
    @ApiOperation("查询商品信息")
    public Result selectItem(ItemQueryReq req) {
        return Result.success().setData(itemService.page(req));
    }

    @GetMapping("/selectById/{id}")
    @ApiOperation("根据id查询商品详细信息")
    public Result selectItemById(@PathVariable Long id) {
        return Result.success().setData(itemService.getById(id));
    }

    @Login
    @GetMapping("/{userId}")
    @ApiOperation("根据用户id查询与其积分最接近的商品信息")
    public Result selectItemByUserId(@PathVariable Long userId) {
        return Result.success().setData(itemService.selectClosestItemByUserId(userId));
    }

}
