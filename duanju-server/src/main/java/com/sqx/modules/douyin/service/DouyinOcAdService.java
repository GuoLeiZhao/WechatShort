package com.sqx.modules.douyin.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.douyin.request.ConversionRequest;

public interface DouyinOcAdService {

    Result uploadConversion(ConversionRequest conversionRequest);

}
