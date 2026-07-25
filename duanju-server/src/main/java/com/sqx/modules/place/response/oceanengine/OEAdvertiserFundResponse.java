package com.sqx.modules.place.response.oceanengine;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OEAdvertiserFundResponse implements Serializable {
    private static final long serialVersionUID = -5779334348008533749L;

    private List<BalanceData> list;

    @Data
    public static class BalanceData implements Serializable {

        private static final long serialVersionUID = -7444000952696974106L;

        private Long account_id;
        private Float balance;
    }

}
