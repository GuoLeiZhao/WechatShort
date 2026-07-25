package com.sqx.modules.landPage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sqx.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class VideoLandingPageClickStatic extends BaseEntity {
    private static final long serialVersionUID = 4737002431755288512L;

    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("landing_page_id")
    private Long landingPageId;
    @TableField("landing_page_name")
    private String landingPageName;
    private String phone;
    @TableField(exist = false)
    private String phoneCode;
    private Long num;
    private String remark;

}
