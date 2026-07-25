package com.sqx.modules.place.service.manager.impl;

import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sqx.modules.place.dao.PlaceDramaDao;
import com.sqx.modules.place.dao.PlaceLinkDao;
import com.sqx.modules.place.dao.PlaceUserDao;
import com.sqx.modules.place.entity.*;
import com.sqx.modules.place.request.qsyy.QsyyPageRequest;
import com.sqx.modules.place.response.qsyy.QsyyBaseResponse;
import com.sqx.modules.place.response.qsyy.QsyyOrder;
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
@Component("QsyyPlatformSyncData")
public class QsyyPlatformSyncDataImpl implements PlatformSyncDataTemplate {
    public static final String SIGN_KEY = "PXWWnVt6JWhN5j6V";
    private static final String BASE_URL = "https://yl.qsyy.com/openApi/api";
    private static final String ORDER_LIST_URL = "/orderList";
    private static final String USER_LIST_URL = "/userList";

    private final PlaceLinkDao placeLinkDao;
    private final PlaceUserDao placeUserDao;
    private final PlaceDramaDao placeDramaDao;
    @Override
    public Authorization getToken() {
        return new Authorization("__QSYY_AUTH_TOKEN__");
    }

    @Override
    public void syncOrderList(Date startTime, Date endTime, Authorization authorization, Consumer<? super List<PlaceOrder>> action) {
        Date now = new Date();
        QsyyPageRequest qsyyPageRequest = new QsyyPageRequest(authorization.getToken(), 1L, now, startTime, endTime, SIGN_KEY);

        int leftPage = 0;
        do {
            QsyyBaseResponse<QsyyBaseResponse.Page> orderListResponse = executeApi(Method.GET, BASE_URL + ORDER_LIST_URL, qsyyPageRequest, "奇树有鱼获取订单列表",
                    res -> JSONObject.parseObject(res.body(), new TypeReference<QsyyBaseResponse<QsyyBaseResponse.Page>>() {
                    }), false, log);

            if (orderListResponse.getState() != 0) {
                log.error("奇树有鱼获取订单列表失败，报错：{} , 当前页码：{}，当前每页个数：{}，剩余 {} 页没有同步", orderListResponse.getMsg(), qsyyPageRequest.getPage(), qsyyPageRequest.getPageSize(), leftPage);
                break;
            }

            QsyyBaseResponse.Page orderListPage = orderListResponse.getData();
            // db 操作
            try {
                List<QsyyOrder> orderList = orderListPage.getOrderList();
                log.info("奇树有鱼获取订单列表：{}", JSON.toJSONString(orderList));


                Map<String, PlaceUser> userOpenIdMap = getPlaceUserByOpenIds(placeUserDao, orderList.stream()
                        .map(order -> null == order.getUid() || 0L == order.getUid() ? null : order.getUid().toString())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()), Context.Channel.QSYY);

                Map<String, PlaceDrama> dramaIdMap = getPlaceDramaByOpenIds(placeDramaDao, orderList.stream()
                        .map(order -> null == order.getDramaId() || 0L == order.getDramaId() ? null : order.getDramaId().toString())
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()), Context.Channel.LHDEER);
//
                List<PlaceOrder> collect = orderList.stream().map(order -> {
                    PlaceUser placeUser = userOpenIdMap.get(order.getUid().toString());
                    PlaceDrama placeDrama = dramaIdMap.get(order.getDramaId().toString());
                    if (null == placeDrama) {
                        placeDrama = new PlaceDrama(order.getDramaName(), order.getDramaId().toString(), Context.Channel.LHDEER);
                        placeDramaDao.insert(placeDrama);
                    }
                    return new PlaceOrder(order, placeUser, new PlaceLink(), placeDrama);
                }).collect(Collectors.toList());
                action.accept(collect);
            } catch (Exception exception) {
                log.error("奇树有鱼获取订单列表，执行自定义方法时失败, 当前页码：{}，当前每页个数：{}", qsyyPageRequest.getPage(), qsyyPageRequest.getPageSize());
                log.error("奇树有鱼获取订单列表，执行自定义方法时失败", exception);
            }

            int totalPage = orderListPage.getTotalPage();
            int currPage = orderListPage.getPage();
            if (totalPage > 0 && 0 < (leftPage = totalPage - currPage)) {
                // 计算剩下的数量，只要大于 0 ，就 ++
                qsyyPageRequest.setPage(currPage + 1L);
            }
        } while (leftPage > 0);

    }

    @Override
    public void syncChannelList(Date startTime, Date endTime, Authorization authorization, Consumer<? super List<PlaceLink>> action) {

    }

    @Override
    public void syncUserList(Date startTime, Date endTime, Authorization authorization, Consumer<? super List<PlaceUser>> action) {

    }
}
