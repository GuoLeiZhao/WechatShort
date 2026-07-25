package com.sqx.common.base;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @version 1.0
 */
@Data
public class BasePagingRequest implements Serializable {

    private static final long	serialVersionUID	= 512197278510336581L;
    private Integer				currPage			= 1;
    private Integer				pageSize			= 10;
    private List<Order>         orders;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Order implements Serializable{
        private static final long serialVersionUID = -4968784374351314400L;

        private String order;
        private boolean asc = true;
    }

}

