package com.sqx.modules.ad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_play")
public class AdPlay extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @TableField("play_id")
    private String playId;

    @TableField("play_name")
    private String playName;

    @TableField("play_image")
    private String playImage;

    @TableField("play_desc")
    private String playDesc;

    @TableField("play_path")
    private String playPath;

    @TableField("douyin_status")
    private String douyinStatus;

    @TableField("wechat_status")
    private String wechatStatus;

    @TableField("kuaishou_status")
    private String kuaishouStatus;

    @TableField("play_rating")
    private Integer playRating;

    @TableField("remark")
    private String remark;

}
