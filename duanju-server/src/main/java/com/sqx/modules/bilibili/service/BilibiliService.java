package com.sqx.modules.bilibili.service;

import com.sqx.modules.bilibili.request.Code2SessionRequest;
import com.sqx.modules.bilibili.response.Code2SessionResponse;

public interface BilibiliService {
    String getAccessToken(String app);

    Code2SessionResponse code2Session(String app, Code2SessionRequest request);
}
