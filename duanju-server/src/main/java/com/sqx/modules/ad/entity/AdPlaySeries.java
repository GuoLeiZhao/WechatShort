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
@TableName("ad_play_series")
public class AdPlaySeries extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @TableField("series_id")
    private String seriesId;

    @TableField("play_id")
    private String playId;

    @TableField("series_title")
    private String seriesTitle;

    @TableField("series_num")
    private Integer seriesNum;

    @TableField("series_image")
    private String seriesImage;

    @TableField("series_path")
    private String seriesPath;

    @TableField("series_desc")
    private String seriesDesc;

    @TableField("douyin_status")
    private String douyinStatus;

    @TableField("remark")
    private String remark;

}
