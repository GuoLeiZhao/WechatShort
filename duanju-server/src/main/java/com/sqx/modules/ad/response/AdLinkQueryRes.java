package com.sqx.modules.ad.response;

import com.sqx.modules.ad.entity.AdLink;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class AdLinkQueryRes extends AdLink implements Serializable {
    private static final long serialVersionUID = 1L;

    private String advertiserName;
    private String albumName;
    private String episodeName;
    private String appName;

}
