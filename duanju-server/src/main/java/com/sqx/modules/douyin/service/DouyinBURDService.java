package com.sqx.modules.douyin.service;

import com.sqx.common.utils.Result;
import com.sqx.modules.douyin.request.UploadTTVideoRequest;
import com.sqx.modules.douyin.request.UserAssetFlowRequest;
import com.sqx.modules.douyin.response.UploadTTVideoResponse;

public interface DouyinBURDService {
    String getAccessToken(String app);

    Result uploadUserAsset(String app, UserAssetFlowRequest userAssetFlowRequest);

    UploadTTVideoResponse upLoadTTVideo(String app, UploadTTVideoRequest uploadTTVideoRequest);
}
