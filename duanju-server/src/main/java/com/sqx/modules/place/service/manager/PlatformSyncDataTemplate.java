package com.sqx.modules.place.service.manager;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.BeanUtil;
import com.sqx.modules.place.dao.PlaceDramaDao;
import com.sqx.modules.place.dao.PlaceLinkDao;
import com.sqx.modules.place.dao.PlaceUserDao;
import com.sqx.modules.place.entity.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface PlatformSyncDataTemplate {

    Authorization getToken();

    void syncOrderList(Date startTime, Date endTime, Authorization authorization, Consumer<? super List<PlaceOrder>> action);

    void syncChannelList(Date startTime, Date endTime, Authorization authorization, Consumer<? super List<PlaceLink>> action);

    void syncUserList(Date startTime, Date endTime, Authorization authorization, Consumer<? super List<PlaceUser>> action);

    default <R> R executeApi(Method method, String url, Object reqParam, String apiName, Function<HttpResponse, ? extends R> mapper, Boolean requestCamelToUnder, org.slf4j.Logger log) {
        HttpRequest request = HttpUtil.createRequest(method, url);
        if (Method.POST == method) {
            request.contentType(ContentType.JSON.toString());
            if (null != reqParam) {
                if (reqParam instanceof String && StrUtil.isNotBlank(reqParam.toString())) {
                    // 是 string 直接塞进去
                    request.body(reqParam.toString());
                }

                if (requestCamelToUnder) {
                    // others
                    SerializeConfig serializeConfig = new SerializeConfig();
                    // 驼峰转下划线
                    serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
                    request.body(JSON.toJSONString(reqParam, serializeConfig));
                } else {
                    request.body(JSON.toJSONString(reqParam));
                }
            }
        } else if (Method.GET == method) {
            request.contentType(ContentType.FORM_URLENCODED.toString());
            if (null != reqParam) {
                if (requestCamelToUnder) {
                    // others
                    SerializeConfig serializeConfig = new SerializeConfig();
                    // 驼峰转下划线
                    serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
                    request.form(JSON.parseObject(JSON.toJSONString(reqParam, serializeConfig)).getInnerMap());
                } else {
                    request.form(BeanUtil.toMap(reqParam));
                }
            }
        }

        try (HttpResponse response = request.execute()){
            if (200 == response.getStatus()) {
                // 200 成功
                return mapper.apply(response);
            }else {
                log.info("request: " + request);
                log.info("response: "+ response);
                throw new SqxException(apiName + " 接口错误，请查看日志并检查配置！");
            }
        }
    }

    /**
     * 根据 openId 查询推广链接
     * @param placeLinkDao
     * @param openIds
     * @return
     */
    default Map<String, PlaceLink> getPlaceLinkByOpenIds(PlaceLinkDao placeLinkDao, List<String> openIds, Context.Channel channel) {
        if (CollUtil.isEmpty(openIds)) {
            return Collections.emptyMap();
        }
        List<PlaceLink> placeLinks = placeLinkDao.selectList(new QueryWrapper<PlaceLink>().lambda().in(PlaceLink::getOpenId, openIds).eq(PlaceLink::getChannel, channel.name()));
        if (CollUtil.isEmpty(placeLinks)) {
            return Collections.emptyMap();
        }

        return placeLinks.stream().collect(Collectors.toMap(PlaceLink::getOpenId, Function.identity()));
    }

    /**
     * 根据 openId 查询用户
     * @param placeUserDao
     * @param openIds
     * @return
     */
    default Map<String, PlaceUser> getPlaceUserByOpenIds(PlaceUserDao placeUserDao, List<String> openIds, Context.Channel channel) {
        if (CollUtil.isEmpty(openIds)) {
            return Collections.emptyMap();
        }
        List<PlaceUser> placeUsers = placeUserDao.selectList(new QueryWrapper<PlaceUser>().lambda().in(PlaceUser::getOpenId, openIds).eq(PlaceUser::getChannel, channel.name()));
        if (CollUtil.isEmpty(placeUsers)) {
            return Collections.emptyMap();
        }

        return placeUsers.stream().collect(Collectors.toMap(PlaceUser::getOpenId, Function.identity()));
    }

    /**
     * 根据名称查询短剧
     * @param placeDramaDao
     * @param openIds
     * @return
     */
    default Map<String, PlaceDrama> getPlaceDramaByOpenIds(PlaceDramaDao placeDramaDao, List<String> openIds, Context.Channel channel) {
        if (CollUtil.isEmpty(openIds)) {
            return Collections.emptyMap();
        }
        List<PlaceDrama> placeDramas = placeDramaDao.selectList(new QueryWrapper<PlaceDrama>().lambda().in(PlaceDrama::getOpenId, openIds).eq(PlaceDrama::getChannel, channel.name()));
        if (CollUtil.isEmpty(placeDramas)) {

            return Collections.emptyMap();
        }

        return placeDramas.stream().collect(Collectors.toMap(PlaceDrama::getName, Function.identity()));
    }

}
