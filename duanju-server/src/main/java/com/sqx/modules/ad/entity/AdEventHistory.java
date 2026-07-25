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
@TableName("ad_event_history")
public class AdEventHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @TableField("id")
    private String id;

    @TableField("adid")
    private String adid;

    @TableField("creativeid")
    private String creativeid;

    @TableField("creativetype")
    private String creativetype;

    @TableField("clickid")
    private String clickid;

    @TableField("event_type")
    private String eventType;

    @TableField("app_id")
    private String appId;

    @TableField("open_id")
    private String openId;

    @TableField("link_id")
    private String linkId;

    @TableField("album_id")
    private String albumId;

    @TableField("episode_id")
    private String episodeId;

}
