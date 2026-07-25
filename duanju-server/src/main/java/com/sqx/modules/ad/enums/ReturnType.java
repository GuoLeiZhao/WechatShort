package com.sqx.modules.ad.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReturnType {
    ACTIVE("ACTIVE", "激活"),
    GAME_ADDICTION("GAME_ADDICTION", "关键行为"),
    LT_ROI("LT_ROI", "变现roi");

    private final String value;  // 直接存储大写的值
    private final String label;
}