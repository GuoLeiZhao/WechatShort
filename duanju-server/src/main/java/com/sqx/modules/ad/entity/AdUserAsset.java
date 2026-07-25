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
@TableName("ad_user_asset")
public class AdUserAsset extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    @TableField("app_id")
    private String app_id;

    @TableField("open_id")
    private String open_id;

    @TableField("dev_user_account_id")
    private String dev_user_account_id;

    /**
     * 同步的资产信息类型 1：会员，2：代币，3：抵用券，4：已获得的短剧剧集信息
     */
    @TableField("asset_type")
    private Integer asset_type;

    @TableField("flow_direction")
    private Integer flow_direction;

    @TableField("flow_type")
    private String flow_type;

    @TableField("out_change_id")
    private String out_change_id;

    @TableField("flow_content")
    private String flow_content;

    @TableField("version")
    private String version;

    @TableField("idempotent_id")
    private String idempotent_id;

    @TableField("change_time")
    private String change_time;

    @TableField("clickid")
    private String clickid;

    @TableField("link_id")
    private String linkId;

    @TableField("tt_album_id")
    private String ttAlbumId;

    @TableField("tt_episode_id")
    private String ttEpisodeId;
}