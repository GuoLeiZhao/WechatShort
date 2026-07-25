package com.sqx.modules.douyin.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class DouyinOcAdBaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 6473809029106073370L;
    private Integer code;
    private String message;
}
