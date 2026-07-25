package com.sqx.modules.wechat.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 媒资信息响应类
 */
@Data
public class ListMediaResponse implements Serializable {

    private static final long serialVersionUID = 2481706894346006199L;

    /**
     * 错误码
     */
    private int errcode;

    /**
     * 错误原因描述
     */
    private String errmsg;

    /**
     * 媒资信息列表
     */
    private List<MediaInfo> mediaInfoList;


    /**
     * 媒资详细信息类
     */
    @Data
    public static class MediaInfo implements Serializable{

        private static final long serialVersionUID = -4556090871310831701L;
        /**
         * 媒资文件ID
         */
        private long mediaId;

        /**
         * 上传时间戳
         */
        private long createTime;

        /**
         * 过期时间戳
         */
        private long expireTime;

        /**
         * 所属剧目ID
         */
        private int dramaId;

        /**
         * 媒资文件大小（字节）
         */
        private String fileSize;

        /**
         * 播放时长（秒）
         */
        private int duration;

        /**
         * 媒资文件名
         */
        private String name;

        /**
         * 媒资描述
         */
        private String description;

        /**
         * 封面图临时链接
         */
        private String coverUrl;

        /**
         * 原始视频临时链接
         */
        private String originalUrl;

        /**
         * MP4格式临时链接
         */
        private String mp4Url;

        /**
         * HLS格式临时链接
         */
        private String hlsUrl;

        /**
         * 审核信息类
         */
        private MediaAuditDetail auditDetail;

        /**
         * 审核信息详情类
         */
        @Data
        public static class MediaAuditDetail implements Serializable {

            private static final long serialVersionUID = 1607003257246559731L;
            /**
             * 审核状态：0-无效值；1-审核中；2-审核驳回；3-审核通过；4-驳回重填
             */
            private int status;

            /**
             * 提审时间戳
             */
            private long createTime;

            /**
             * 审核时间戳
             */
            private long auditTime;

            /**
             * 审核备注
             */
            private String reason;

            /**
             * 审核证据截图ID列表
             */
            private List<String> evidenceMaterialIdList;

        }
    }


}
