package com.sqx.modules.wechat.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserPhoneNumberRequest implements Serializable {
    private static final long serialVersionUID = -6249029601182610453L;

    // 手机号获取凭证
    private String code;

}
