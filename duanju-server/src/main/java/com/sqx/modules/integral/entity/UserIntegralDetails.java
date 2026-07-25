package com.sqx.modules.integral.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("user_integral_details")
public class UserIntegralDetails implements Serializable {
    private static final long serialVersionUID = 2273788098858160348L;
    /**
     * 积分详情id
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 内容
     */
    private String content;

    /**
     * 获取类型 1签到 2 报名 3 购买商品 4 购买单集 5 购买整部 6 完成任务 7 解锁剧集
     */
    private Integer classify;

    /**
     * 物品id
     */
    @TableField(exist = false)
    private Long goodPId;

    /**
     * 物品id
     */
    @TableField("good_id")
    private Long goodId;

    /**
     * 物品id
     */
    @TableField("good_name")
    private String goodName;

    /**
     * 物品id
     */
    @TableField("good_img")
    private String goodImg;
    /**
     * 物品id
     */
    @TableField("order_id")
    private String orderId;

    /**
     * 分类 1增加 2减少
     */
    private Integer type;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 连续第几天
     */
    private Integer day;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private String createTime;

    public static class Constants {

        // 获取类型
        public static class CLASSIFY {
            public static final Integer COST = 0;
            public static final Integer SIGN_IN = 1;
            public static final Integer SIGN_UP = 2;
            public static final Integer BUY = 3;
            public static final Integer COURSE_SINGLE = 4;
            public static final Integer COURSE_WHOLE = 5;
            public static final Integer TASK_COMPLETE = 6;
            public static final Integer ACTIVITY = 7;

        }

        // 记分类型
        public static class TYPE {
            public static final Integer INCREASE = 1;
            public static final Integer DECREASE = 2;
        }


    }

}
