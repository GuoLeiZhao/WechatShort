package com.sqx.modules.douyin.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class Code2SessionResponse implements Serializable {
    private static final long serialVersionUID = -2713496345358981427L;

    private String sessionKey;
    private String openid;
    private String anonymousOpenid;
    private String anonymous_openid;
    private String unionid;
}
