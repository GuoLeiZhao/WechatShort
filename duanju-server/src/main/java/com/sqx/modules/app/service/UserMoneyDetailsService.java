package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoneyDetails;

public interface UserMoneyDetailsService extends IService<UserMoneyDetails> {
    Result queryUserMoneyDetails(Integer page, Integer limit, Long userId,Integer classify,Integer type);
    Double monthIncome(String date,Long userId);
}
