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
 * 事件属性表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("事件属性表")
@TableName("event_property")
public class EventProperty extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 属性记录ID
     */
    @ApiModelProperty("属性记录ID")
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    /**
     * 关联事件记录ID
     */
    @ApiModelProperty("关联事件记录ID")
    @TableField("event_track_id")
    private Long eventTrackId;

    /**
     * 属性键名，最大256字符
     */
    @ApiModelProperty("属性键名")
    @TableField("property_key")
    private String propertyKey;

    /**
     * 属性值
     */
    @ApiModelProperty("属性值")
    @TableField("property_value")
    private String propertyValue;

    /**
     * 属性类型：string-字符型，number-数值型
     */
    @ApiModelProperty("属性类型")
    @TableField("property_type")
    private String propertyType;

    /**
     * 记录创建时间
     */
    @ApiModelProperty("记录创建时间")
    @TableField("created_at")
    private Date createdAt;
}
