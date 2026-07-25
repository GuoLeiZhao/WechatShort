package com.sqx.modules.app.service;

import com.sqx.common.utils.Result;

import java.util.Date;

public interface UserSignService {

    /**
     * 签到
     *
     * @param userId
     * @return
     */
    Result signIn(Long userId);

    boolean checkSign(Long userId, Date date);

    /**
     * 统计连续签到的次数
     *
     * @param userId
     * @param date
     * @return
     */
    int getContinuousSignCount(Long userId, Date date);

    /**
     * 获取单月连续签到次数
     * @param key
     * @param dayOfMonth
     * @return
     */
    int getSignCountFromRedisSingleMonth(String key, int dayOfMonth);


    Result selectSignDay(Long userId);

}
