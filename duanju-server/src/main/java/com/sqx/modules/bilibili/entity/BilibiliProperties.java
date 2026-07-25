package com.sqx.modules.bilibili.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "bilibili.miniapp")
public class BilibiliProperties implements Serializable {
    private static final long serialVersionUID = -3250266556405075136L;

    private List<Config> configs;

    @Data
    public static class Config implements Serializable {

        private static final long serialVersionUID = -8588550345596543262L;

        private String app;
        private String appid;
        private String secret;
    }


}
