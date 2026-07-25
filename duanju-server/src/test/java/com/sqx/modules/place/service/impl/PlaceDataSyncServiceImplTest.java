package com.sqx.modules.place.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.sqx.modules.place.entity.Context;
import com.sqx.modules.place.service.PlaceDataSyncService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlaceDataSyncServiceImplTest {
    @Autowired
    private PlaceDataSyncService placeDataSyncService;

    @Test
    public void syncOrderListData() {
//        DateTime startTime = DateUtil.parse("2024-05-20 12:00:00", "yyyy-MM-dd HH:mm:ss");
//        DateTime endTime = DateUtil.parse("2024-05-20 13:00:00", "yyyy-MM-dd HH:mm:ss");
//        placeDataSyncService.syncOrderListData(Context.Platform.LHDeerPlatformSyncData, startTime, endTime);
//
//        DateTime startTime1 = DateUtil.parse("2024-05-20 12:00:00", "yyyy-MM-dd HH:mm:ss");
//        DateTime endTime1 = DateUtil.parse("2024-05-20 13:00:00", "yyyy-MM-dd HH:mm:ss");
//        placeDataSyncService.syncUserListData(Context.Platform.LHDeerPlatformSyncData, startTime1, endTime1);
//
//
//        DateTime startTime2 = DateUtil.parse("2024-04-18 14:00:00", "yyyy-MM-dd HH:mm:ss");
//        DateTime endTime2 = DateUtil.parse("2024-04-18 15:00:00", "yyyy-MM-dd HH:mm:ss");
//        placeDataSyncService.syncLinkListData(Context.Platform.LHDeerPlatformSyncData, startTime2, endTime2);

        DateTime startTime = DateUtil.parse("2024-05-09 18:00:00", "yyyy-MM-dd HH:mm:ss");
        DateTime endTime = DateUtil.parse("2024-05-09 19:00:00", "yyyy-MM-dd HH:mm:ss");
        placeDataSyncService.syncOrderListData(Context.Platform.QsyyPlatformSyncData, startTime, endTime);
//
//        DateTime startTime1 = DateUtil.parse("2024-05-20 12:00:00", "yyyy-MM-dd HH:mm:ss");
//        DateTime endTime1 = DateUtil.parse("2024-05-20 13:00:00", "yyyy-MM-dd HH:mm:ss");
//        placeDataSyncService.syncUserListData(Context.Platform.QsyyPlatformSyncData, startTime1, endTime1);
//
//
//        DateTime startTime2 = DateUtil.parse("2024-04-18 14:00:00", "yyyy-MM-dd HH:mm:ss");
//        DateTime endTime2 = DateUtil.parse("2024-04-18 15:00:00", "yyyy-MM-dd HH:mm:ss");
//        placeDataSyncService.syncLinkListData(Context.Platform.QsyyPlatformSyncData, startTime2, endTime2);
    }

    public static void main(String[] args) {
        System.out.println(DateUtil.formatDateTime(new Date(1715942926 * 1000L)));
    }
}