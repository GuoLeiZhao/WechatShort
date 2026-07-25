package com.sqx.modules.ad.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdStaticsHourRes implements Serializable {
    private static final long serialVersionUID = -9084336755812760208L;

    // 消耗金额分时列表
    private HourData[] paymentHourList;
    // 广告回收分时列表
    private HourData[] costHourList;
    // roi分时列表
    private HourData[] roiHourList;

    @Data
    private static class HourData  implements Serializable {
        private static final long serialVersionUID = -6425228948541397440L;

        private String hour;
        private Float amount;
    }

}
