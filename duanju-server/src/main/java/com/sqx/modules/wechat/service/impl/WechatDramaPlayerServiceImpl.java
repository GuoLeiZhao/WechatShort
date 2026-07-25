package com.sqx.modules.wechat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.ConfigConstant;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.course.dao.CourseDao;
import com.sqx.modules.course.dao.CourseDetailsDao;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseDetails;
import com.sqx.modules.orders.dao.OrdersDao;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.wechat.request.LockDataReq;
import com.sqx.modules.wechat.response.CanPlayDramaResponse;
import com.sqx.modules.wechat.service.WechatDramaPlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WechatDramaPlayerServiceImpl implements WechatDramaPlayerService {

    private final UserDao userDao;
    private final CourseDao courseDao;
    private final OrdersDao ordersDao;
    private final CourseDetailsDao courseDetailsDao;

    @Override
    public CanPlayDramaResponse getCanPlayDramaSerialNo(Long dramaId, Long userId, String sessionKey) {
        UserEntity userEntity = userDao.selectById(userId);
        if (null == userEntity) {
            throw new SqxException("用户不存在");
        }

        // 数据准备
        Course course = courseDao.selectCourseInfoByWxMediaId(dramaId);
        Long courseId = course.getCourseId();
        String courseCount = course.getCourseCount();

        if ( 2 == course.getIsPrice()) {
            // 如果是免费
            LockDataReq lockDataReq = new LockDataReq();
            lockDataReq.setOpenid(userEntity.getOpenId());
            lockDataReq.setSrcAppid(ConfigConstant.App1.APP_ID);
            lockDataReq.setDramaId(course.getWxMediaId().toString());
            lockDataReq.setSerialList(Collections.singletonList(new LockDataReq.SerialItem(1, Integer.parseInt(courseCount), 1)));
            lockDataReq.setDataExpireAt(DateUtil.offsetDay(new Date(), 1).getTime() / 1000);
            // 全集免费，从第 1 集到最后一集
            CanPlayDramaResponse.FreeList freeList = new CanPlayDramaResponse.FreeList(1, Integer.parseInt(courseCount));
            return new CanPlayDramaResponse(course.getWxMediaId().toString(), getEncrypt(sessionKey, lockDataReq), freeList);
        }

        List<Orders> orders = ordersDao.selectOrdersCourseCollectByCourseAndUser(courseId, userId);
        CourseDetails courseDetails = courseDetailsDao.queryFreeMaxMin(courseId);
        List<Integer> buyPlays = orders.stream().map(Orders::getCourseDetailsName).filter(StrUtil::isNotBlank).map(Integer::parseInt).collect(Collectors.toList());
        CanPlayDramaResponse.FreeList freeList;
        String min = courseDetails.getMin().trim();
        String max = courseDetails.getMax().trim();
        if (StrUtil.isNotBlank(min) && StrUtil.isNotBlank(courseDetails.getMax())) {
            freeList = new CanPlayDramaResponse.FreeList(Integer.parseInt(min), Integer.parseInt(max));
            for (int i = Integer.parseInt(min); i <= Integer.parseInt(max); i++) {
                buyPlays.add(i);
            }

        } else {
            freeList = new CanPlayDramaResponse.FreeList(0, 0);
        }

        List<LockDataReq.SerialItem> serialList = getSerialList(buyPlays, Integer.parseInt(courseCount));
        LockDataReq lockDataReq = new LockDataReq();
        lockDataReq.setOpenid(userEntity.getOpenId());
        lockDataReq.setSrcAppid(ConfigConstant.App1.APP_ID);
        lockDataReq.setDramaId(dramaId.toString());
        lockDataReq.setSerialList(serialList);
        lockDataReq.setDataExpireAt(DateUtil.offsetHour(new Date(), 2).getTime() / 1000);

        CanPlayDramaResponse canPlayDramaResponse = new CanPlayDramaResponse(course.getWxMediaId().toString(), getEncrypt(sessionKey, lockDataReq), null);
        canPlayDramaResponse.setFreeList(freeList);

        return canPlayDramaResponse;
    }


    private static String getEncrypt(String sessionKey, LockDataReq lockDataReq) {
        String secretKey = sessionKey.length() >= 16 ? sessionKey.substring(0, 16) : sessionKey;
        AES aes = new AES(Mode.ECB, Padding.PKCS5Padding, secretKey.getBytes(StandardCharsets.UTF_8));
        // others
        SerializeConfig serializeConfig = new SerializeConfig();
        // 驼峰转下划线
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        return aes.encryptBase64(JSON.toJSONString(lockDataReq, serializeConfig));
    }

    private List<LockDataReq.SerialItem> getSerialList(List<Integer> serialList, int maxSerialNo) {
        serialList.sort(Comparator.comparing(Integer::intValue));
        int[][] canPlay = new int[maxSerialNo][2];
        int[][] cannotPlay = new int[maxSerialNo][2];

        int j = 0;
        int q = 0;

        for (int i = 1; i <= maxSerialNo; i++) {
            if (serialList.contains(i)) {
                j = getSubArr(canPlay, j, i);
            } else {
                q = getSubArr(cannotPlay, q, i);
            }
        }

        List<LockDataReq.SerialItem> serialItems = Arrays.stream(canPlay).filter(serials -> serials[0] != 0)
                .map(serials -> new LockDataReq.SerialItem(serials[0], serials[1], 1)).collect(Collectors.toList());
        serialItems.addAll(Arrays.stream(cannotPlay).filter(serials -> serials[0] != 0)
                .map(serials -> new LockDataReq.SerialItem(serials[0], serials[1], 2)).collect(Collectors.toList()));
        return serialItems;

    }

    private int getSubArr(int[][] cannotPlay, int q, int i) {
        if (0 == cannotPlay[q][0]) {
            cannotPlay[q][0] = i;
            cannotPlay[q][1] = i;
        } else if (cannotPlay[q][1] == i - 1) {
            cannotPlay[q][1] = i;
        } else {
            q++;
            cannotPlay[q][0] = i;
            cannotPlay[q][1] = i;
        }
        return q;
    }
}
