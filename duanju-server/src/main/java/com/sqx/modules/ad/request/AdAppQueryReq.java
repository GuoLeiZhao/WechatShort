package com.sqx.modules.ad.request;

import com.sqx.common.base.BasePagingRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdAppQueryReq extends BasePagingRequest implements Serializable {

    private static final long serialVersionUID = -8819329884616924955L;

    private String appId;
    private String appName;
    private Date createdStart;
    private Date createdEnd;

}