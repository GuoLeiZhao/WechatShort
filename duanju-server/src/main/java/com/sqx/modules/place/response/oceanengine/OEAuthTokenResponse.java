package com.sqx.modules.place.response.oceanengine;

import lombok.Data;

import java.io.Serializable;

@Data
public class OEAuthTokenResponse implements Serializable {
    private static final long serialVersionUID = 8102197031270508309L;


    /**
     * 用于验证权限的token
     */
    private String accessToken;

    /**
     * access_token剩余有效时间,单位(秒)
     */
    private Long expiresIn;

    /**
     * 刷新access_token,用于获取新的access_token和refresh_token，并且刷新过期时间
     */
    private String refreshToken;

    /**
     * refresh_token剩余有效时间,单位(秒)
     */
    private Long refreshTokenExpiresIn;

}
