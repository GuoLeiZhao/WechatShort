package com.sqx.modules.ad.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeGranularityType {
    STAT_TIME_GRANULARITY_DAILY("STAT_TIME_GRANULARITY_DAILY", "按天维度"),
    STAT_TIME_GRANULARITY_HOURLY("STAT_TIME_GRANULARITY_HOURLY", "按小时维度");

    private final String value;
    private final String label;

}