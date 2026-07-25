package com.sqx.modules.place.service;

import com.sqx.modules.place.entity.Context;

import java.util.Date;

public interface PlaceDataSyncService {

    void syncOrderListData(Context.Platform platform, Date startTime, Date endTime);

    void syncUserListData(Context.Platform platform, Date startTime, Date endTime);

    void syncLinkListData(Context.Platform platform, Date startTime, Date endTime);
}
