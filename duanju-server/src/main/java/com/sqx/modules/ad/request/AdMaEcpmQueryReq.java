package com.sqx.modules.ad.request;

import com.sqx.common.base.BasePagingRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdMaEcpmQueryReq extends BasePagingRequest implements Serializable {

    private static final long serialVersionUID = -1022629913930790606L;

    private String mpId;
    private String openId;
    private Integer isHandle;
    private String linkId;
    private String createdStart;
    private String createdEnd;

}