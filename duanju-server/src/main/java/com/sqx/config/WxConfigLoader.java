package com.sqx.config;

import com.sqx.common.utils.ConfigConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 启动时把 application.yml 里 wx.miniapp.configs（按 name 区分）的值
 * 注入到 {@link ConfigConstant} 的静态字段，并构建 wxConfigMap。
 * 更换微信账号只需改 application.yml，无需改 Java 源码。
 */
@Component
public class WxConfigLoader {

    @Autowired
    private WxMaProperties properties;

    @PostConstruct
    public void load() {
        if (properties.getConfigs() != null) {
            for (WxMaProperties.Config c : properties.getConfigs()) {
                String name = c.getName() == null ? "" : c.getName().trim().toLowerCase();
                switch (name) {
                    case "app1":
                        ConfigConstant.App1.APP_ID = c.getAppid();
                        ConfigConstant.App1.SECRET = c.getSecret();
                        ConfigConstant.App1.TOKEN = c.getToken();
                        ConfigConstant.App1.ENCODING_AES_KEY = c.getAesKey();
                        ConfigConstant.App1.APP_KEY = c.getAppKey();
                        break;
                    case "app1gzh":
                        ConfigConstant.App1Gzh.APP_ID = c.getAppid();
                        ConfigConstant.App1Gzh.SECRET = c.getSecret();
                        ConfigConstant.App1Gzh.APP_KEY = c.getAppKey();
                        break;
                    case "app2":
                        ConfigConstant.App2.APP_ID = c.getAppid();
                        ConfigConstant.App2.SECRET = c.getSecret();
                        ConfigConstant.App2.TOKEN = c.getToken();
                        ConfigConstant.App2.ENCODING_AES_KEY = c.getAesKey();
                        ConfigConstant.App2.APP_KEY = c.getAppKey();
                        break;
                    default:
                        break;
                }
            }
        }
        ConfigConstant.wxConfigMap.put(ConfigConstant.KEY_APP1, new ConfigConstant.App1());
        ConfigConstant.wxConfigMap.put(ConfigConstant.KEY_APP1GZH, new ConfigConstant.App1Gzh());
        ConfigConstant.wxConfigMap.put(ConfigConstant.KEY_APP2, new ConfigConstant.App2());
    }
}
