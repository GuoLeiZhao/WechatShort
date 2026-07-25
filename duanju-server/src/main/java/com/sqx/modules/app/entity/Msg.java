package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
@TableName("msg")
public class Msg implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    private String code;

    private String phone;


}