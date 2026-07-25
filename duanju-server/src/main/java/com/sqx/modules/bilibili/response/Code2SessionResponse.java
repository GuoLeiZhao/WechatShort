package com.sqx.modules.bilibili.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class Code2SessionResponse implements Serializable {
    private static final long serialVersionUID = -6142692803930207199L;

    private String openid;
    private String unionid;
    private String errcode;
    private String errmsg;
    private String session_key;

}