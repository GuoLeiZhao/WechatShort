package com.sqx.modules.invite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
@TableName("invite")
public class Invite implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 邀请id
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 邀请人id
     */
    private Long userId;

    /**
     * 被邀请人id
     */
    private Long inviteeUserId;

    /**
     * 状态 0非会员 1会员
     */
    private Integer state;

    /**
     * 收益
     */
    private Double money;

    /**
     * 创建时间
     */
    private String createTime;

}