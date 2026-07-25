package com.sqx.modules.place.service.manager.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sqx.common.utils.RedisUtils;
import com.sqx.modules.place.dao.PlaceDramaDao;
import com.sqx.modules.place.dao.PlaceLinkDao;
import com.sqx.modules.place.dao.PlaceUserDao;
import com.sqx.modules.place.entity.*;
import com.sqx.modules.place.request.rmwh.RmwhPageRequest;
import com.sqx.modules.place.request.rmwh.RmwhTokenRequest;
import com.sqx.modules.place.response.rmwh.RmwhBaseResponse;
import com.sqx.modules.place.response.rmwh.RmwhChannel;
import com.sqx.modules.place.response.rmwh.RmwhOrder;
import com.sqx.modules.place.response.rmwh.RmwhUser;
import com.sqx.modules.place.service.manager.PlatformSyncDataTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component("LHDeerPlatformSyncData")
public class LHDeerPlatformSyncDataImpl implements PlatformSyncDataTemplate {
    private static final String BASE_URL = "https://winapi.lhdeer.com/api";
    private static final String GET_TOKEN_URL = "/getToken";
    private static final String ORDER_LIST_URL = "/order/getOrderList";
    private static final String USER_LIST_URL = "/user/getUserList";
    private static final String TOKEN_REDIS_KEY = "TOKEN_KEY_LHDEER";

    private final RedisUtils redisUtils;
    private final PlaceLinkDao placeLinkDao;
    private final PlaceUserDao placeUserDao;
    private final PlaceDramaDao placeDramaDao;

    @Override
    public Authorization getToken() {

        String token = redisUtils.get(TOKEN_REDIS_KEY);
        if (StrUtil.isNotBlank(token)) {
            return new Authorization(token);
        }

        synchronized (TOKEN_REDIS_KEY) {
            token = redisUtils.get(TOKEN_REDIS_KEY);
            if (StrUtil.isNotBlank(token)) {
                return new Authorization(token);
            }
            RmwhTokenRequest rmwhTokenRequest = new RmwhTokenRequest();
            RmwhBaseResponse<Authorization> authorizationRes = executeApi(Method.POST, BASE_URL + GET_TOKEN_URL,
                    rmwhTokenRequest, "长角鹿获取 token",
                    response -> JSONObject.parseObject(response.body(), new TypeReference<RmwhBaseResponse<Authorization>>() {
                    }),
                    true, log);

            Authorization authorization = authorizationRes.getData();
            // token 两小时过期，按 3688 * 1.9 过期提前获取新的 token
            redisUtils.set(TOKEN_REDIS_KEY, authorization.getToken(), 6840);
            return authorization;
        }
    }

    @Override
    public void syncOrderList(Date startTime, Date endTime, Authorization authorization, Consumer<? super List<PlaceOrder>> action) {
        RmwhPageRequest rmwhPageRequest = new RmwhPageRequest(authorization.getToken());
        rmwhPageRequest.setStartTime(DateUtil.formatDateTime(startTime));
        rmwhPageRequest.setEndTime(DateUtil.formatDateTime(endTime));

        int left = 0;
        do {
            RmwhBaseResponse<RmwhBaseResponse.Page<RmwhOrder>> orderListResponse = executeApi(Method.POST, BASE_URL + ORDER_LIST_URL, rmwhPageRequest, "长角鹿获取订单列表",
                    res -> JSONObject.parseObject(res.body(), new TypeReference<RmwhBaseResponse<RmwhBaseResponse.Page<RmwhOrder>>>() {
                    }), true, log);

            if (orderListResponse.getCode() != 200) {
                log.error("长角鹿获取订单信息失败，报错：{} , 当前页码：{}，当前每页个数：{}，剩余 {} 没有同步", orderListResponse.getMsg(), rmwhPageRequest.getPage(), rmwhPageRequest.getPageSize(), left);
                break;
            }

            RmwhBaseResponse.Page<RmwhOrder> orderListPage = orderListResponse.getData();
            // db 操作
            try {
                List<RmwhOrder> orderList = orderListPage.getList();

                Map<String, PlaceLink> placeLinkOpenIdMap = getPlaceLinkByOpenIds(placeLinkDao, orderList.stream()
                        .map(order -> null == order.getChannelId() || 0L == order.getChannelId() ? null : order.getChannelId().toString())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()), Context.Channel.LHDEER);

                Map<String, PlaceUser> userOpenIdMap = getPlaceUserByOpenIds(placeUserDao, orderList.stream()
                        .map(order -> null == order.getUserId() || 0L == order.getUserId() ? null : order.getUserId().toString())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()), Context.Channel.LHDEER);

                Map<String, PlaceDrama> dramaIdMap = getPlaceDramaByOpenIds(placeDramaDao, orderList.stream()
                        .map(order -> null == order.getVideoId() || 0L == order.getVideoId() ? null : order.getVideoId().toString())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()), Context.Channel.LHDEER);

                List<PlaceOrder> collect = orderList.stream().map(order -> {
                    PlaceLink placeLink = placeLinkOpenIdMap.get(order.getChannelId().toString());
                    PlaceUser placeUser = userOpenIdMap.get(order.getUserId().toString());
                    PlaceDrama placeDrama = dramaIdMap.get(order.getVideoId().toString());
                    if (null == placeDrama) {
                        placeDrama = new PlaceDrama(order.getVideoName(), order.getVideoId().toString(), Context.Channel.LHDEER);
                        placeDramaDao.insert(placeDrama);
                    }
                    return new PlaceOrder(order, placeUser, placeLink, placeDrama);
                }).collect(Collectors.toList());
                action.accept(collect);
            } catch (Exception exception) {
                log.error("长角鹿获取订单信息，执行自定义方法时失败, 当前页码：{}，当前每页个数：{}", rmwhPageRequest.getPage(), rmwhPageRequest.getPageSize());
                log.error("长角鹿获取订单信息，执行自定义方法时失败", exception);
            }

            int totalCount = orderListPage.getTotalCount();
            int currPage = orderListPage.getPage();
            if (totalCount > 0 && 0 < (left = totalCount - (currPage * rmwhPageRequest.getPageSize()))) {
                // 计算剩下的数量，只要大于 0 ，就 ++
                rmwhPageRequest.setPage(currPage + 1);
            }
        } while (left > 0);
    }

    @Override
    public void syncChannelList(Date startTime, Date endTime, Authorization authorization, Consumer<? super List<PlaceLink>> action) {

        RmwhPageRequest rmwhPageRequest = new RmwhPageRequest(authorization.getToken());
        rmwhPageRequest.setStartTime(DateUtil.formatDateTime(startTime));
        rmwhPageRequest.setEndTime(DateUtil.formatDateTime(endTime));

        int left = 0;
        do {
            RmwhBaseResponse<RmwhBaseResponse.Page<RmwhChannel>> channelListResponse = executeApi(Method.POST, BASE_URL + USER_LIST_URL, rmwhPageRequest, "长角鹿获取推广链接列表",
                    res -> JSONObject.parseObject(res.body(), new TypeReference<RmwhBaseResponse<RmwhBaseResponse.Page<RmwhChannel>>>() {
                    }), true, log);

            if (channelListResponse.getCode() != 200) {
                log.error("长角鹿获取推广链接失败，报错：{} , 当前页码：{}，当前每页个数：{}，剩余 {} 没有同步", channelListResponse.getMsg(), rmwhPageRequest.getPage(), rmwhPageRequest.getPageSize(), left);
                break;
            }

            RmwhBaseResponse.Page<RmwhChannel> channelListPage = channelListResponse.getData();
            // db 操作
            try {
                List<PlaceLink> collect = channelListPage.getList().stream().map(PlaceLink::new).collect(Collectors.toList());
                action.accept(collect);
            } catch (Exception exception) {
                log.error("长角鹿获取推广链接列表，执行自定义方法时失败, 当前页码：{}，当前每页个数：{}", rmwhPageRequest.getPage(), rmwhPageRequest.getPageSize());
                log.error("长角鹿获取推广链接列表，执行自定义方法时失败", exception);
            }

            int totalCount = channelListPage.getTotalCount();
            int currPage = channelListPage.getPage();
            if (totalCount > 0 && 0 < (left = totalCount - (currPage * rmwhPageRequest.getPageSize()))) {
                // 计算剩下的数量，只要大于 0 ，就 ++
                rmwhPageRequest.setPage(currPage + 1);
            }
        } while (left > 0);
    }

    @Override
    public void syncUserList(Date startTime, Date endTime, Authorization authorization, Consumer<? super List<PlaceUser>> action) {

        RmwhPageRequest rmwhPageRequest = new RmwhPageRequest(authorization.getToken());
        rmwhPageRequest.setStartTime(DateUtil.formatDateTime(startTime));
        rmwhPageRequest.setEndTime(DateUtil.formatDateTime(endTime));

        int left = 0;
        do {
            RmwhBaseResponse<RmwhBaseResponse.Page<RmwhUser>> userListResponse = executeApi(Method.POST, BASE_URL + USER_LIST_URL, rmwhPageRequest, "长角鹿获取用户列表",
                    res -> JSONObject.parseObject(res.body(), new TypeReference<RmwhBaseResponse<RmwhBaseResponse.Page<RmwhUser>>>() {
                    }), true, log);

            if (userListResponse.getCode() != 200) {
                log.error("长角鹿获取用户信息失败，报错：{} , 当前页码：{}，当前每页个数：{}，剩余 {} 没有同步", userListResponse.getMsg(), rmwhPageRequest.getPage(), rmwhPageRequest.getPageSize(), left);
                break;
            }

            RmwhBaseResponse.Page<RmwhUser> userListPage = userListResponse.getData();
            // db 操作
            try {

                List<RmwhUser> userList = userListPage.getList();

                Map<String, PlaceLink> placeLinkOpenIdMap = getPlaceLinkByOpenIds(placeLinkDao, userList.stream()
                        .map(usr -> null == usr.getChannelId() || 0L == usr.getChannelId() ? null : usr.getChannelId().toString())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()), Context.Channel.LHDEER);

                List<PlaceUser> collect = userList.stream().map(rmwhUser -> {
                    PlaceLink placeLink = placeLinkOpenIdMap.get(rmwhUser.getChannelId().toString());
                    return new PlaceUser(rmwhUser, placeLink);
                }).collect(Collectors.toList());
                action.accept(collect);
            } catch (Exception exception) {
                log.error("长角鹿获取用户信息，执行自定义方法时失败, 当前页码：{}，当前每页个数：{}", rmwhPageRequest.getPage(), rmwhPageRequest.getPageSize());
                log.error("长角鹿获取用户信息，执行自定义方法时失败", exception);
            }

            int totalCount = userListPage.getTotalCount();
            int currPage = userListPage.getPage();
            if (totalCount > 0 && 0 < (left = totalCount - (currPage * rmwhPageRequest.getPageSize()))) {
                // 计算剩下的数量，只要大于 0 ，就 ++
                rmwhPageRequest.setPage(currPage + 1);
            }
        } while (left > 0);
    }

}
