package com.sqx.modules.douyin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 2. 上传视频
 * https://bytedance.larkoffice.com/wiki/SG1MwJyWhiqwUxkkZvjcConanue
 * */
@Data
public class UploadTTVideoRequest implements Serializable {
    private static final long serialVersionUID = -8687844300205870106L;

    @JsonProperty("resource_type")
    private Integer resourceType;

    @JsonProperty("ma_app_id")
    private String maAppId;

    @JsonProperty("video_meta")
    private VideoMeta videoMeta;

    @Data
    public static class VideoMeta implements Serializable {
        private static final long serialVersionUID = 7523705750272506274L;

        private String url;
        private String title;
        private String description;
        private String format;
        @JsonProperty("use_dy_cloud")
        private Boolean useDyCloud;
        @JsonProperty("dy_cloud_id")
        private String dyCloudId;

    }
}