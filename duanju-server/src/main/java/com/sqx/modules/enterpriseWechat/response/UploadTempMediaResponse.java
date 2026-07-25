package com.sqx.modules.enterpriseWechat.response;

import com.sqx.modules.enterpriseWechat.entity.UploadMediaType;
import com.sqx.modules.enterpriseWechat.request.AddMsgTemplateRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UploadTempMediaResponse extends EnterpriseWechatBaseResponse {
    private static final long serialVersionUID = -8045235539947624812L;

    private UploadMediaType type;
    private String mediaId;
    private String createdAt;

}
