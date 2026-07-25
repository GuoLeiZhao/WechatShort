package com.sqx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 */
@Data
@ConfigurationProperties(prefix = "wx.miniapp")
public class WxMaProperties {

    private List<Config> configs;

    @Data
    public static class Config {
        /**
         * 配置名称，用于区分多个小程序/公众号：app1(小程序) / app1gzh(公众号) / app2
         */
        private String name;

        /**
         * 设置微信小程序的appid
         */
        private String appid;

        /**
         * 虚拟支付 appKey（可留空）
         */
        private String appKey;

        /**
         * 设置微信小程序的Secret
         */
        private String secret;

        /**
         * 设置微信小程序消息服务器配置的token
         */
        private String token;

        /**
         * 设置微信小程序消息服务器配置的EncodingAESKey
         */
        private String aesKey;

        /**
         * 消息格式，XML或者JSON
         */
        private String msgDataFormat;
    }

}
