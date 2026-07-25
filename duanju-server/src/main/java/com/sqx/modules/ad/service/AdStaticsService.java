package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.ad.entity.AdStatics;
import com.sqx.modules.ad.request.AdStaticsHourReq;
import com.sqx.modules.ad.request.AdStaticsQueryReq;
import com.sqx.modules.ad.request.AdStaticsReq;
import com.sqx.modules.ad.response.AdStaticsRes;

import java.util.List;

public interface AdStaticsService extends IService<AdStatics> {
    IPage<AdStatics> page(AdStaticsQueryReq req);

    AdStaticsRes getInfo(AdStaticsReq req);

    List<AdStatics> getHourData(AdStaticsHourReq req);
}
