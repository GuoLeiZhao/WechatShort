package com.sqx.config;

import cn.hutool.core.collection.CollUtil;
import com.sqx.modules.douyin.entity.DouyinProperties;
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
@EnableConfigurationProperties(DouyinProperties.class)
public class DouyinConfig {

    private final DouyinProperties douyinProperties;

    private static Map<String, DouyinProperties.Config> propertiesMap;

    public static DouyinProperties.Config getDouyinConfig(String app) {
        DouyinProperties.Config config = propertiesMap.get(app);
        if (config == null) {
            throw new IllegalArgumentException(String.format("未找到对应app=[%s]的配置，请核实！", app));
        }

        return config;
    }

    @PostConstruct
    public void init() {
        List<DouyinProperties.Config> configs = this.douyinProperties.getConfigs();
        if (CollUtil.isEmpty(configs)) {
            throw new IllegalArgumentException("未找到抖音配置，请核实！");
        }

        propertiesMap = configs.stream().collect(Collectors.toMap(DouyinProperties.Config::getApp, Function.identity()));
    }

}
