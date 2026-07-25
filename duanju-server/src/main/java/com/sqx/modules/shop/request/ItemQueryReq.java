package com.sqx.modules.shop.request;

import com.sqx.common.base.BasePagingRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class ItemQueryReq extends BasePagingRequest implements Serializable {

    private static final long serialVersionUID = -8819329884616924955L;

    private Long id;
    private String nameLike;
    private String groupName;
    private Date createdStart;
    private Date createdEnd;
    private Boolean shelves;

}