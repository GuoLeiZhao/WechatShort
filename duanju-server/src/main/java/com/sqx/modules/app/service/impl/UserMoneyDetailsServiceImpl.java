package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserMoneyDetailsDao;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserMoneyDetailsServiceImpl extends ServiceImpl<UserMoneyDetailsDao, UserMoneyDetails> implements UserMoneyDetailsService {

    @Override
    public Result queryUserMoneyDetails(Integer page, Integer limit, Long userId,Integer classify,Integer type) {
        IPage<UserMoneyDetails> page1 = new Page(page, limit);
        QueryWrapper<UserMoneyDetails> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        if(classify!=null){
            queryWrapper.eq("classify", classify);
        }
        if(type!=null){
            queryWrapper.eq("type", type);
        }
        queryWrapper.orderByDesc("create_time");
        return Result.success().put("data", baseMapper.selectPage(page1, queryWrapper));
    }

    @Override
    public Double monthIncome(String date, Long userId) {
        return baseMapper.monthIncome(date,userId);
    }
}
