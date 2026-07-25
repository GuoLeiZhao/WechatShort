package com.sqx.modules.douyin.request;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CreateOrderRequest implements Serializable {
    private static final long serialVersionUID = 4364463375394453568L;

    private Long userId;
    private Long courseId;
    private Long courseDetailsId;
    private Long vipDetailsId;
    private BigDecimal money;
    private DouyinCreateOrderRequest douyinCreateOrderRequest;

    @Data
    public static class DouyinCreateOrderRequest implements Serializable {
        private static final long serialVersionUID = 4509034088681678071L;
        /**
         * 小程序APPID
         */
        private String appId;
        /**
         * 开发者侧的订单号。 只能是数字、大小写字母_-*且在同一个app_id下唯一
         */
        private String outOrderNo;
        /**
         * 支付价格。 单位为[分]
         */
        private Long totalAmount;
        /**
         * 商品描述。 长度限制不超过 128 字节且不超过 42 字符
         */
        private String subject;
        /**
         * 商品详情 长度限制不超过 128 字节且不超过 42 字符
         */
        private String body;
        /**
         * 订单过期时间(秒)。最小5分钟，最大2天，小于5分钟会被置为5分钟，大于2天会被置为2天
         */
        private Long validTime;
        /**
         * 签名，详见签名DEMO
         */
        private String sign;
        /**
         * 开发者自定义字段，回调原样回传。 超过最大长度会被截断
         */
        private String cpExtra;
        /**
         * 商户自定义回调地址，必须以 https 开头，支持 443 端口。 指定时，支付成功后抖音会请求该地址通知开发者
         */
        private String notifyUrl;
        /**
         * 第三方平台服务商 id，非服务商模式留空
         */
        private String thirdpartyId;
        /**
         * 可用此字段指定本单使用的收款商户号（目前为灰度功能，需要联系平台运营添加白名单，白名单添加1小时后生效；未在白名单的小程序，该字段不生效）
         */
        private String storeUid;
        /**
         * 是否屏蔽支付完成后推送用户抖音消息，1-屏蔽 0-非屏蔽，默认为0。 特别注意： 若接入POI, 请传1。因为POI订单体系会发消息，所以不用再接收一次担保支付推送消息，
         */
        private int disableMsg = 0;
        /**
         * 支付完成后推送给用户的抖音消息跳转页面，开发者需要传入在app.json中定义的链接，如果不传则跳转首页。
         */
        private String msgPage;
        /**
         * 订单拓展信息，详见下面 expand_order_info 参数说明
         */
        private String expandOrderInfo;
        /**
         * 屏蔽指定支付方式，屏蔽多个支付方式，请使用逗号","分割，枚举值：
         * 屏蔽微信支付：LIMIT_WX
         * 屏蔽支付宝支付：LIMIT_ALI
         * <p>
         * 特殊说明：若之前开通了白名单，平台会保留之前屏蔽逻辑；若传入该参数，会优先以传入的为准，白名单则无效
         */
        private String limitPayWay;

        @Data
        public static class ExpandOrderInfo implements Serializable {

            private static final long serialVersionUID = 3918740560748350538L;
            /**
             * 配送费原价，单位为[分]，仅外卖小程序需要传对应信息
             */
            private Long originalDeliveryFee;
            /**
             * 实付配送费，单位为[分]，仅外卖小程序需要传对应信息
             */
            private Long actualDeliveryFee;

        }
    }
}
