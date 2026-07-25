package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.modules.app.entity.UserMoney;

public interface UserMoneyService extends IService<UserMoney> {

    UserMoney selectUserMoneyByUserId(Long userId);

    void updateMoney(int i, Long userId, double money);

    Boolean updateMoneyByUserId(Long userId, Integer type, double money, String content, String title);
}
