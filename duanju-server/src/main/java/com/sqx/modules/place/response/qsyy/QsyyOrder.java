package com.sqx.modules.place.response.qsyy;

import lombok.Data;

import java.io.Serializable;

@Data
public class QsyyOrder implements Serializable {
    private static final long serialVersionUID = -6351029983870586851L;
    /** 订单号 */
    private String orderId;
    /** 巨量广告计划 ID */
    private String adId;
    /** 推广 ID */
    private String spreadId;
    /** 推广名 */
    private String spreadName;
    /** 小程序 APPID */
    private String appid;
    /** 小程序名 */
    private String appName;
    /** 用户 uid */
    private Long uid;
    /** 用户 openid */
    private String openid;
    /** 商品类型(coin: 点券, vip: VIP) */
    private String goodsType;
    /** 商品名 */
    private String goodsName;
    /** 充值次数 */
    private Long payNum;
    /** 平台订单号 */
    private String oid;
    /** 支付金额(元) */
    private Long payment;
    /** 下单时间(13 位时间戳) */
    private Long createTime;
    /** 成单时间(13 位时间戳) */
    private Long finishTime;
    /** 用户染色时间(13 位时间戳) */
    private Long userCreateTime;
    /** 用户注册时间(13 位时间戳) */
    private Long userRegisterTime;
    /** 是否首充(1: 是, 0: 否) */
    private Long firstPay;
    /** 推广员账号 */
    private String promoter;
    /** 推广员名字 */
    private String promoterName;
    private Coin coin;
    private Vip vip;
    /** 用户 ip(下单时) */
    private String ip;
    /** 用户 ua(下单时) */
    private String ua;
    /** 用户操作系统(下单时)<IOS/Android> */
    private String os;
    /** 回传来源,枚举值(巨量线索,巨量锚点,巨量星图,腾讯广告,企业微信,百度营销,快手线索,小米) */
    private String callbackVersion;
    /** 短剧 ID */
    private Long dramaId;
    /** 短剧名 */
    private String dramaName;
    /** wx: 微信, dy: 抖音 */
    private String platform;
    /** 商户号/商户来源, 枚举值(wx, dy, dy-general, palm, wxVirtual). 注: wx: 微信原生, dy: 抖音担保支付, dy-general: 抖音通用支付, palm: 第三方支付, wxVirtual: 微信虚拟支付 */
    private String mch;

    @Data
    public static class Coin implements Serializable{
        private static final long serialVersionUID = -1910779225916133416L;

        /** 购买点券数量 */
        private Long count;
        /** 赠送点券数量 */
        private Long giveaway;
    }

    @Data
    public static class Vip implements Serializable{
        private static final long serialVersionUID = -1910779225916133416L;

        /** 购买点券数量 */
        private Long count;
        /** 赠送点券数量 */
        private Long giveaway;
    }

}
