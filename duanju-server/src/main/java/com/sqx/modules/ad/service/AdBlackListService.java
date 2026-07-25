package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdBlackList;
import com.sqx.modules.ad.request.AdBlackListQueryReq;
import com.sqx.modules.ad.response.AdBlackQueryRes;

public interface AdBlackListService extends IService<AdBlackList> {

    Result insert(AdBlackList adBlackList);

    IPage<AdBlackQueryRes> page(AdBlackListQueryReq req);

    Result selectOne(AdBlackList adBlackList);

    Result deleteById(Long id);
}
