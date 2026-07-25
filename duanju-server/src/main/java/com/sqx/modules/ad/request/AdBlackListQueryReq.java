package com.sqx.modules.ad.request;

import com.sqx.common.base.BasePagingRequest;
import com.sqx.modules.ad.entity.AdBlackList;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdBlackListQueryReq extends BasePagingRequest implements Serializable {

    private static final long serialVersionUID = 5554822961070990450L;

    private AdBlackList.BlackKey blackKey;
    private String blackValue;
    private String appId;

}