package com.sqx.modules.applog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.sqx.common.base.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("app_log")
public class Log extends BaseEntity{
    @TableId(type = IdType.INPUT)
    @TableField("id")
    private String id;

    @TableField("data")
    private String data;
}
