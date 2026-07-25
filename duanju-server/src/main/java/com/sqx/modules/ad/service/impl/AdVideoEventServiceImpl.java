package com.sqx.modules.ad.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.dao.AdVideoEventDao;
import com.sqx.modules.ad.entity.AdVideoEvent;
import com.sqx.modules.ad.request.AdVideoEventQueryReq;
import com.sqx.modules.ad.service.AdVideoEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdVideoEventServiceImpl extends ServiceImpl<AdVideoEventDao, AdVideoEvent> implements AdVideoEventService {

    @Override
    public IPage<AdVideoEvent> page(AdVideoEventQueryReq req) {
        return baseMapper.selectPage(
                new Query<AdVideoEvent>().getPage(req),
                new QueryWrapper<AdVideoEvent>().lambda()
                        .eq(null != req.getEventType(), AdVideoEvent::getEventType, req.getEventType())
                        .eq(StrUtil.isNotBlank(req.getAppId()), AdVideoEvent::getAppId, req.getAppId())
                        .eq(StrUtil.isNotBlank(req.getOpenId()), AdVideoEvent::getOpenId, req.getOpenId())
                        .eq(StrUtil.isNotBlank(req.getClickId()), AdVideoEvent::getClickId, req.getClickId())
                        .eq(StrUtil.isNotBlank(req.getLinkId()), AdVideoEvent::getLinkId, req.getLinkId())
                        .eq(StrUtil.isNotBlank(req.getAlbumId()), AdVideoEvent::getAlbumId, req.getAlbumId())
                        .eq(StrUtil.isNotBlank(req.getEpisodeId()), AdVideoEvent::getEpisodeId, req.getEpisodeId())
        );
    }

    @Override
    public Result insert(AdVideoEvent adVideoEvent) {
        int rows = baseMapper.insert(adVideoEvent);
        if (rows > 0) {
            return Result.success("添加成功！");
        } else {
            return Result.error("添加失败");
        }
    }

}
