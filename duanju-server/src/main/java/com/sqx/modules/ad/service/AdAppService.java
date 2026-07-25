package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdApp;
import com.sqx.modules.ad.request.AdAppQueryReq;

import java.util.List;

public interface AdAppService extends IService<AdApp> {

    IPage<AdApp> page(AdAppQueryReq req);

    List<AdApp> dropList(AdAppQueryReq req);

    Result insert(AdApp adApp);

    Result deleteById(String appId);

    Result update(AdApp adApp);

    Result selectById(String appId);

    Result selectBatchIds(List<String> ids);

}
