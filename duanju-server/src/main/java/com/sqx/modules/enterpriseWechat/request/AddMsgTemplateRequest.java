package com.sqx.modules.enterpriseWechat.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AddMsgTemplateRequest implements Serializable {
    private static final long serialVersionUID = -8649602843093389248L;

    private String chatType;
    private List<String> externalUserid;
    private List<String> chatIdList;
    private TagFilter tagFilter;
    private String sender;
    private boolean allowSelect;
    private Text text;
    private List<Attachment> attachments;

    @Data
    public static class TagFilter implements Serializable {
        private static final long serialVersionUID = -7189539831801547418L;
        private List<GroupList> groupList;

        @Data
        public static class GroupList implements Serializable {
            private static final long serialVersionUID = 3320602352504777106L;

            private List<String> tagList;
        }
    }
    @Data
    public static class Text implements Serializable {
        private static final long serialVersionUID = -7189539831801547418L;
        private String content;
    }

    @Data
    public static class Attachment implements Serializable {
        private static final long serialVersionUID = -8496955812475759049L;
        private AttachmentMsgType msgtype;
        private Image image;
        private Link link;
        private MiniProgram miniprogram;
        private Video video;
        private File file;

        @Data
        public static class Image implements Serializable{
            private static final long serialVersionUID = -876097720201366327L;

            private String mediaId;
            private String picUrl;
        }

        @Data
        public static class Link implements Serializable{
            private static final long serialVersionUID = -876097720201366327L;

            private String title;
            private String picurl;
            private String desc;
            private String url;
        }

        @Data
        public static class MiniProgram implements Serializable{
            private static final long serialVersionUID = -876097720201366327L;

            private String title;
            private String picMediaId;
            private String appid;
            private String page;
        }

        @Data
        public static class Video implements Serializable {
            private static final long serialVersionUID = -876097720201366327L;
            private String mediaId;
        }

        @Data
        public static class File implements Serializable{
            private static final long serialVersionUID = -876097720201366327L;

            private String mediaId;
        }

        public enum AttachmentMsgType{
            image, link, miniprogram, video, file;
        }
    }


}
