package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdCustomer;
import com.sqx.modules.ad.entity.AdLink;
import com.sqx.modules.ad.request.AdLinkListReq;
import com.sqx.modules.ad.request.AdLinkQueryReq;
import com.sqx.modules.ad.response.AdLinkQueryRes;
import com.sqx.modules.douyin.request.AdWebhookRequest;

import java.util.List;

public interface AdLinkService extends IService<AdLink> {

    Result insert(AdLink adLink);

    List<AdLink> selectList(AdLinkListReq adLinkListReq);

    IPage<AdLinkQueryRes> page(AdLinkQueryReq req);

    Result update(AdLink adLink);

    void adWebhook(AdWebhookRequest adWebhookRequest);

    AdLink selectOneByLinkId(String linkId);

    List<AdCustomer> getAllAdCustomer();

    AdLink selectOneByAdvertiserId(String advertiserId);
}
