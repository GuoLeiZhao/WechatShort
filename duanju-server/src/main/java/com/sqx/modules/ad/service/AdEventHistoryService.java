package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdEventHistory;

public interface AdEventHistoryService extends IService<AdEventHistory> {

    Result insert(AdEventHistory adEventHistory);

    AdEventHistory getAdEventHistoryOne(AdEventHistory adEventHistory);

}
