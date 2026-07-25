package com.sqx.modules.douyin.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateOrderResponse implements Serializable {
    private static final long serialVersionUID = -6159280575647724481L;

    @JSONField(name = "order_id")
    private String orderId;

    @JSONField(name = "order_token")
    private String orderToken;
}
