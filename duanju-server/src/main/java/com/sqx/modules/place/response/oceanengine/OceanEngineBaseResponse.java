package com.sqx.modules.place.response.oceanengine;

import lombok.Data;

import java.io.Serializable;

@Data
public class OceanEngineBaseResponse<T> implements Serializable {
    private static final long serialVersionUID = -599428364464428348L;

    /**
     * 返回码
     */
    private int code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 请求ID
     */
    private String requestId;
}
