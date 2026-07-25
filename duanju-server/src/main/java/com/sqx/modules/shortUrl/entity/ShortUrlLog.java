package com.sqx.modules.shortUrl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("short_url_log")
public class ShortUrlLog implements Serializable {
    private static final long serialVersionUID = -5967388967132986175L;

    @TableId(type = IdType.AUTO)
    private Long id;
    /** 短链ID */
    @TableField("short_url_id")
    private Long shortUrlId;
    /** ip */
    private String ip;
    /** 国 */
    private String country;
    /** 省 */
    private String region;
    /** 市 */
    private String city;
    /** 创建时间 */
    @TableField("created_at")
    private Date createdAt;
}
