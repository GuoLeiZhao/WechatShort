package com.sqx.modules.ad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_ma_ecpm")
public class AdMaEcpm extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @TableField("id")
    private String id;

    @TableField("app_name")
    private String appName;

    @TableField("log_id")
    private String logId;

    @TableField("event")
    private String event;

    @TableField("client_key")
    private String clientKey;

    @TableField("from_user_id")
    private String fromUserId;

    @TableField("content_id")
    private String contentId;

    @TableField("mp_id")
    private String mpId;

    @TableField("cost")
    private String cost;

    @TableField("open_id")
    private String openId;

    @TableField("event_time")
    private Date eventTime;

    @TableField("req_id")
    private String reqId;

    @TableField("ad_type")
    private String adType;

    @TableField("is_handle")
    private Integer isHandle;

    @TableField("link_id")
    private String linkId;

}
