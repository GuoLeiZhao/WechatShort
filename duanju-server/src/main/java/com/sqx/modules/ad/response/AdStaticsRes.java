package com.sqx.modules.ad.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdStaticsRes implements Serializable {
    private static final long serialVersionUID = -4052519224223268372L;

    // 今日消耗
    private Float allPayments;
    // 今日流量主
    private Float allCost;
    // 首日roi
    private Float allRoi;

}
