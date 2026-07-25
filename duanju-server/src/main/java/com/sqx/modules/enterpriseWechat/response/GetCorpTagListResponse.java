package com.sqx.modules.enterpriseWechat.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetCorpTagListResponse extends EnterpriseWechatBaseResponse{
    private static final long serialVersionUID = -1167895743284161928L;

    private List<TagGroup> tagGroup;

    @Data
    public static class TagGroup implements Serializable {
        private static final long serialVersionUID = 3322724278061374784L;

        private String groupId;
        private String groupName;
        private Long createTime;
        private Integer order;
        private Boolean deleted;
        private List<Tag> tag;

        @Data
        public static class Tag implements Serializable {
            private static final long serialVersionUID = 7632812548356698814L;

            private String id;
            private String name;
            private Long createTime;
            private Integer order;
            private Boolean deleted;
        }
    }

}
