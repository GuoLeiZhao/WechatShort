package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdPlay;
import com.sqx.modules.ad.request.AdPlayQueryReq;

import java.util.List;

public interface AdPlayService extends IService<AdPlay> {

    IPage<AdPlay> page(AdPlayQueryReq req);

    List<AdPlay> dropList(AdPlayQueryReq req);

    Result insert(AdPlay adPlay);

    Result deleteById(String playId);

    Result update(AdPlay adPlay);

    Result selectById(String playId);

    Result selectBatchIds(List<String> ids);
}
