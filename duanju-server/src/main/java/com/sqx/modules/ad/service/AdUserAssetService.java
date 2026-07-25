package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdUserAsset;

public interface AdUserAssetService extends IService<AdUserAsset> {

    Result insert(AdUserAsset adUserAsset);
}
