package com.sqx.modules.ad.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Map;

/**
 * 事件上报请求
 */
@Data
@ApiModel("事件上报请求")
public class EventTrackRequest {

    /**
     * 事件ID，最大128字符
     */
    @ApiModelProperty(value = "事件ID", required = true, example = "ViewProductDetails")
    @NotBlank(message = "事件ID不能为空")
    @Size(max = 128, message = "事件ID长度不能超过128个字符")
    private String eventId;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "user_001")
    @Size(max = 64, message = "用户ID长度不能超过64个字符")
    private String userId;

    /**
     * 会话ID
     */
    @ApiModelProperty(value = "会话ID", example = "session_001")
    @Size(max = 255, message = "会话ID长度不能超过255个字符")
    private String sessionId;

    /**
     * 设备ID
     */
    @ApiModelProperty(value = "设备ID", example = "device_001")
    @Size(max = 64, message = "设备ID长度不能超过64个字符")
    private String deviceId;

    /**
     * 平台类型：ios/android/web/miniprogram
     */
    @ApiModelProperty(value = "平台类型", example = "miniprogram")
    @Size(max = 20, message = "平台类型长度不能超过20个字符")
    private String platform;

    /**
     * 应用版本
     */
    @ApiModelProperty(value = "应用版本", example = "1.0.0")
    @Size(max = 20, message = "应用版本长度不能超过20个字符")
    private String appVersion;

    /**
     * 渠道来源
     */
    @ApiModelProperty(value = "渠道来源", example = "wechat")
    @Size(max = 50, message = "渠道来源长度不能超过50个字符")
    private String channel;

    /**
     * IP地址
     */
    @ApiModelProperty(value = "IP地址", example = "192.168.1.1")
    @Size(max = 45, message = "IP地址长度不能超过45个字符")
    private String ipAddress;

    /**
     * 用户代理
     */
    @ApiModelProperty(value = "用户代理", example = "Mozilla/5.0...")
    @Size(max = 500, message = "用户代理长度不能超过500个字符")
    private String userAgent;

    /**
     * 事件发生时间
     */
    @ApiModelProperty(value = "事件发生时间", example = "2024-01-15 10:30:00")
    private Date eventTime;

    /**
     * 事件属性
     * 字符型属性值需要加引号，数值型属性值不加引号
     * 例如：{"Category":"家电","PayAmount":6999}
     */
    @ApiModelProperty(value = "事件属性", example = "{\"Category\":\"家电\",\"PayAmount\":6999}")
    private Map<String, Object> properties;
}
