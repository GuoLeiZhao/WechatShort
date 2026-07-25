package com.sqx.modules.place.service.manager;

import com.bytedance.ads.model.ReportAdvertiserGetV2Response;
import com.sqx.modules.place.request.oceanengine.CallbackRequest;
import com.sqx.modules.place.request.oceanengine.ReportRequest;
import com.sqx.modules.place.response.oceanengine.OEAdvertiserFundResponse;
import com.sqx.modules.place.response.oceanengine.OEAdvertiserReportResponse;
import com.sqx.modules.place.response.oceanengine.OECustomCenterAdListResponse;

import java.util.Collection;
import java.util.List;

public interface OceanEngineManager {

    String getToken();

    ReportAdvertiserGetV2Response reportAdvertiserGet();

    void appCallbackGet(CallbackRequest callbackRequest);

    Collection<OECustomCenterAdListResponse.Advertiser> getAdvertiserList(String advertiserId);

    List<OEAdvertiserReportResponse.Report> getAdvertiserDayReport(ReportRequest reportRequest, String token);

    // 批量查询账户余额
    List<OEAdvertiserFundResponse.BalanceData> getAdvertiserFund(List<Long> advertiserIds);
}
