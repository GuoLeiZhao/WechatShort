package com.sqx.modules.bilibili.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class BilibiliBaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 8450304898962176921L;

    private Integer errcode;
    private Integer code;

    private T data;
}
