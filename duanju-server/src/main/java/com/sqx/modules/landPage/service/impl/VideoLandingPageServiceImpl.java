package com.sqx.modules.landPage.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.base.BasePagingRequest;
import com.sqx.common.utils.Query;
import com.sqx.modules.landPage.dao.VideoLandingPageDao;
import com.sqx.modules.landPage.entity.VideoLandingPage;
import com.sqx.modules.landPage.request.VideoLandingPageQueryReq;
import com.sqx.modules.landPage.request.VideoLandingPageShareQueryReq;
import com.sqx.modules.landPage.service.VideoLandingPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class VideoLandingPageServiceImpl extends ServiceImpl<VideoLandingPageDao, VideoLandingPage> implements VideoLandingPageService {
    @Override
    public IPage<VideoLandingPage> page(VideoLandingPageQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("sort", true)));
        return baseMapper.selectPage(
                new Query<VideoLandingPage>().getPage(req),
                new QueryWrapper<VideoLandingPage>().lambda()
                        .eq(null != req.getId() && 0L != req.getId(), VideoLandingPage::getId, req.getId())
                        .eq(StrUtil.isNotBlank(req.getName()), VideoLandingPage::getName, req.getName())
                        .like(StrUtil.isNotBlank(req.getNameLike()), VideoLandingPage::getName, req.getNameLike())
                        .eq(StrUtil.isNotBlank(req.getGroupName()), VideoLandingPage::getGroupName, req.getGroupName())
                        .eq(StrUtil.isNotBlank(req.getType()), VideoLandingPage::getType, req.getType())
                        .eq(VideoLandingPage::getDeleted, Boolean.FALSE)
        );
    }
}
