package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.app.dao.UserMoneyDao;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserMoneyServiceImpl extends ServiceImpl<UserMoneyDao, UserMoney> implements UserMoneyService {

    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;

    @Override
    public void updateMoney(int i, Long userId, double money){
        baseMapper.updateMayMoney(i,userId,money);
    }

    @Override
    public UserMoney selectUserMoneyByUserId(Long userId){
        UserMoney userMoney = baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
        if(userMoney==null){
            userMoney=new UserMoney();
            userMoney.setUserId(userId);
            userMoney.setMoney(new BigDecimal("0.00"));
            baseMapper.insert(userMoney);
        }
        return userMoney;
    }

    @Override
    public Boolean updateMoneyByUserId(Long userId, Integer type, double money, String content, String title){
        this.updateMoney(type, userId, money);
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setMoney(new BigDecimal(money));
        userMoneyDetails.setUserId(userId);
        userMoneyDetails.setContent(content);
        userMoneyDetails.setTitle(title);
        userMoneyDetails.setType(type);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
        return userMoneyDetailsService.save(userMoneyDetails);
    }


}
