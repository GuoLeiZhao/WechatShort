package com.sqx.modules.ad.request;

import com.sqx.common.base.BasePagingRequest;
import com.sqx.modules.ad.entity.AdAdvertEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdAdvertEventQueryReq extends BasePagingRequest implements Serializable {
    private static final long serialVersionUID = 3104780931146423474L;
    private String appId;
    private String openId;
    private String clickId;
    private String linkId;
    private String albumId;
    private String episodeId;
    private String path;
    private AdAdvertEvent.EventType eventType;
    private AdAdvertEvent.AdvertType advertType;
}