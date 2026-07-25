package com.sqx.modules.ad.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AdMaEcpmListReq implements Serializable {

    private static final long serialVersionUID = -8819329884616924955L;

    private String linkId;
    private String openId;
    private Integer isHandle;
    private Date createdStart;
    private Date createdEnd;

}