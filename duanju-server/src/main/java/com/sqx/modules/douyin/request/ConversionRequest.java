package com.sqx.modules.douyin.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 转化回传 API 对接说明
 * https://event-manager.oceanengine.com/docs/8650/app_api_docs
 */
@Data
public class ConversionRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    // 事件类型，必填，如 "active" 或 "active_pay" "game_addiction"
    private String event_type;

    // 事件权重，可选，单位人民币，默认100
    private Double event_weight;

    // 上下文信息，必填
    private Context context;

    // 附加属性，可选，示例 {"pay_amount": 120}
    private Map<String, Object> properties;

    // 事件发生时间戳，必填，毫秒级
    private long timestamp;

    // 外部事件ID，可选，用于去重
    private String outer_event_id;

    @Data
    public static class Context implements Serializable {
        private static final long serialVersionUID = 1L;

        // 广告信息，必填
        private Ad ad;

        // 设备信息，可选
        private Device device;
    }

    @Data
    public static class Ad implements Serializable {
        private static final long serialVersionUID = 1L;

        // 回调参数，必填，从监测链接或URL中获取
        private String callback;

        // 匹配类型，可选，0=点击归因（默认），1=展示归因，2=有效播放归因
        private Integer match_type;
    }

    @Data
    public static class Device implements Serializable {
        private static final long serialVersionUID = 1L;

        // 设备平台，可选，如 "android" 或 "ios"，小写
        private String platform;

        // Android IMEI的MD5值，可选
        private String imei;

        // iOS IDFA原值，可选
        private String idfa;

        // Android OAID原值，可选
        private String oaid;
    }
}