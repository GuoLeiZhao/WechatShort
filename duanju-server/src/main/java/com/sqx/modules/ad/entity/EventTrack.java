package com.sqx.modules.ad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 事件主表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("事件主表")
@TableName("event_track")
public class EventTrack extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 事件记录ID
     */
    @ApiModelProperty("事件记录ID")
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    /**
     * 事件ID，最大128字符
     */
    @ApiModelProperty("事件ID")
    @TableField("event_id")
    private String eventId;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    @TableField("user_id")
    private String userId;

    /**
     * 会话ID
     */
    @ApiModelProperty("会话ID")
    @TableField("session_id")
    private String sessionId;

    /**
     * 设备ID
     */
    @ApiModelProperty("设备ID")
    @TableField("device_id")
    private String deviceId;

    /**
     * 平台类型：ios/android/web/miniprogram
     */
    @ApiModelProperty("平台类型")
    private String platform;

    /**
     * 应用版本
     */
    @ApiModelProperty("应用版本")
    @TableField("app_version")
    private String appVersion;

    /**
     * 渠道来源
     */
    @ApiModelProperty("渠道来源")
    private String channel;

    /**
     * IP地址
     */
    @ApiModelProperty("IP地址")
    @TableField("ip_address")
    private String ipAddress;

    /**
     * 用户代理
     */
    @ApiModelProperty("用户代理")
    @TableField("user_agent")
    private String userAgent;

    /**
     * 事件发生时间
     */
    @ApiModelProperty("事件发生时间")
    @TableField("event_time")
    private Date eventTime;

    /**
     * 记录创建时间
     */
    @ApiModelProperty("记录创建时间")
    @TableField("created_at")
    private Date createdAt;

    /**
     * 记录更新时间
     */
    @ApiModelProperty("记录更新时间")
    @TableField("updated_at")
    private Date updatedAt;
}
