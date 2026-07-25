package com.sqx.modules.ad.request;

import com.sqx.common.base.BasePagingRequest;
import com.sqx.modules.ad.entity.AdVideoEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdVideoEventQueryReq extends BasePagingRequest implements Serializable {
    private static final long serialVersionUID = 3104780931146423474L;
    private AdVideoEvent.EventType eventType;
    private String appId;
    private String openId;
    private String clickId;
    private String linkId;
    private String albumId;
    private String episodeId;
}