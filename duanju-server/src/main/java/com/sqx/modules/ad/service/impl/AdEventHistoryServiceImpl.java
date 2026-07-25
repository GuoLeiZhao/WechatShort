package com.sqx.modules.ad.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.dao.AdEventHistoryDao;
import com.sqx.modules.ad.entity.AdEventHistory;
import com.sqx.modules.ad.service.AdEventHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdEventHistoryServiceImpl extends ServiceImpl<AdEventHistoryDao, AdEventHistory> implements AdEventHistoryService {

    @Override
    public Result insert(AdEventHistory adEventHistory) {
        int rows = baseMapper.insert(adEventHistory);
        if (rows > 0) {
            return Result.success("添加成功！");
        } else {
            return Result.error("添加失败");
        }
    }

    @Override
    public AdEventHistory getAdEventHistoryOne(AdEventHistory req) {
        AdEventHistory adEventHistory = baseMapper.selectOne(new QueryWrapper<AdEventHistory>().lambda()
                .eq(StrUtil.isNotBlank(req.getOpenId()), AdEventHistory::getOpenId, req.getOpenId())
                .eq(StrUtil.isNotBlank(req.getEventType()), AdEventHistory::getEventType, req.getEventType())
                .eq(StrUtil.isNotBlank(req.getAppId()), AdEventHistory::getAppId, req.getAppId())
                .orderByDesc(AdEventHistory::getCreatedAt)
                .last("limit 1")
        );
        return adEventHistory;
    }

}
