package com.sqx.modules.ad.request;

import com.sqx.common.base.BasePagingRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdPlaySeriesQueryReq extends BasePagingRequest implements Serializable {

    private static final long serialVersionUID = -8819329884616924955L;

    private String seriesId;
    private String seriesTitle;
    private Integer seriesNum;
    private String playId;
    private String playName;
    private Date createdStart;
    private Date createdEnd;

}