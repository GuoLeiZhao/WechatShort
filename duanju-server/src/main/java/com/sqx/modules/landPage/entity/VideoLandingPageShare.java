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
@TableName("video_landing_page_share")
@NoArgsConstructor
public class VideoLandingPageShare implements Serializable {
    private static final long serialVersionUID = 2493524081639779162L;

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("app_id")
    private String appId;
    private String name;
    private String type;
    @TableField("group_name")
    private String groupName;
    private String pic;
    private String url;
    private String remark;
    private Boolean deleted;
    @TableField("created_at")
    private Date createdAt;
    @TableField("modified_at")
    private Date modifiedAt;
}
