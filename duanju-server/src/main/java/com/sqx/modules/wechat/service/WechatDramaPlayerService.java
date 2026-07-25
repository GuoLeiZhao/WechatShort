package com.sqx.modules.wechat.service;

import com.sqx.modules.wechat.request.LockDataReq;
import com.sqx.modules.wechat.response.CanPlayDramaResponse;

public interface WechatDramaPlayerService {
    CanPlayDramaResponse getCanPlayDramaSerialNo(Long dramaId, Long userId, String sessionKey);
}
