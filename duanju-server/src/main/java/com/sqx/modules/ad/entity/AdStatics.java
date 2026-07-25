package com.sqx.modules.ad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import com.sqx.modules.ad.enums.TimeGranularityType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_statics")
public class AdStatics extends BaseEntity {

    private static final long serialVersionUID = 4675257056483519185L;

    @TableId(type = IdType.INPUT)
    private Long id;

    // 时间粒度
    @TableField("time_granularity")
    private TimeGranularityType timeGranularity;

    // 记录时间 YYYY-MM-DD HH
    @TableField("stat_datetime")
    private Date statDatetime;

    @TableField("advertiser_id")
    private Long advertiserId;

    // 广告账户名称
    @TableField("advertiser_name")
    private String advertiserName;

    // 链接id
    @TableField("link_id")
    private String linkId;

    // 账户余额
    private Float balance;

    // 消耗
    private Float payment;

    // 回收
    private Float cost;

    // 首日roi
    private Float roi;

    // 展示数
    @TableField("ad_show")
    private Long adShow;

    // 千展成本
    @TableField("cost_per_thousand_show")
    private Float costPerThousandShow;

    // 点击数
    @TableField("click")
    private Long click;

    // 点击率
    @TableField("click_rate")
    private Float clickRate;

    // 平均点击成本
    @TableField("cost_per_click")
    private Float costPerClick;

    // 跳转数
    @TableField("jump_count")
    private Long jumpCount;

    // 跳转率
    @TableField("jump_rate")
    private Float jumpRate;

    // 拉起数
    @TableField("launch_count")
    private Long launchCount;

    // 拉起率
    @TableField("launch_rate")
    private Float launchRate;

    // 拉起成本
    @TableField("launch_cost")
    private Float launchCost;

    // arpu
    private Float arpu;

    // ipu
    private Float ipu;

    // 平均ecpm
    @TableField("avg_ecpm")
    private Float avgEcpm;

    /**
     * 总消耗
     */
    @TableField(exist = false)
    private Float sumPayment;

    /**
     * 总回收
     */
    @TableField(exist = false)
    private Float sumCost;

    @TableField(exist = false)
    private String hourTime;

}