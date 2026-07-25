package com.sqx.modules.wechat.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class WechatOfficeAccountSignResponse implements Serializable {

    private static final long serialVersionUID = 2794867617232506233L;

    private String appId;
    private String url;
    private String nonceStr;
    private String timestamp;
    private String signature;

}
