package com.sqx.modules.douyin.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "douyin.miniapp")
public class DouyinProperties implements Serializable {
    private static final long serialVersionUID = 5863824880500803120L;

    private List<Config> configs;

    @Data
    public static class Config implements Serializable {

        private static final long serialVersionUID = 7868426344862575174L;

        private String app;
        private String appid;
        private String secret;
        private String salt;
    }


}
