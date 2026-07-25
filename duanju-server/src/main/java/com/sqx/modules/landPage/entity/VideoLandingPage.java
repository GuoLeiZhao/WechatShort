package com.sqx.modules.landPage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("video_landing_page")
@NoArgsConstructor
public class VideoLandingPage implements Serializable {
    private static final long serialVersionUID = -276591093045771920L;

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("app_id")
    private String appId;
    private String name;
    @TableField("group_name")
    private String groupName;
    /* public（公包） / private（私包） / short_url（短链） */
    private String type;
    private String pic;
    private String description;
    private String url;
    private Double score;
    private Long sort;
    private String remark;
    private Boolean deleted;
    @TableField("created_at")
    private Date createdAt;
    @TableField("modified_at")
    private Date modifiedAt;

}

