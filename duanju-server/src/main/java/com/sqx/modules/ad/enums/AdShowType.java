package com.sqx.modules.ad.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AdShowType {
    VIOLENT("VIOLENT", "暴力变现"),
    MODEL_XIAOMAI("MODEL_XIAOMAI", "产品模型一"),
    MODEL_XIAOMAI_2("MODEL_XIAOMAI_2", "产品模型二");

    private final String value;
    private final String label;

}