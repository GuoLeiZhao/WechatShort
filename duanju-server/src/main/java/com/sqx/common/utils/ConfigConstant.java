package com.sqx.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统参数相关Key
 *
 * 微信小程序/公众号的 appid、secret、token、aesKey、appKey 全部通过 application.yml
 * 的 wx.miniapp.configs 配置（按 name 区分 app1/app1gzh/app2），
 * 由 {@link com.sqx.config.WxConfigLoader} 在启动时注入到下面的静态字段。
 * 因此本文件不含任何明文密钥；更换账号只需改 application.yml。
 */
public class ConfigConstant {
    /**
     * 云存储配置KEY
     */
    public final static String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";
    public final static Map<String, WechatConfig> wxConfigMap = new HashMap<>();

    public static final String KEY_APP1 = "app1";
    public static final String KEY_APP1GZH = "app1gzh";
    public static final String KEY_APP2 = "app2";

    // 短剧平台（值由 application.yml 注入）
    public static class App2 extends WechatConfig {
        private static final long serialVersionUID = 6433747561526933777L;
        public static String APP_ID;
        public static String TOKEN;
        public static String ENCODING_AES_KEY;
        public static String APP_KEY;
        public static String SECRET;
        public static final String WX_AUTH_TOKEN_KEY = "WX_AUTH_TOKEN_KEY_APP2";

        public App2() {
            super(APP_ID, APP_KEY, SECRET, WX_AUTH_TOKEN_KEY, TOKEN, ENCODING_AES_KEY);
        }
    }

    // 示例 - 短剧 小程序（值由 application.yml 注入）
    public static class App1 extends WechatConfig {
        private static final long serialVersionUID = -449489200894755446L;
        public static String APP_ID;
        public static String APP_KEY;
        public static String SECRET;
        public static final String WX_AUTH_TOKEN_KEY = "WX_AUTH_TOKEN_KEY_APP1";
        public static String TOKEN;
        public static String ENCODING_AES_KEY;

        public App1() {
            super(APP_ID, APP_KEY, SECRET, WX_AUTH_TOKEN_KEY, TOKEN, ENCODING_AES_KEY);
        }
    }

    // 示例 - 短剧 公众号（值由 application.yml 注入）
    public static class App1Gzh extends WechatConfig {
        private static final long serialVersionUID = -7926877782905062282L;
        public static String APP_ID;
        public static String APP_KEY;
        public static String SECRET;
        public static final String WX_AUTH_TOKEN_KEY = "WX_OFA_AUTH_TOKEN_KEY";

        public App1Gzh() {
            super(APP_ID, APP_KEY, SECRET, WX_AUTH_TOKEN_KEY, "", "");
        }
    }

    @Data
    @AllArgsConstructor
    public static class WechatConfig implements Serializable {

        private static final long serialVersionUID = -8903115640815333261L;

        private String appId;
        // 虚拟支付 appKey
        private String appKey;
        private String secret;
        private String redisKey;
        private String token;
        private String encodingAESKey;
    }
}
