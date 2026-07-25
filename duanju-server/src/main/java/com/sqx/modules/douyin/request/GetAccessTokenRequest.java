package com.sqx.modules.douyin.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetAccessTokenRequest implements Serializable {
    private static final long serialVersionUID = 5642762707643941242L;

    private String appid;
    private String secret;
    private String grantType = "client_credential";

    public GetAccessTokenRequest(String appid, String secret) {
        this.appid = appid;
        this.secret = secret;
    }
}
