package com.sqx.modules.place.response.rmwh;

import lombok.Data;

import java.io.Serializable;

@Data
public class RmwhOrder implements Serializable {
    private static final long serialVersionUID = -4850328764712997241L;

    /**
     * 订单充值金额
     */
    private Float price;
    /**
     * 支付渠道：微信虚拟、微信非虚拟、抖音-IOS、抖音-安卓
     */
    private String orderChannel;
    /**
     * 支付平台单号
     */
    private String orderId;
    /**
     * 下单时间
     */
    private String orderTime;
    /**
     * 支付时间
     */
    private String payTime;
    /**
     * 支付状态，0待支付，1已支付
     */
    private Integer orderStatus;
    /**
     * 充值类型，充值目的：充值，冲会员等
     */
    private String orderType;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户openid
     */
    private String openId;
    /**
     * 用户注册时间
     */
    private String userName;
    /**
     * 用户注册时间
     */
    private String regTime;
    /**
     * 推广链接id
     */
    private Long channelId;
    /**
     * 推广链接名称
     */
    private String channelName;
    /**
     * 短剧jid
     */
    private Long videoId;
    /**
     * 短剧名称
     */
    private String videoName;
    /**
     * 小程序名称
     */
    private String miniappName;
    /**
     * 小程序appid
     */
    private String miniappAppid;
    /**
     * 1安卓，2ios
     */
    private Integer osType;
    /**
     * 代理商ID，包含主账号与子账号
     */
    private Long agentId;
    /**
     * 广告id
     */
    private String clickid;
    /**
     * ip
     */
    private String ip;
    /**
     * 代理商推广链接透传字段
     */
    private String eprm;


}
