package com.sqx.modules.enterpriseWechat.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AddMsgTemplateResponse extends EnterpriseWechatBaseResponse{
    private static final long serialVersionUID = 8996087030508120484L;

    private List<String> failList;
    private String msgid;

}
