package com.sqx.modules.shop.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.shop.entity.Item;
import com.sqx.modules.shop.request.ItemQueryReq;
import com.sqx.modules.shop.service.ItemService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "商品信息", tags = {"商品信息"})
@RequestMapping(value = "/shop/item")
@RequiredArgsConstructor
public class ItemController extends AbstractController {

    private final ItemService itemService;

    @PostMapping()
    @ApiOperation("添加商品信息")
    public Result insertItem(@RequestBody Item item) {
        return Result.success().setData(itemService.save(item));
    }

    @PutMapping()
    @ApiOperation("修改商品信息")
    public Result updateItem(@RequestBody Item item) {
        return Result.success().setData(itemService.saveOrUpdate(item));
    }
    
    @PutMapping("/shelves/{id}/{shelves}")
    @ApiOperation("上下架")
    public Result updateItemShelves(@PathVariable Long id, @PathVariable Boolean shelves) {
        Item item = new Item();
        item.setId(id);
        item.setShelves(shelves);
        return Result.success().setData(itemService.updateById(item));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("假删除")
    public Result updateDelete(@PathVariable Long id) {
        return Result.success().setData(itemService.delete(id));
    }

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


}