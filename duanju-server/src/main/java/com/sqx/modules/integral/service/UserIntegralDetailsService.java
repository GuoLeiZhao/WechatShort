package com.sqx.modules.integral.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.integral.entity.UserIntegralDetails;

public interface UserIntegralDetailsService extends IService<UserIntegralDetails> {

    IPage selectUserIntegralDetailsByUserId(int page, int limit, Long userId);

    IPage payDetails(int page, int limit, Long userId);

    IPage creditsExchangeList(int page, int limit, Long userId);

    Result signIn(Long userId);

    Result signInNew(Long userId, int signDayId);

    Result selectIntegralDay(Long userId);

    Result selectIntegralDayNew(Long userId);

    Result creditsExchange(Long userId, Integer integral);

    /**
     * 积分兑换
     * @param userIntegralDetails
     * @return
     */
    Result creditsExchange(UserIntegralDetails userIntegralDetails);

    Result completeTask(Integer taskId, Long userId);

    Result isCompleteTask(Integer taskId, Long userId);
}
