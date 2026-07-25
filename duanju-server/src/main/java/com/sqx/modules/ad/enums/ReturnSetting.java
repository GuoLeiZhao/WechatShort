package com.sqx.modules.ad.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReturnSetting {
    IPU_ECPM_HIGH("IPU_ECPM_HIGH", "ipu+ecpm摸高"),
    IPU_ECPM_FILTER("IPU_ECPM_FILTER", "ipu+ecpm过滤"),
    IPU_ARPU("IPU_ARPU", "ipu+arpu");

    private final String value;
    private final String label;
}