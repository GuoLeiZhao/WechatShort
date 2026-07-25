package com.sqx.modules.orders.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseCollect;
import com.sqx.modules.course.entity.CourseDetails;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description orders
 */
@Data
@TableName("orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(type = IdType.AUTO)
    private Long ordersId;

    /**
     * 订单编号
     */
    private String ordersNo;

    /**
     * 支付宝支付单号
     */
    private String tradeNo;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 短剧id
     */
    private Long courseId;

    private Long courseDetailsId;

    @TableField(exist = false)
    /**
     * 短剧对象
     */
    private Course course;
    /**
     * 支付金额
     */
    private BigDecimal payMoney;

    /**
     * 支付方式 1微信app 2微信公众号 3微信小程序 4支付宝  5会员免费 6看点
     */
    private Integer payWay;


    /**
     * 状态 0待支付 1已支付 2已退款
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 退款原因
     */
    private String refundContent;

    /**
     * 订单种类 1短剧 2会员 3充值
     */
    private Integer ordersType;

    /**
     * 0会月/1季度/2年
     */
    private Integer vipNameType;

    @TableField(exist = false)
    private String title;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String courseDetailsName;

}
