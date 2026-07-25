package com.sqx.modules.ad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_black_list")
public class AdBlackList extends BaseEntity {
    private static final long serialVersionUID = -2554354989335347643L;

    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;

    @TableField("black_key")
    private BlackKey blackKey;

    @TableField("black_value")
    private String blackValue;

    @TableField("app_id")
    private String appId;

    @Getter
    @AllArgsConstructor
    public enum BlackKey {
        OPENID("用户ID"),
        ;

        private final String desc;
    }

}
