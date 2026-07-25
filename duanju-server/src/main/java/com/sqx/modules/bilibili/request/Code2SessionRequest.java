package com.sqx.modules.bilibili.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class Code2SessionRequest implements Serializable {
    private static final long serialVersionUID = 3961187999275049157L;

    private String appid;
    private String secret;
    private String js_code;
    private String grant_type;
}
