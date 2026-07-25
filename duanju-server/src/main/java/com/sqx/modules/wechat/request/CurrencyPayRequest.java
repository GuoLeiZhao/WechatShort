package com.sqx.modules.wechat.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class CurrencyPayRequest implements Serializable {

    private static final long serialVersionUID = -7532509112600682684L;

    private String openid; // 用户的openid
    private int env = 0; // 环境标识，0表示正式环境，1表示沙箱环境
    private String userIp; // 用户ip，例如: "1.1.1.1"
    private int amount; // 支付的代币数量
    private String orderId; // 订单号
    private String payitem; // 物品信息列表，记录到账户流水中
    private String remark; // 备注
    private int deviceType = 1; // 平台类型，1表示安卓，2表示苹果

}

