package com.sqx.modules.wechat.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class XPayPushMessageCommonResponse implements Serializable {
    private static final long serialVersionUID = -3486107666299817509L;

    private Integer Errcode = 0;
    private String Errmsg = "success";
}
