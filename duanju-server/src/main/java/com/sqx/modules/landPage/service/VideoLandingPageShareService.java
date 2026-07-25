package com.sqx.modules.landPage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.landPage.entity.VideoLandingPageShare;
import com.sqx.modules.landPage.request.VideoLandingPageShareQueryReq;

public interface VideoLandingPageShareService extends IService<VideoLandingPageShare> {
    IPage<VideoLandingPageShare> page(VideoLandingPageShareQueryReq req);

}
