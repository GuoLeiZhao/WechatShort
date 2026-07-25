package com.sqx.modules.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("shop_item")
public class Item extends BaseEntity {
    private static final long serialVersionUID = 9063971146763833449L;

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 名称 */
    private String name;
    /** 惊喜名称 */
    @TableField("sup_name")
    private String supName;
    /** 图片 */
    private String pic;
    /** 排序权重 */
    private Long sort;
    /** 描述(详情) */
    @TableField("`desc`")
    private String desc;
    /** 价格(积分) */
    private Long price;
    /** 库存 */
    private Long stock;
    /** 是否上架 */
    private Boolean shelves;

}
