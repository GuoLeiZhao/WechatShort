package com.sqx.modules.douyin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdWebhookRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("log_id")
    private String logId;

    private String event;

    @JsonProperty("client_key")
    private String clientKey;

    @JsonProperty("from_user_id")
    private String fromUserId;

    private Object content;

}