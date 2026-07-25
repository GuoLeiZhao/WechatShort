package com.sqx.modules.douyin.request.thirdparty;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ComponentTicketReq implements Serializable {

    private static final long serialVersionUID = 7874493421203713458L;

    @JsonProperty("Nonce")
    private String nonce;

    @JsonProperty("TimeStamp")
    private String timestamp;

    @JsonProperty("Encrypt")
    private String encrypt;

    @JsonProperty("MsgSignature")
    private String msgSignature;

}
