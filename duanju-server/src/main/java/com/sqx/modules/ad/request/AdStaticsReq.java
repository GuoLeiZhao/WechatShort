package com.sqx.modules.ad.request;

import com.sqx.modules.ad.enums.TimeGranularityType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdStaticsReq implements Serializable {

    private static final long serialVersionUID = -7206661461991254629L;

    private String startDate;
    private String endDate;
    private List<String> advertiserIds;
    private TimeGranularityType timeGranularity;

}
