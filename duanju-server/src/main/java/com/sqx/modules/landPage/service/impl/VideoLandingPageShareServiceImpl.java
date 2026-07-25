package com.sqx.modules.landPage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.base.BasePagingRequest;
import com.sqx.common.utils.Query;
import com.sqx.modules.landPage.dao.VideoLandingPageDao;
import com.sqx.modules.landPage.dao.VideoLandingPageShareDao;
import com.sqx.modules.landPage.entity.VideoLandingPage;
import com.sqx.modules.landPage.entity.VideoLandingPageShare;
import com.sqx.modules.landPage.request.VideoLandingPageShareQueryReq;
import com.sqx.modules.landPage.service.VideoLandingPageService;
import com.sqx.modules.landPage.service.VideoLandingPageShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class VideoLandingPageShareServiceImpl extends ServiceImpl<VideoLandingPageShareDao, VideoLandingPageShare> implements VideoLandingPageShareService {
    @Override
    public IPage<VideoLandingPageShare> page(VideoLandingPageShareQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("id", false)));
        return baseMapper.selectPage(
                new Query<VideoLandingPageShare>().getPage(req),
                new QueryWrapper<VideoLandingPageShare>().lambda()
                        .eq(null != req.getId() && 0L != req.getId(), VideoLandingPageShare::getId, req.getId())
                        .eq(StrUtil.isNotBlank(req.getName()), VideoLandingPageShare::getName, req.getName())
                        .like(StrUtil.isNotBlank(req.getNameLike()), VideoLandingPageShare::getName, req.getNameLike())
                        .eq(StrUtil.isNotBlank(req.getGroupName()), VideoLandingPageShare::getGroupName, req.getGroupName())
                        .eq(VideoLandingPageShare::getDeleted, Boolean.FALSE)
        );
    }
}
