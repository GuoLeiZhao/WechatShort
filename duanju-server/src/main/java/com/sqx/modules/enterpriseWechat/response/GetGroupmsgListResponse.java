package com.sqx.modules.enterpriseWechat.response;

import com.sqx.modules.enterpriseWechat.request.AddMsgTemplateRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GetGroupmsgListResponse extends EnterpriseWechatBaseResponse {
    private static final long serialVersionUID = -8045235539947624812L;

    private String next_cursor;
    private List<GroupMsg> groupMsgList;

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class GroupMsg extends AddMsgTemplateRequest {
        private static final long serialVersionUID = 4848861257508120126L;

        /** 群发消息创建者userid，API接口创建的群发消息不返回该字段 */
        private String creator;
        private String createTime;
        /** 0：企业 1：个人 */
        private Integer createType;
    }

}
