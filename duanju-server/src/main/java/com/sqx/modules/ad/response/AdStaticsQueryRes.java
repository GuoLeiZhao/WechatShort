package com.sqx.modules.ad.response;

import com.sqx.modules.ad.entity.AdStatics;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdStaticsQueryRes extends AdStatics implements Serializable {
    private static final long serialVersionUID = -6717055872077029101L;

}
