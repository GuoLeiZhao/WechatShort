package com.sqx.modules.ad.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sqx.common.base.BasePagingRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdLinkQueryReq extends BasePagingRequest implements Serializable {

    private static final long serialVersionUID = -8819329884616924955L;

    private String userId;
    private String linkId;
    private String linkName;
    private String advertiserID;
    private String createdStart;
    private String createdEnd;

}