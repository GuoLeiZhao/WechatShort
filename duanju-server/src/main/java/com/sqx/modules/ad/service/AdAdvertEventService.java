package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdAdvertEvent;
import com.sqx.modules.ad.request.AdAdvertEventQueryReq;

public interface AdAdvertEventService extends IService<AdAdvertEvent> {

    IPage<AdAdvertEvent> page(AdAdvertEventQueryReq req);

    Result insert(AdAdvertEvent adAdvertEvent);

}
