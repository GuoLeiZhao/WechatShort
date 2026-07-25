package com.sqx.modules.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Date;

@Data
@TableName("shop_order")
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {
    private static final long serialVersionUID = -4094200955994877353L;

    @TableId(type = IdType.INPUT)
    private String id;

    /** 商品id */
    @TableField("item_id")
    private Long itemId;

    /** 商品名称 */
    @TableField("item_name")
    private String itemName;

    /** 商品封面 */
    @TableField("item_pic")
    private String itemPic;

    /** 订单状态 */
    @TableField("order_status")
    private OrderStatus orderStatus;

    /** 买家姓名 */
    @TableField("buyer_name")
    private String buyerName;

    /** 买家手机 */
    @TableField("buyer_phone")
    private String buyerPhone;

    /**
     * 买家收货地址
     */
    @TableField("buyer_city")
    private String buyerCity;

    /** 买家收货地址 */
    @TableField("buyer_address")
    private String buyerAddress;

    /** 物流编号 */
    @TableField("transfer_no")
    private String transferNo;

    /** 物流状态 */
    @TableField("delivery_status")
    private DeliveryStatus deliveryStatus;

    /** 发货日期 */
    @TableField("delivery_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deliveryDate;

    /** 发货日期 */
    @TableField("order_user_id")
    private Long orderUserId;

    /** 备注 */
    private String remark;


    @Getter
    @AllArgsConstructor
    public enum OrderStatus {
        WAITING("待付款"),
        PAID("已付款"),
        CANCELED("已取消"),
        FINISHED("已完成"),
        ;

        private final String desc;
    }

    @Getter
    @AllArgsConstructor
    public enum DeliveryStatus {
        NOT_DELIVERED("未发货"),
        PICKED_UP("已揽收"),
        DELIVERED("已发货"),
        ;

        private final String desc;
    }


}
