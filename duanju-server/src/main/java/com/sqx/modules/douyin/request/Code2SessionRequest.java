package com.sqx.modules.douyin.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class Code2SessionRequest implements Serializable {
    private static final long serialVersionUID = 7630417817345409972L;

    private String appid;
    private String secret;
    private String code;
    private String anonymousCode;
    private String ttOptions;
    private String ttClickId;
    private String ttLinkId;
}
