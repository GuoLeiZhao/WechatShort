package com.sqx.modules.landPage.request;

import com.sqx.common.base.BasePagingRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class VideoLandingPageShareQueryReq extends BasePagingRequest implements Serializable {

    private static final long serialVersionUID = -8819329884616924955L;

    private Long id;
    private String name;
    private String nameLike;
    private String groupName;
    private String type;
}
