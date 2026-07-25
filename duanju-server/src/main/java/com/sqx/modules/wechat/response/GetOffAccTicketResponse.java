package com.sqx.modules.wechat.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetOffAccTicketResponse implements Serializable {
    private static final long serialVersionUID = 4876806154025403154L;

    private Integer errcode;
    private String errmsg;
    private String ticket;
    private Long expires_in;
}
