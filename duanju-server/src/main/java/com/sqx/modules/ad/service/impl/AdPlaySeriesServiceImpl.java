package com.sqx.modules.ad.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.base.BasePagingRequest;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.dao.AdPlaySeriesDao;
import com.sqx.modules.ad.entity.AdPlaySeries;
import com.sqx.modules.ad.request.AdPlaySeriesQueryReq;
import com.sqx.modules.ad.service.AdPlaySeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdPlaySeriesServiceImpl extends ServiceImpl<AdPlaySeriesDao, AdPlaySeries> implements AdPlaySeriesService {
    @Override
    public IPage<AdPlaySeries> page(AdPlaySeriesQueryReq req) {
        List<BasePagingRequest.Order> orders = new ArrayList<>();
        orders.add(new BasePagingRequest.Order("play_id", false));
        orders.add(new BasePagingRequest.Order("series_num", true));
        req.setOrders(orders);
        return baseMapper.selectPage(
                new Query<AdPlaySeries>().getPage(req),
                new QueryWrapper<AdPlaySeries>().lambda()
                        .eq(StrUtil.isNotBlank(req.getSeriesId()), AdPlaySeries::getSeriesId, req.getSeriesId())
                        .eq(StrUtil.isNotBlank(req.getPlayId()), AdPlaySeries::getPlayId, req.getPlayId())
                        .eq(req.getSeriesNum() != null, AdPlaySeries::getSeriesNum, req.getSeriesNum())
                        .like(StrUtil.isNotBlank(req.getSeriesTitle()), AdPlaySeries::getSeriesTitle, req.getSeriesTitle())
                        .gt(null != req.getCreatedStart(), AdPlaySeries::getCreatedAt, req.getCreatedStart())
                        .lt(null != req.getCreatedEnd(), AdPlaySeries::getCreatedAt, req.getCreatedEnd())
        );
    }

    @Override
    public List<AdPlaySeries> dropList(AdPlaySeriesQueryReq req) {
        return baseMapper.selectList(
                new QueryWrapper<AdPlaySeries>().lambda()
                        .eq(StrUtil.isNotBlank(req.getPlayId()), AdPlaySeries::getPlayId, req.getPlayId())
                        .eq(StrUtil.isNotBlank(req.getSeriesId()), AdPlaySeries::getSeriesId, req.getSeriesId())
                        .like(StrUtil.isNotBlank(req.getSeriesTitle()), AdPlaySeries::getSeriesTitle, req.getSeriesTitle())
                        .orderByAsc(AdPlaySeries::getSeriesNum)
        );
    }

    @Override
    public Result insert(AdPlaySeries adPlaySeries) {
        int rows = baseMapper.insert(adPlaySeries);
        if (rows > 0) {
            return Result.success("添加成功！");
        } else {
            return Result.error("添加失败");
        }
    }

    @Override
    public Result deleteById(String seriesId) {
        int rows = baseMapper.deleteById(seriesId);
        if (rows > 0) {
            return Result.success("删除成功！");
        } else {
            return Result.error("删除失败");
        }
    }

    @Override
    public Result update(AdPlaySeries adPlaySeries) {
        QueryWrapper<AdPlaySeries> whereWrapper = new QueryWrapper<>();
        whereWrapper.eq("series_id", adPlaySeries.getSeriesId());
        int rows = baseMapper.update(adPlaySeries, whereWrapper);
        if (rows > 0) {
            return Result.success("更新成功！");
        } else {
            return Result.error("更新失败");
        }
    }

    @Override
    public Result selectById(String seriesId) {
        AdPlaySeries adPlaySeries = baseMapper.selectById(seriesId);
        if (adPlaySeries != null) {
            return Result.success().setData(adPlaySeries);
        } else {
            return Result.error("获取失败");
        }
    }

    @Override
    public Result selectBatchIds(List<String> ids) {
        List<AdPlaySeries> adPlaySeriesList = baseMapper.selectBatchIds(ids);
        if (adPlaySeriesList != null) {
            return Result.success().setData(adPlaySeriesList);
        } else {
            return Result.error("获取失败");
        }
    }

}
