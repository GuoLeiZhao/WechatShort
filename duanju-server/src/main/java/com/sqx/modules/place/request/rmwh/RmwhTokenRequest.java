package com.sqx.modules.place.request.rmwh;

import lombok.Data;

import java.io.Serializable;

@Data
public class RmwhTokenRequest implements Serializable {
    private static final long serialVersionUID = 4704262780247642493L;

    /** 代理商唯一标识 */
    private long agentId = 53059554377729L;
    /** 密码 */
    private String secret = "__RMWH_SECRET__";
}
