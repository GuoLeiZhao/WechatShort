package com.sqx.modules.place.response.sykt;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SyktOrder implements Serializable {
    private static final long serialVersionUID = 6333715672624264462L;

    private List<OrderItem> orderList;
    private int totalCount;

    @Data
    public static class OrderItem implements Serializable {
        private static final long serialVersionUID = 613169932619501449L;

        /** 小程序名称 */
        private String appName;
        /** 授权公众号appId */
        private String gzhId;
        /** 授权公众号的用户openId */
        private String gzhUserId;
        /** 用户ID */
        private String memberId;
        /** 分销商子账户id */
        private String agentId;
        /** 订单ID */
        private String orderId;
        /** 付费时间戳 */
        private String payDate;
        /** 付费金额 单位:分 */
        private String payNotifyAmount;
        /** 用户点击推广链接时间戳 */
        private String createDate;
        /** 推广链接名称 */
        private String linkName;
        /** 推广链接id */
        private String linkId;
        /** 用户首次注册日期时间戳 */
        private String regDate;
        /** 充值关联短剧id */
        private String movieId;
        /** 充值关联短剧名称 */
        private String movieName;
        /** 用户所属小程序的openid */
        private String maOpenid;
        /** 订单状态：0-未支付 1-已支付, 接口只会返回已支付的订单 */
        private Integer status;
        /** 订单下单时间戳 */
        private String orderCreateTime;
        /** 设备系统类型: 1-Android, 2-IOS */
        private Integer deviceType;
        /** 充值渠道: 1-支付宝，2-微信，3-抖音支付，4-微信小程序虚拟支付 */
        private Integer rechargeChannel;
        /** 广告计划ID（部分链路可能获取不到） */
        private String adId;
        /** 广告平台id（内部定义的广告平台id，部分可能获取不到） */
        private String platformId;
        /** 广告平台名称（比如巨量2.0、腾讯广告(新版)，部分可能获取不到） */
        private String platformName;
        /** 广告账号ID（部分可能获取不到） */
        private String advertiserId;
    }
}
