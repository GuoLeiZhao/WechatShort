package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdPlaySeries;
import com.sqx.modules.ad.request.AdPlaySeriesQueryReq;

import java.util.List;

public interface AdPlaySeriesService extends IService<AdPlaySeries> {

    IPage<AdPlaySeries> page(AdPlaySeriesQueryReq req);

    List<AdPlaySeries> dropList(AdPlaySeriesQueryReq req);

    Result insert(AdPlaySeries adPlaySeries);

    Result deleteById(String seriesId);

    Result update(AdPlaySeries adPlaySeries);

    Result selectById(String seriesId);

    Result selectBatchIds(List<String> ids);

}
