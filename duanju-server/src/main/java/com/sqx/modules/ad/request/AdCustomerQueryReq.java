package com.sqx.modules.ad.request;

import com.sqx.common.base.BasePagingRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdCustomerQueryReq extends BasePagingRequest implements Serializable {

    private static final long serialVersionUID = -8819329884616924955L;

    private Long advertiserId;
    private String advertiserName;
    private String advertiserType;
    private Date createdStart;
    private Date createdEnd;

}