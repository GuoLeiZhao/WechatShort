package com.sqx.modules.place.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.sqx.common.exception.SqxException;
import com.sqx.modules.place.dao.PlaceLinkDao;
import com.sqx.modules.place.dao.PlaceOrderDao;
import com.sqx.modules.place.dao.PlaceUserDao;
import com.sqx.modules.place.entity.Authorization;
import com.sqx.modules.place.entity.Context;
import com.sqx.modules.place.entity.PlaceOrder;
import com.sqx.modules.place.service.PlaceDataSyncService;
import com.sqx.modules.place.service.manager.PlatformSyncDataTemplate;
import com.sqx.modules.place.service.manager.impl.LHDeerPlatformSyncDataImpl;
import com.sqx.modules.place.service.manager.impl.QsyyPlatformSyncDataImpl;
import com.sqx.modules.place.service.manager.impl.SYKTPlatformSyncDataImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceDataSyncServiceImpl implements PlaceDataSyncService {

    private final Map<Context.Platform, PlatformSyncDataTemplate> platformSyncDataTemplateMap = new HashMap<>();
    private final ApplicationContext applicationContext;
    private final PlaceLinkDao placeLinkDao;
    private final PlaceUserDao placeUserDao;
    private final PlaceOrderDao placeOrderDao;

    @PostConstruct
    public void init() {
        platformSyncDataTemplateMap.put(Context.Platform.LHDeerPlatformSyncData, applicationContext.getBean(LHDeerPlatformSyncDataImpl.class));
        platformSyncDataTemplateMap.put(Context.Platform.QsyyPlatformSyncData, applicationContext.getBean(QsyyPlatformSyncDataImpl.class));
        platformSyncDataTemplateMap.put(Context.Platform.YkmgPlatformSyncData, applicationContext.getBean(SYKTPlatformSyncDataImpl.class));
    }

    @Override
    public void syncOrderListData(Context.Platform platform, Date startTime, Date endTime) {

        PlatformSyncDataTemplate platformSyncDataTemplate = platformSyncDataTemplateMap.get(platform);
        Authorization token = platformSyncDataTemplate.getToken();
        if (null == token || StrUtil.isBlank(token.getToken())) {
            throw new SqxException("拉取" + platform + "平台时，token 获取失败！");
        }
        platformSyncDataTemplate.syncOrderList(startTime, endTime, token, orders -> {
            orders.forEach(placeOrderDao::insert);
        });
    }
    @Override
    public void syncUserListData(Context.Platform platform, Date startTime, Date endTime) {

        PlatformSyncDataTemplate platformSyncDataTemplate = platformSyncDataTemplateMap.get(platform);
        Authorization token = platformSyncDataTemplate.getToken();
        if (null == token || StrUtil.isBlank(token.getToken())) {
            throw new SqxException("拉取" + platform + "平台时，token 获取失败！");
        }
        platformSyncDataTemplate.syncUserList(startTime, endTime, token, users -> {
            users.forEach(placeUserDao::insert);
            log.info(JSON.toJSONString(users));
        });
    }
    @Override
    public void syncLinkListData(Context.Platform platform, Date startTime, Date endTime) {

        PlatformSyncDataTemplate platformSyncDataTemplate = platformSyncDataTemplateMap.get(platform);
        Authorization token = platformSyncDataTemplate.getToken();
        if (null == token || StrUtil.isBlank(token.getToken())) {
            throw new SqxException("拉取" + platform + "平台时，token 获取失败！");
        }
        platformSyncDataTemplate.syncChannelList(startTime, endTime, token, orders -> {
            orders.forEach(placeLinkDao::insert);
            log.info(JSON.toJSONString(orders));
        });
    }
}
