package com.sqx.modules.place.service.manager.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sqx.modules.place.dao.PlaceDramaDao;
import com.sqx.modules.place.dao.PlaceLinkDao;
import com.sqx.modules.place.dao.PlaceOrderDao;
import com.sqx.modules.place.dao.PlaceUserDao;
import com.sqx.modules.place.entity.*;
import com.sqx.modules.place.request.sykt.SYKTPageRequest;
import com.sqx.modules.place.response.rmwh.RmwhBaseResponse;
import com.sqx.modules.place.response.rmwh.RmwhUser;
import com.sqx.modules.place.response.sykt.SyktBaseResponse;
import com.sqx.modules.place.response.sykt.SyktOrder;
import com.sqx.modules.place.response.sykt.SyktUser;
import com.sqx.modules.place.service.manager.PlatformSyncDataTemplate;
import com.sqx.modules.utils.ID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SYKTPlatformSyncDataImpl implements PlatformSyncDataTemplate {

    public static final String BASE_API = "https://short-movie-openapi.sykt520.com/openapi/v2";
    public static final String USER_API = "/user/query";
    public static final String ORDER_API = "/order/query";

    private final PlaceUserDao placeUserDao;
    private final PlaceLinkDao placeLinkDao;
    private final PlaceDramaDao placeDramaDao;
    private final PlaceOrderDao placeOrderDao;

    @Override
    public Authorization getToken() {
        return null;
    }

    @Override
    public void syncOrderList(Date startTime, Date endTime, Authorization authorization, Consumer<? super List<PlaceOrder>> action) {
        String startTimeStr = DateUtil.format(startTime, "yyyyMMddHHmmss");
        String endTimeStr = DateUtil.format(endTime, "yyyyMMddHHmmss");
        int page = 0;
        int size = 500;
        int left = 0;
        do {
            page++;
            SYKTPageRequest syktPageRequest = new SYKTPageRequest(startTimeStr, endTimeStr, page, size);
            SyktBaseResponse<SyktOrder> orderSyktBaseResponse = this.executeApi(Method.POST, BASE_API + ORDER_API, syktPageRequest, "映客美光平台订单同步", response -> JSONObject.parseObject(response.body(), new TypeReference<SyktBaseResponse<SyktOrder>>() {
            }), false, log);

            if (orderSyktBaseResponse.getCode() != 0) {
                log.error("映客美光获取订单信息失败，报错：{} , 当前页码：{}，当前每页个数：{}，剩余 {} 没有同步", orderSyktBaseResponse.getMsg(), syktPageRequest.getPage(), syktPageRequest.getSize(), left);
                break;
            }
            SyktOrder syktOrder = orderSyktBaseResponse.getData();

            // db 操作
            try {

                List<SyktOrder.OrderItem> orderList = syktOrder.getOrderList();
                Map<String, PlaceLink> linkMap = getPlaceLinkByOpenIds(placeLinkDao, orderList.stream()
                        .map(usr -> StrUtil.isBlank(usr.getLinkId()) ? null : usr.getLinkId())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()), Context.Channel.LHDEER);

                Map<String, PlaceDrama> dramaMap = new HashMap<>();
                List<PlaceDrama> dramas = orderList.stream().map(PlaceDrama::new).peek(placeDrama -> {
                    dramaMap.put(placeDrama.getOpenId(), placeDrama);
                }).collect(Collectors.toList());
                dramas.forEach(placeDramaDao::insert);

                List<String> userIds = orderList.stream().map(SyktOrder.OrderItem::getMemberId).collect(Collectors.toList());
                Map<String, PlaceUser> userMap = this.getPlaceUserByOpenIds(placeUserDao, userIds, Context.Channel.YKMG);

                List<PlaceOrder> collect = orderList.stream().map(orderItem ->
                        new PlaceOrder(orderItem, userMap.get(orderItem.getMemberId()), linkMap.get(orderItem.getLinkId()), dramaMap.get(orderItem.getMovieId()))
                ).collect(Collectors.toList());
                action.accept(collect);

            } catch (Exception exception) {
                log.error("映客美光获取用户信息，执行自定义方法时失败, 当前页码：{}，当前每页个数：{}", page, size);
                log.error("映客美光获取用户信息，执行自定义方法时失败", exception);
            }

            int totalCount = syktOrder.getTotalCount();
            left = totalCount - (page * size);

        }while (0 < left);

    }

    @Override
    public void syncChannelList(Date startTime, Date endTime, Authorization authorization, Consumer<? super List<PlaceLink>> action) {

    }

    @Override
    public void syncUserList(Date startTime, Date endTime, Authorization authorization, Consumer<? super List<PlaceUser>> action) {
        String startTimeStr = DateUtil.format(startTime, "yyyyMMddHHmmss");
        String endTimeStr = DateUtil.format(endTime, "yyyyMMddHHmmss");
        int page = 0;
        int size = 500;
        int left = 0;
        do {
            page++;
            SYKTPageRequest syktPageRequest = new SYKTPageRequest(startTimeStr, endTimeStr, page, size);
            SyktBaseResponse<SyktUser> userSyktBaseResponse = this.executeApi(Method.POST, BASE_API + USER_API, syktPageRequest, "映客美光平台用户同步", response -> JSONObject.parseObject(response.body(), new TypeReference<SyktBaseResponse<SyktUser>>() {
            }), false, log);

            if (userSyktBaseResponse.getCode() != 0) {
                log.error("映客美光获取用户信息失败，报错：{} , 当前页码：{}，当前每页个数：{}，剩余 {} 没有同步", userSyktBaseResponse.getMsg(), syktPageRequest.getPage(), syktPageRequest.getSize(), left);
                break;
            }
            SyktUser syktUser = userSyktBaseResponse.getData();

            // db 操作
            try {

                List<SyktUser.UserItem> userList = syktUser.getUserRegList();

                Map<String, PlaceLink> placeLinkOpenIdMap = new HashMap<>();
                List<PlaceLink> placeLinks = userList.stream().map(PlaceLink::new).peek(placeLink -> {
                    placeLinkOpenIdMap.put(placeLink.getOpenId(), placeLink);
                }).collect(Collectors.toList());
                placeLinks.forEach(placeLinkDao::insert);

                List<PlaceUser> collect = userList.stream().map(rmwhUser -> {
                    PlaceLink placeLink = placeLinkOpenIdMap.get(rmwhUser.getLinkId());
                    return new PlaceUser(rmwhUser, placeLink);
                }).collect(Collectors.toList());
                action.accept(collect);
            } catch (Exception exception) {
                log.error("映客美光获取用户信息，执行自定义方法时失败, 当前页码：{}，当前每页个数：{}", page, size);
                log.error("映客美光获取用户信息，执行自定义方法时失败", exception);
            }

            int totalCount = syktUser.getTotalCount();
            left = totalCount - (page * size);

        }while (0 < left);

    }




}
