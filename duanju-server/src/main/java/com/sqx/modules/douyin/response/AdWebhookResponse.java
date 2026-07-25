package com.sqx.modules.douyin.response;

import lombok.Data;
import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;
import java.util.List;

@Data
public class AdWebhookResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String logId;

    private String event;

    private String clientKey;

    private String fromUserId;

    @Data
    public static class ContentItem {
        private String id;

        @JSONField(name = "mp_id")
        private String mpId;

        private String cost;

        @JSONField(name = "open_id")
        private String openId;

        @JSONField(name = "event_time")
        private String eventTime;

        @JSONField(name = "req_id")
        private String reqId;

        @JSONField(name = "ad_type")
        private String adType;
    }

    private List<ContentItem> contentItems;
}