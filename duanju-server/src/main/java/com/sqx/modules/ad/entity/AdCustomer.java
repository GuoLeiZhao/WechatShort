package com.sqx.modules.ad.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ad_customer")
public class AdCustomer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 广告主id（一个对象只会返回企业号和广告主其中一种）
     */
    @TableId(type = IdType.INPUT)
    @TableField("advertiser_id")
    private Long advertiserId;

    /**
     * 广告主名称（一个对象只会返回企业号和广告主其中一种）
     */
    @TableField("advertiser_name")
    private String advertiserName;

    /**
     * 广告主类型
     * 枚举值：DOU+类广告主账号、NORMAL普通广告主帐号、LOCAL：本地推
     */
    @TableField("advertiser_type")
    private String advertiserType;

    /**
     * 巨量引擎工作台（原纵横组织）id
     */
    @TableField("cc_account_id")
    private String ccAccountId;

}
