package com.sqx.modules.ad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import com.sqx.modules.ad.enums.AdShowType;
import com.sqx.modules.ad.enums.ReturnCycle;
import com.sqx.modules.ad.enums.ReturnSetting;
import com.sqx.modules.ad.enums.ReturnType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_link")
public class AdLink extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @TableField("link_id")
    private String linkId;

    @TableField("user_id")
    private String userId;

    @TableField("link_name")
    private String linkName;

    @TableField("advertiser_id")
    private Long advertiserId;

    @TableField("album_id")
    private String albumId;

    @TableField("episode_id")
    private String episodeId;

    @TableField("app_id")
    private String appId;

    @TableField("landing_channel")
    private String landingChannel;

    @TableField("promotion_channel")
    private String promotionChannel;

    @TableField("return_name")
    private String returnName;

    @TableField("return_type")
    private ReturnType returnType;

    @TableField("return_cycle")
    private ReturnCycle returnCycle;

    @TableField("return_cycle_hours")
    private Integer returnCycleHours;

    @TableField("return_setting")
    private ReturnSetting returnSetting;

    @TableField("ipu")
    private BigDecimal ipu;

    @TableField("ecpm_arpu")
    private BigDecimal ecpmArpu;

    private BigDecimal rate;

    @TableField("ad_show_type")
    private AdShowType adShowType;

}
