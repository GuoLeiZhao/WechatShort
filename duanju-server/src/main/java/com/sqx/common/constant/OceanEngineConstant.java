package com.sqx.common.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OceanEngineConstant {

    public static final String ACCESS_TOKEN_LOCK = "OCEAN_ENGINE_ACCESS_TOKEN_LOCK";
    public static final String ACCESS_TOKEN_KEY = "OCEAN_ENGINE_ACCESS_TOKEN";
    public static final String REFRESH_TOKEN_KEY = "OCEAN_ENGINE_REFRESH_TOKEN";
    public static final String AUTH_CODE_KEY = "OCEAN_ENGINE_AUTH_CODE";

    public static final String APP_ID = "__OCEANENGINE_APP_ID__";
    public static final String APP_SECRET = "__OCEANENGINE_APP_SECRET__";

    // 授权管理账号 https://open.oceanengine.com/developer/admin/app_info/basic/__OCEANENGINE_APP_ID__
    public static final List<String> ACCOUNT_IDS =
            Collections.unmodifiableList(Arrays.asList("__OE_ADVERTISER_ID_1__", "__OE_ADVERTISER_ID_2__", "__OE_ADVERTISER_ID_3__", "__OE_ADVERTISER_ID_4__"));


}
