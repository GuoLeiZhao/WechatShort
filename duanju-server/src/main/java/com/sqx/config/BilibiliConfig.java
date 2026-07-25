package com.sqx.config;

import cn.hutool.core.collection.CollUtil;
import com.sqx.modules.bilibili.entity.BilibiliProperties;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BilibiliProperties.class)
public class BilibiliConfig {

    private final BilibiliProperties bilibiliProperties;

    private static Map<String, BilibiliProperties.Config> propertiesMap;

    public static BilibiliProperties.Config getBilibiliConfig(String app) {
        BilibiliProperties.Config config = propertiesMap.get(app);
        if (config == null) {
            throw new IllegalArgumentException(String.format("未找到对应app=[%s]的配置，请核实！", app));
        }

        return config;
    }

    @PostConstruct
    public void init() {
        List<BilibiliProperties.Config> configs = this.bilibiliProperties.getConfigs();
        if (CollUtil.isEmpty(configs)) {
            throw new IllegalArgumentException("未找到b站配置，请核实！");
        }

        propertiesMap = configs.stream().collect(Collectors.toMap(BilibiliProperties.Config::getApp, Function.identity()));
    }

}
