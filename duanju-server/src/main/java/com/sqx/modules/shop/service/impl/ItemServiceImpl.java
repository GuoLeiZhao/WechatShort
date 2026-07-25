package com.sqx.modules.shop.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.base.BasePagingRequest;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.integral.dao.UserIntegralDao;
import com.sqx.modules.integral.dao.UserIntegralDetailsDao;
import com.sqx.modules.integral.entity.UserIntegral;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.shop.dao.ItemDao;
import com.sqx.modules.shop.entity.Item;
import com.sqx.modules.shop.request.ItemQueryReq;
import com.sqx.modules.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl extends ServiceImpl<ItemDao, Item> implements ItemService {


    private final UserIntegralDao userIntegralDao;
    private final UserIntegralDetailsDao userIntegralDetailsDao;

    @Override
    public Result delete(Long id) {
        if (null == id || 0L == id) {
            return Result.error("更新失败，请联系管理员");
        }
        Item item = new Item();
        item.setId(id);
        item.setDeleted(Boolean.TRUE);
        int rows = super.baseMapper.updateById(item);
        if (0 < rows) {
            return Result.success();
        }
        return Result.error("更新失败，请确认商品是否存在");
    }

    @Override
    public IPage<Item> page(ItemQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("sort", true)));
        return baseMapper.selectPage(
                new Query<Item>().getPage(req),
                new QueryWrapper<Item>().lambda()
                        .eq(null != req.getId() && 0L != req.getId(), Item::getId, req.getId())
                        .like(StrUtil.isNotBlank(req.getNameLike()), Item::getName, req.getNameLike())
                        .eq(null != req.getShelves() && req.getShelves(), Item::getShelves, req.getShelves())
                        .lt(null != req.getCreatedEnd(), Item::getCreatedAt, req.getCreatedEnd())
                        .gt(null != req.getCreatedStart(), Item::getCreatedAt, req.getCreatedStart())
                        .eq(Item::getDeleted, Boolean.FALSE)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean buy(Long itemId, Long userId, String orderId) {
        Item item = this.getById(itemId);
        // 商品校验
        if (item == null) {
            throw new SqxException("商品不存在");
        }
        int price = item.getPrice().intValue();
        if (item.getStock() < 1) {
            throw new SqxException("商品库存不足，请联系客服增加");
        }
        if (item.getShelves() == Boolean.FALSE) {
            throw new SqxException("商品已下架");
        }

        // 用户积分校验
        UserIntegral userIntegral = userIntegralDao.selectById(userId);
        if (null == userIntegral) {
            throw new SqxException("用户积分不足");
        }

        if (userIntegral.getIntegralNum() < price) {
            throw new SqxException("用户积分不足");
        }

        synchronized (item) {

            // 扣除积分
            userIntegralDao.updateIntegral(UserIntegralDetails.Constants.TYPE.DECREASE, userId, price);

            // 增加扣分记录
            UserIntegralDetails userIntegralDetails = new UserIntegralDetails();
            userIntegralDetails.setGoodId(item.getId());
            userIntegralDetails.setGoodName(item.getName());
            userIntegralDetails.setGoodImg(item.getPic());
            userIntegralDetails.setOrderId(orderId);
            userIntegralDetails.setClassify(UserIntegralDetails.Constants.CLASSIFY.BUY);
            userIntegralDetails.setContent("兑换商品减少：" + price + "积分");
            userIntegralDetails.setCreateTime(DateUtil.now());
            userIntegralDetails.setNum(price);
            userIntegralDetails.setType(UserIntegralDetails.Constants.TYPE.DECREASE);
            userIntegralDetails.setUserId(userId);
            userIntegralDetailsDao.insert(userIntegralDetails);

            // 减商品库存
            item.setStock(item.getStock() - 1);
            return this.updateById(item);
        }
    }

    @Override
    public Result selectClosestItemByUserId(Long userId) {
        if (null == userId) {
            return Result.error("用户不存在");
        }

        UserIntegral userIntegral = userIntegralDao.selectById(userId);
        Item value = baseMapper.selectOne(new QueryWrapper<Item>().lambda()
                .gt(Item::getPrice, userIntegral.getIntegralNum())
                .eq(Item::getDeleted, Boolean.FALSE)
                .orderByAsc(Item::getPrice).last("limit 1")
        );

        if (null == value) {
            value = baseMapper.selectOne(new QueryWrapper<Item>().lambda()
                    .eq(Item::getDeleted, Boolean.FALSE)
                    .orderByAsc(Item::getPrice).last("limit 1")
            );
        }
        return Result.success().setData(value);
    }
}
