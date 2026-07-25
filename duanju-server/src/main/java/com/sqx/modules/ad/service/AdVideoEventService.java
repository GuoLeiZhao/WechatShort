package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdVideoEvent;
import com.sqx.modules.ad.request.AdVideoEventQueryReq;

public interface AdVideoEventService extends IService<AdVideoEvent> {

    IPage<AdVideoEvent> page(AdVideoEventQueryReq req);

    Result insert(AdVideoEvent adVideoEvent);

}
