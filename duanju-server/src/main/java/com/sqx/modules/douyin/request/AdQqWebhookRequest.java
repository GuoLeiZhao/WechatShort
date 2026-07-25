package com.sqx.modules.douyin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdQqWebhookRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("event_data")
    private Object eventData;

}