package com.sqx.modules.ad.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReturnCycle {
    HOUR("HOUR", "按小时回传");

    private final String value;
    private final String label;
}