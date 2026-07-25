package com.sqx.modules.orders.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayRequest implements Serializable {
    private static final long serialVersionUID = -4139527188867937577L;

    private Long orderId;
    private String tenant;
    private String platform;

}
