package com.sqx.modules.activity.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@Data
@TableName("invite_activity")
@EqualsAndHashCode(callSuper = true)
public class InviteActivity extends BaseEntity {
    private static final long serialVersionUID = 1623390554371607103L;

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 领取标题 */
    private String title;
    /** 6 位唯一标识，数字加字母 */
    private String hash;
    /** 领取状态 */
    private Status status;
    /** 领取状态 */
    @TableField(exist = false)
    private String statusDesc;
    /** 领取用户 */
    @TableField("user_id")
    private Long userId;
    /** 领取用户名称 */
    @TableField(exist = false)
    private String userName;
    /** 赠送看点 / 积分数量 */
    private Double num;
    /** 领取时间 */
    @TableField("receive_time")
    private String receiveTime;
    /** 活动类型 */
    private Type type;
    /** 活动类型 */
    @TableField(exist = false)
    private String typeDesc;

    @Getter
    public enum Status {
        WAITING("待领取"),
        FINISHED("已发放"),
        ;

        private final String value;

        Status(String value) {
            this.value = value;
        }

    }
    @Getter
    public enum Type {
        INTEGRAL("积分"),
        POINT("看点"),
        COUPON("优惠券"),
        ;

        private final String value;

        Type(String value) {
            this.value = value;
        }

    }
}
