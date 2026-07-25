package com.sqx.modules.ad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 事件定义表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("事件定义表")
@TableName("event_definition")
public class EventDefinition extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 事件定义ID
     */
    @ApiModelProperty("事件定义ID")
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    /**
     * 事件ID
     */
    @ApiModelProperty("事件ID")
    @TableField("event_id")
    private String eventId;

    /**
     * 事件名称
     */
    @ApiModelProperty("事件名称")
    @TableField("event_name")
    private String eventName;

    /**
     * 事件描述
     */
    @ApiModelProperty("事件描述")
    @TableField("event_description")
    private String eventDescription;

    /**
     * 事件分类
     */
    @ApiModelProperty("事件分类")
    private String category;

    /**
     * 状态：0-禁用，1-启用
     */
    @ApiModelProperty("状态")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @TableField("created_at")
    private Date createdAt;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @TableField("updated_at")
    private Date updatedAt;
}
