package com.sqx.modules.ad.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.base.BasePagingRequest;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.dao.AdPlayDao;
import com.sqx.modules.ad.entity.AdPlay;
import com.sqx.modules.ad.request.AdPlayQueryReq;
import com.sqx.modules.ad.service.AdPlayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdPlayServiceImpl extends ServiceImpl<AdPlayDao, AdPlay> implements AdPlayService {
    @Override
    public IPage<AdPlay> page(AdPlayQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("created_at", false)));
        return baseMapper.selectPage(
                new Query<AdPlay>().getPage(req),
                new QueryWrapper<AdPlay>().lambda()
                        .eq(StrUtil.isNotBlank(req.getPlayId()), AdPlay::getPlayId, req.getPlayId())
                        .like(StrUtil.isNotBlank(req.getPlayName()), AdPlay::getPlayName, req.getPlayName())
                        .gt(null != req.getCreatedStart(), AdPlay::getCreatedAt, req.getCreatedStart())
                        .lt(null != req.getCreatedEnd(), AdPlay::getCreatedAt, req.getCreatedEnd())
        );
    }

    @Override
    public List<AdPlay> dropList(AdPlayQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("created_at", false)));
        return baseMapper.selectList(
                new QueryWrapper<AdPlay>().lambda()
                        .eq(StrUtil.isNotBlank(req.getPlayId()), AdPlay::getPlayId, req.getPlayId())
                        .like(StrUtil.isNotBlank(req.getPlayName()), AdPlay::getPlayName, req.getPlayName())
        );
    }

    @Override
    public Result insert(AdPlay adPlay) {
        int rows = baseMapper.insert(adPlay);
        if (rows > 0) {
            return Result.success("添加成功！");
        } else {
            return Result.error("添加失败");
        }
    }

    @Override
    public Result deleteById(String playId) {
        int rows = baseMapper.deleteById(playId);
        if (rows > 0) {
            return Result.success("删除成功！");
        } else {
            return Result.error("删除失败");
        }
    }

    @Override
    public Result update(AdPlay adPlay) {
        QueryWrapper<AdPlay> whereWrapper = new QueryWrapper<>();
        whereWrapper.eq("play_id", adPlay.getPlayId());
        int rows = baseMapper.update(adPlay, whereWrapper);
        if (rows > 0) {
            return Result.success("更新成功！");
        } else {
            return Result.error("更新失败");
        }
    }

    @Override
    public Result selectById(String playId) {
        AdPlay adPlay = baseMapper.selectById(playId);
        if (adPlay != null) {
            return Result.success().setData(adPlay);
        } else {
            return Result.error("获取失败");
        }
    }

    @Override
    public Result selectBatchIds(List<String> ids) {
        List<AdPlay> adPlayList = baseMapper.selectBatchIds(ids);
        if (adPlayList != null) {
            return Result.success().setData(adPlayList);
        } else {
            return Result.error("获取失败");
        }
    }

}
