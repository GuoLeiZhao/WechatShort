package com.sqx.modules.place.response.sykt;

import lombok.Data;

import java.io.Serializable;

@Data
public class SyktBaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 5376475666481048314L;

    private String msg;
    private int code;
    private T data;
}
