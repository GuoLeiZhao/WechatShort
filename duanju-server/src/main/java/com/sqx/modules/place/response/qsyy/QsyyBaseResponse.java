package com.sqx.modules.place.response.qsyy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
public class QsyyBaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1930684688474343508L;

    // 状态码 0：成功，小于 0 异常返回, msg 为异常说明
    private int state;
    // 失败原因
    private String msg;

    private T data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Page implements Serializable {

        private static final long serialVersionUID = -7304516386349415557L;

        private int page;
        private int pageSize;
        private int totalCount;
        private int totalPage;
        private List<QsyyOrder> orderList;
        private List<QsyyUser> userList;

    }
}
