package com.sqx.modules.place.request.oceanengine;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RefreshTokenRequest implements Serializable {

    private static final long serialVersionUID = -5612276177687204672L;

    private String appId;
    private String secret;
    private String refreshToken;
}
