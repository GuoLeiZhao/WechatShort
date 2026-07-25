package com.sqx.modules.ad.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.dao.AdAdvertEventDao;
import com.sqx.modules.ad.entity.AdAdvertEvent;
import com.sqx.modules.ad.request.AdAdvertEventQueryReq;
import com.sqx.modules.ad.service.AdAdvertEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AdAdvertEventServiceImpl extends ServiceImpl<AdAdvertEventDao, AdAdvertEvent> implements AdAdvertEventService {

    @Override
    public IPage<AdAdvertEvent> page(AdAdvertEventQueryReq req) {
        return baseMapper.selectPage(
                new Query<AdAdvertEvent>().getPage(req),
                new QueryWrapper<AdAdvertEvent>().lambda()
                        .eq(null != req.getEventType(), AdAdvertEvent::getEventType, req.getEventType())
                        .eq(null != req.getAdvertType(), AdAdvertEvent::getAdvertType, req.getAdvertType())
                        .eq(StrUtil.isNotBlank(req.getAppId()), AdAdvertEvent::getAppId, req.getAppId())
                        .eq(StrUtil.isNotBlank(req.getOpenId()), AdAdvertEvent::getOpenId, req.getOpenId())
                        .eq(StrUtil.isNotBlank(req.getClickId()), AdAdvertEvent::getClickId, req.getClickId())
                        .eq(StrUtil.isNotBlank(req.getLinkId()), AdAdvertEvent::getLinkId, req.getLinkId())
                        .eq(StrUtil.isNotBlank(req.getAlbumId()), AdAdvertEvent::getAlbumId, req.getAlbumId())
                        .eq(StrUtil.isNotBlank(req.getEpisodeId()), AdAdvertEvent::getEpisodeId, req.getEpisodeId())
                        .like(StrUtil.isNotBlank(req.getPath()), AdAdvertEvent::getPath, req.getPath())
        );
    }

    @Override
    public Result insert(AdAdvertEvent adAdvertEvent) {
        int rows = baseMapper.insert(adAdvertEvent);
        if (rows > 0) {
            return Result.success("添加成功！");
        } else {
            return Result.error("添加失败");
        }
    }

}
