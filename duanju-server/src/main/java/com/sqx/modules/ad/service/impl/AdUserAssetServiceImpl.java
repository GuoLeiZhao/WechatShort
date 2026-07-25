package com.sqx.modules.ad.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.dao.AdUserAssetDao;
import com.sqx.modules.ad.entity.AdUserAsset;
import com.sqx.modules.ad.service.AdUserAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdUserAssetServiceImpl extends ServiceImpl<AdUserAssetDao, AdUserAsset> implements AdUserAssetService {

    @Override
    public Result insert(AdUserAsset adUserAsset) {
        int rows = baseMapper.insert(adUserAsset);
        if (rows > 0) {
            return Result.success("添加成功！");
        } else {
            return Result.error("添加失败");
        }
    }

}
