package com.sqx.modules.place.entity;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import com.sqx.modules.place.response.qsyy.QsyyOrder;
import com.sqx.modules.place.response.rmwh.RmwhOrder;
import com.sqx.modules.place.response.sykt.SyktOrder;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@TableName("tb_place_order")
@EqualsAndHashCode(callSuper = true)
public class PlaceOrder extends BaseEntity {
    private static final long serialVersionUID = -3419997030719312587L;

    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 订单充值金额
     */
    private BigDecimal price;
    /**
     * 支付渠道：微信虚拟、微信非虚拟、抖音-IOS、抖音-安卓
     */
    @TableField("order_channel")
    private OrderChannel orderChannel;
    /**
     * 支付平台单号
     */
    @TableField("order_id")
    private String orderId;
    /**
     * 下单时间
     */
    @TableField("order_time")
    private Date orderTime;
    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Date payTime;
    /**
     * 支付状态，0待支付，1已支付
     */
    @TableField("order_status")
    private Boolean orderStatus;
    /**
     * 充值类型，充值目的：充值，冲会员等
     */
    @TableField("order_type")
    private OrderType orderType;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;
    /**
     * 渠道用户 id
     */
    @TableField("open_id")
    private String openId;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 用户注册时间
     */
    @TableField("reg_time")
    private Date regTime;
    /**
     * 推广链接id
     */
    @TableField("link_id")
    private String linkId;
    /**
     * 推广链接名称
     */
    @TableField("link_name")
    private String linkName;
    /**
     * 短剧jid
     */
    @TableField("video_id")
    private String videoId;
    /**
     * 短剧名称
     */
    @TableField("video_name")
    private String videoName;
    /**
     * 小程序名称
     */
    @TableField("miniapp_name")
    private String miniappName;
    /**
     * 小程序appid
     */
    @TableField("miniapp_appid")
    private String miniappAppid;
    /**
     * 1安卓，2ios
     */
    @TableField("device_type")
    private DeviceType deviceType;
    /**
     * 代理商ID，包含主账号与子账号
     */
    @TableField("agent_id")
    private String agentId;
    /**
     * 广告id
     */
    @TableField("ad_id")
    private String adId;
    /**
     * ip
     */
    private String ip;
    /**
     * 渠道
     */
    private Context.Channel channel;

    @Getter
    @AllArgsConstructor
    public enum OrderChannel {

        WX_VIRTUAL("微信虚拟"),
        WX_NON_VIRTUAL("微信非虚拟"),
        DOUYIN("抖音"),
        DY_DB("抖音担保支付"),
        ALIPAY("支付宝"),
        PALM("三方支付"),
        ;
        private final String desc;
    }

    @Getter
    @AllArgsConstructor
    public enum OrderType {
        RECHARGE("充值"),
        VIP("会员"),
        ;
        private final String desc;
    }

    @Getter
    @AllArgsConstructor
    public enum DeviceType{
        ANDROID("安卓"),
        IOS("IOS"),
        ;
        private final String desc;
    }

    /**
     * 长角鹿构造函数
     * @param order
     * @param placeUser
     * @param placeLink
     * @param placeDrama
     */
    public PlaceOrder(RmwhOrder order, PlaceUser placeUser, PlaceLink placeLink, PlaceDrama placeDrama) {
        this.price = BigDecimal.valueOf(order.getPrice());
        switch (order.getOrderChannel()) {
            case "微信非虚拟":
                this.orderChannel = OrderChannel.WX_NON_VIRTUAL;
                break;
            case "抖音-ANDROID":
                this.orderChannel = OrderChannel.DY_DB;
                break;
            case "抖音-IOS":
                this.orderChannel = OrderChannel.DOUYIN;
                break;
            case "微信虚拟":
                this.orderChannel = OrderChannel.WX_VIRTUAL;
                break;
            default:
                this.orderChannel = OrderChannel.PALM;
                break;
        }
        this.orderId = order.getOrderId();
        this.orderTime = DateUtil.parseDateTime(order.getOrderTime());
        this.payTime = DateUtil.parseDateTime(order.getPayTime());
        this.orderStatus = 1 == order.getOrderStatus();
        switch (order.getOrderType()) {
            case "充值":
                this.orderType = OrderType.RECHARGE;
                break;
            case "会员":
                this.orderType = OrderType.VIP;
                break;
        }
        this.userId = placeUser.getId();
        this.miniappName = order.getMiniappName();
        this.miniappAppid = order.getMiniappAppid();
        this.deviceType = order.getOsType() == 1 ? DeviceType.ANDROID : DeviceType.IOS;
        this.agentId = order.getAgentId().toString();
        this.ip = order.getIp();
        this.channel = Context.Channel.LHDEER;
        setUserLinkDrama(placeUser, placeLink, placeDrama);
    }

    /**
     * 映客美光构造器
     * @param order
     * @param placeUser
     * @param placeLink
     * @param placeDrama
     */
    public PlaceOrder(SyktOrder.OrderItem order, PlaceUser placeUser, PlaceLink placeLink, PlaceDrama placeDrama) {
        this.price = new BigDecimal(order.getPayNotifyAmount());
        switch (order.getRechargeChannel()) {
            case 1:
                this.orderChannel = OrderChannel.ALIPAY;
                break;
            case 2:
                this.orderChannel = OrderChannel.WX_NON_VIRTUAL;
                break;
            case 3:
                this.orderChannel = OrderChannel.DOUYIN;
                break;
            case 4:
                this.orderChannel = OrderChannel.WX_VIRTUAL;
                break;
        }

        this.orderId = order.getOrderId();
        this.orderTime = DateUtil.parseDateTime(order.getOrderCreateTime());
        this.payTime = DateUtil.parseDateTime(order.getPayDate());
        this.orderStatus = 1 == order.getStatus();
        this.orderType = OrderType.RECHARGE;
        this.userId = placeUser.getId();
        this.miniappName = order.getAppName();
        this.miniappAppid = null;
        this.deviceType = order.getDeviceType() == 1 ? DeviceType.ANDROID : DeviceType.IOS;
        this.agentId = order.getAgentId();
        this.adId = order.getAdId();
        this.ip = null;
        this.channel = Context.Channel.YKMG;
        setUserLinkDrama(placeUser, placeLink, placeDrama);
    }

    public PlaceOrder(QsyyOrder order, PlaceUser placeUser, PlaceLink placeLink, PlaceDrama placeDrama) {
        this.price = BigDecimal.valueOf(order.getPayment());
        switch (order.getMch()) {
            case "wx":
                this.orderChannel = OrderChannel.WX_NON_VIRTUAL;
                break;
            case "dy":
                this.orderChannel = OrderChannel.DY_DB;
                break;
            case "dygeneral":
                this.orderChannel = OrderChannel.DOUYIN;
                break;
            case "wxVirtual":
                this.orderChannel = OrderChannel.WX_VIRTUAL;
                break;
            case "palm":
                this.orderChannel = OrderChannel.PALM;
                break;
        }
        this.orderId = order.getOid();

        this.orderTime = new Date(order.getCreateTime() * 1000L);
        this.payTime = null == order.getFinishTime() ? null : new Date(order.getFinishTime() * 1000L);
        this.orderStatus = null == this.payTime;
        this.orderType = null == order.getVip() ? OrderType.RECHARGE : OrderType.VIP;
        this.miniappName = order.getAppName();
        this.miniappAppid = order.getAppid();
        this.deviceType = DeviceType.ANDROID.name().equals(order.getOs()) ? DeviceType.ANDROID : DeviceType.IOS;
        this.agentId = null;
        this.adId = order.getAdId();
        this.ip = order.getIp();
        this.channel = Context.Channel.QSYY;
        setUserLinkDrama(placeUser, placeLink, placeDrama);

    }

    /**
     * 设置公共
     * @param placeUser
     * @param placeLink
     * @param placeDrama
     */
    public void setUserLinkDrama(PlaceUser placeUser, PlaceLink placeLink, PlaceDrama placeDrama){
        this.userName = placeUser.getName();
        this.regTime = placeUser.getRegTime();
        this.openId = placeUser.getOpenId();
        this.linkId = placeLink.getId();
        this.linkName = placeLink.getName();
        this.videoId = placeDrama.getId();
        this.videoName = placeDrama.getName();
    }
}
