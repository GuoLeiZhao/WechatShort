package com.sqx.modules.shortUrl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("short_url")
public class ShortUrl implements Serializable {
    private static final long serialVersionUID = 5083779859759785715L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;

    /** 短链 */
    private String surl;

    /** 长链 */
    private String lurl;

    /** 调用次数(PV) */
    private Long views;

    /** 调用次数(UV) */
    @TableField(exist = false)
    private Long viewsUV;

    /** 是否删除 */
    private Boolean deleted;

    /** 创建时间 */
    @TableField("created_at")
    private Date createdAt;

    /** 修改时间 */
    @TableField("modified_at")
    private Date modifiedAt;

    @TableField("bak_url")
    private String bakUrl;

    /** 短链类型 */
    private String type;
}
