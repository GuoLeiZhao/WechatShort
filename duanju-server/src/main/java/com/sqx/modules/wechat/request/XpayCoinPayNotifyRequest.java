package com.sqx.modules.wechat.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class XpayCoinPayNotifyRequest implements Serializable{

    private static final long serialVersionUID = -644711981127922466L;

    /** 小程序原始ID */
    private String ToUserName;
    /** 用户openid */
    private String FromUserName;
    /** 消息发送时间 */
    private long CreateTime;
    /** 消息类型，固定为：event */
    private String MsgType;
    /** 事件类型，固定为：xpay_coin_pay_notify */
    private String Event;
    /** 用户openid */
    private String OpenId;
    /** 业务订单号 */
    private String OutTradeNo;
    /** 环境配置，0：现网环境（正式环境），1：沙箱环境 */
    private int Env;
    /** 微信支付信息 */
    private WeChatPayInfo WeChatPayInfo;
    /** 代币参数信息 */
    private CoinInfo CoinInfo;

    /** WeChatPayInfo类 */
    @Data
    public static class WeChatPayInfo implements Serializable{
        private static final long serialVersionUID = 746649509508137778L;
        /** 微信支付商户单号 */
        private String MchOrderNo;
        /** 交易单号（微信支付订单号） */
        private String TransactionId;
        /** 用户支付时间，Linux秒级时间戳 */
        private long PaidTime;

    }

    /** CoinInfo类 */
    @Data
    public static class CoinInfo implements Serializable {
        private static final long serialVersionUID = -3334679353665661155L;
        /** 数量 */
        private int Quantity;
        /** 物品原始价格（单位：分） */
        private int OrigPrice;
        /** 物品实际支付价格（单位：分） */
        private int ActualPrice;
        /** 透传信息 */
        private String Attach;

    }


}

