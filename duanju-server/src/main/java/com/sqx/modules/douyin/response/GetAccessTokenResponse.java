package com.sqx.modules.douyin.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class GetAccessTokenResponse implements Serializable {
    private static final long serialVersionUID = 4365919240363223459L;

    private String accessToken;
    private Long expiresIn;
    private Long expiresAt;
}
