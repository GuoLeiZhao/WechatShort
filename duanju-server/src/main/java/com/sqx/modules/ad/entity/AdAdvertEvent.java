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
@TableName("ad_advert_event")
public class AdAdvertEvent extends BaseEntity {
    private static final long serialVersionUID = -4741609737268697470L;

    @TableId(type = IdType.AUTO)
    @TableField("id")
    private String id;

    @TableField("app_id")
    private String appId;

    @TableField("open_id")
    private String openId;

    /**
     * 巨量广告id
     * */
    @TableField("click_id")
    private String clickId;

    /**
     * 平台链接id
     * */
    @TableField("link_id")
    private String linkId;

    @TableField("album_id")
    private String albumId;

    @TableField("episode_id")
    private String episodeId;

    @TableField("path")
    private String path;

    @TableField("seq")
    private Integer seq;

    @TableField("event_type")
    private EventType eventType;

    @TableField("advert_type")
    private AdvertType advertType;

    @Getter
    @AllArgsConstructor
    public enum EventType {
        ON_SHOW_SUCCESS("播放广告成功"),
        ON_SHOW_FAIL("播放广告失败"),
        ON_CLOSE_ENDED("广告关闭且看完"),
        ON_CLOSE_NOT_ENDED("广告关闭未看完"),
        ON_ERROR("广告播放失败"),
        ;

        private final String desc;
    }

    @Getter
    @AllArgsConstructor
    public enum AdvertType {
        INCENTIVE_VIDEO("激励视频广告"),
        ;

        private final String desc;
    }

    @TableField("remark")
    private String remark;

}
