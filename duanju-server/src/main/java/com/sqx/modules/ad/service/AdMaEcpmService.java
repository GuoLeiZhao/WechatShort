package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdMaEcpm;
import com.sqx.modules.ad.request.AdMaEcpmListReq;
import com.sqx.modules.ad.request.AdMaEcpmQueryReq;

import java.util.List;

public interface AdMaEcpmService extends IService<AdMaEcpm> {

    IPage<AdMaEcpm> page(AdMaEcpmQueryReq req);

    Result download(AdMaEcpmQueryReq req);

    Result insert(AdMaEcpm adMaEcpm);

    Result update(AdMaEcpm adMaEcpm);

    List<AdMaEcpm> selectList(AdMaEcpmListReq req);

}
