package com.sqx.modules.ad.request;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sqx.modules.ad.enums.ReturnCycle;
import com.sqx.modules.ad.enums.ReturnType;
import lombok.Data;
import java.io.Serializable;

@Data
public class AdLinkListReq implements Serializable {

    private static final long serialVersionUID = -8819329884616924955L;

    private String linkId;
    private Boolean deleted;
    @TableField("return_type")
    private ReturnType returnType;
    private ReturnCycle returnCycle;

}