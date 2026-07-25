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
@TableName("ad_video_event")
public class AdVideoEvent extends BaseEntity {
    private static final long serialVersionUID = -2554354989335347643L;

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

    @TableField("seq")
    private Integer seq;

    @TableField("path")
    private String path;

    @TableField("event_type")
    private EventType eventType;

    @Getter
    @AllArgsConstructor
    public enum EventType {
        ON_PLAY("播放"),
        ON_PAUSE("暂停"),
        ON_ENDED("本集播放完成"),
        ON_ERROR("播放错误"),
        ON_RATE_CHANGE("播放倍速改变"),
        ON_OPEN_CATALOG("打开剧集列表"),
        ON_CHANGE_EPISODE("切换剧集"),
        CTRL_LIKE("喜欢"),
        CTRL_UNLIKE("不喜欢"),
        CTRL_SHARE("分享"),
        CTRL_SUBSCRIBE("追剧"),
        CTRL_UNSUBSCRIBE("不追剧"),
        CTRL_OPENRATEPANEL("打开倍速面板"),
        ;

        private final String desc;
    }

    @TableField("remark")
    private String remark;

}
