package com.sqx.modules.place.response.rmwh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
public class RmwhBaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1930684688474343508L;

    // 状态码 200：成功，400：失败
    private int code;
    // 失败原因
    private String msg;

    private T data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Page<T> implements Serializable {

        private static final long serialVersionUID = -7304516386349415557L;

        private int page;
        private int totalCount;
        private List<T> list;

    }
}
