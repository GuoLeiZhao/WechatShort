package com.sqx.modules.landPage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.landPage.entity.VideoLandingPage;
import com.sqx.modules.landPage.entity.VideoLandingPageShare;
import com.sqx.modules.landPage.request.VideoLandingPageQueryReq;
import com.sqx.modules.landPage.request.VideoLandingPageShareQueryReq;

public interface VideoLandingPageService  extends IService<VideoLandingPage> {
    IPage<VideoLandingPage> page(VideoLandingPageQueryReq req);
}
