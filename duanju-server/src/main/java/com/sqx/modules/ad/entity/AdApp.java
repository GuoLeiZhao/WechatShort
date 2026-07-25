package com.sqx.modules.ad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_app")
public class AdApp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @TableField("app_id")
    private String appId;

    @TableField("app_type")
    private AppType appType;

    @TableField("app_name")
    private String appName;

    @Getter
    @AllArgsConstructor
    public enum AppType {
        WECHAT("微信小程序"),
        DOUYIN("抖音小程序"),
        KUAISHOU("快手小程序"),
        BILIBILI("B站小程序"),
        ;

        private final String desc;
    }

    @TableField("is_open_link")
    private Boolean isOpenLink;

    @TableField("link_id")
    private String linkId;

    @TableField("start_time")
    private Integer startTime;

    @TableField("next_time")
    private Integer nextTime;

    @TableField("ad_unit_ids")
    private String adUnitIds;

    @TableField("activity_id")
    private String activityId;

    @TableField("im_id")
    private String imId;

    @TableField("disable_screen")
    private String disableScreen;
}
