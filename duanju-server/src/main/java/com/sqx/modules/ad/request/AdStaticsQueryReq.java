package com.sqx.modules.ad.request;

import com.sqx.common.base.BasePagingRequest;
import com.sqx.modules.ad.enums.TimeGranularityType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdStaticsQueryReq extends BasePagingRequest implements Serializable {

    private static final long serialVersionUID = -6645332700496041529L;

    private String startDate;
    private String endDate;
    private List<String> advertiserIds;
    private TimeGranularityType timeGranularity;

}
