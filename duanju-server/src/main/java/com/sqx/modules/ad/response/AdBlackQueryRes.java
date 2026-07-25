package com.sqx.modules.ad.response;

import com.sqx.modules.ad.entity.AdBlackList;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdBlackQueryRes extends AdBlackList implements Serializable {
    private static final long serialVersionUID = 6686548763628951347L;

    private String appName;

}
