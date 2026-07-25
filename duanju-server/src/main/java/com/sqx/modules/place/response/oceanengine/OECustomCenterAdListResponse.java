package com.sqx.modules.place.response.oceanengine;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OECustomCenterAdListResponse implements Serializable {
    private static final long serialVersionUID = 8102197031270508309L;

    private List<Advertiser> list;
    private OEPageInfo page_info;

    @Data
    public static class Advertiser implements Serializable {
        private static final long serialVersionUID = -6495433481070134768L;

        private String cc_account_id;
        private Long advertiser_id;
        private String advertiser_name;
        private String advertiser_type;

    }

}
