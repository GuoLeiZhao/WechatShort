package com.sqx.modules.place.request.oceanengine;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CustomerCenterAdListRequest implements Serializable {

    private static final long serialVersionUID = 4332277980815917533L;

    private String ccAccountId;
    private Integer page;
    private Integer page_size;

}