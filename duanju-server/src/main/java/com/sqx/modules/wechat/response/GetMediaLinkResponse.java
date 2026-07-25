package com.sqx.modules.wechat.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class GetMediaLinkResponse implements Serializable {

    private static final long serialVersionUID = 1769227185230508334L;

    // 错误码
    private String errcode;
    // 错误信息
    private String errmsg;
    // 媒体播放信息
    private MediaPlaybackInfo mediaInfo;

    @Data
    public static class MediaPlaybackInfo implements Serializable {
        private static final long serialVersionUID = 1769227185230508334L;

        /**
         * 媒资文件ID，唯一标识媒资文件。
         */
        private long mediaId;

        /**
         * 播放时长，单位为秒。
         */
        private int duration;

        /**
         * 媒资文件名。
         */
        private String name;

        /**
         * 媒资描述。
         */
        private String description;

        /**
         * 封面图临时链接，用于获取媒资文件的封面图片。
         */
        private String coverUrl;

        /**
         * MP4格式临时链接，用于下载或播放媒资的MP4格式文件。
         */
        private String mp4Url;

        /**
         * HLS格式临时链接，用于播放媒资的HLS格式流媒体文件。
         */
        private String hlsUrl;

    }

}
