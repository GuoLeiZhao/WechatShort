package com.sqx.modules.place.response.oceanengine;

import lombok.Data;

import java.io.Serializable;

@Data
public class OEPageInfo  implements Serializable {

    private static final long serialVersionUID = 5580873926678279203L;

    private Integer total_number;
    private Integer page;
    private Integer page_size;
    private Integer total_page;
}
