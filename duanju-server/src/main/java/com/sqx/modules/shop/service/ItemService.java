package com.sqx.modules.shop.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.shop.entity.Item;
import com.sqx.modules.shop.request.ItemQueryReq;

public interface ItemService extends IService<Item> {

    /**
     * 根据id删除
     * @param id
     * @return
     */
    Result delete(Long id);

    /**
     * 分页查询
     * @param req
     * @return
     */
    IPage<Item> page(ItemQueryReq req);

    Boolean buy(Long itemId, Long userId, String orderId);

    Result selectClosestItemByUserId(Long userId);

}
