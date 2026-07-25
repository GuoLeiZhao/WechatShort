package com.sqx.modules.ad.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdPlayNotifyReq implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private String version;
    private String msg;

}
