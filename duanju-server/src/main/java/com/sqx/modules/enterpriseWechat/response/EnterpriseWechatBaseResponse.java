package com.sqx.modules.enterpriseWechat.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class EnterpriseWechatBaseResponse implements Serializable {
    private static final long serialVersionUID = -6927629076126886114L;

    private Integer errcode;
    private String errmsg;

}
