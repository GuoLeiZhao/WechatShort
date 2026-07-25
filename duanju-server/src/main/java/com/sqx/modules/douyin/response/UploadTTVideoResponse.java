package com.sqx.modules.douyin.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class UploadTTVideoResponse implements Serializable {
    private static final long serialVersionUID = -8915347583231901563L;
    private String resource_type;
    private VideoResult video_result;

    @Data
    public static class VideoResult implements Serializable {
        private static final long serialVersionUID = -2541213832593332789L;
        private String open_video_id;
    }
}