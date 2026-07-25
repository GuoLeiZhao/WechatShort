package com.sqx.modules.ad.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdStaticsHourType {
    CONSUMPTION("CONSUMPTION", "消耗金额"),
    AD_RECOVERY("AD_RECOVERY", "广告回收"),
    ROI("ROI", "ROI");

    private final String value;
    private final String label;

}