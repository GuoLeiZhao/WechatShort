package com.sqx.modules.ad.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdStaticsHourReq implements Serializable {

    private static final long serialVersionUID = -7206661461991254629L;

    private String startDate;
    private String endDate;
    private List<String> advertiserIds;

}
