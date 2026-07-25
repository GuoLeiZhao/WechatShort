package com.sqx.common.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -8842923974834047689L;

    /** 创建人 */
    @TableField("created_by")
    private String 				createdBy;
    /** 创建时间 */
    @TableField("created_at")
    private Date                createdAt;
    /** 修改时间 */
    @TableField("modified_at")
    private Date				modifiedAt;
    /** 是否删除 */
    private Boolean				deleted;
}
