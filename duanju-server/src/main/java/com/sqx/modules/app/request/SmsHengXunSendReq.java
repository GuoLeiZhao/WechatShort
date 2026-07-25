package com.sqx.modules.app.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SmsHengXunSendReq implements Serializable {

    private static final long serialVersionUID = 401139194003488194L;

    public String userName;
    public String content;
    public List<String> phoneList;
    public String timestamp;
    public String sign;
}
