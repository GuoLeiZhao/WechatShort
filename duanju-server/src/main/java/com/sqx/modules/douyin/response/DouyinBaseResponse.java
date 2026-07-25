package com.sqx.modules.douyin.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class DouyinBaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 6473809029106073370L;

    private Integer errNo;
    private Integer err_no;
    private String errTips;
    private String err_tips;

    private T data;
}
