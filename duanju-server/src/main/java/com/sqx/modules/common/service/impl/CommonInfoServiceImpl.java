package com.sqx.modules.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.dao.CommonInfoDao;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
@Service
public class CommonInfoServiceImpl extends ServiceImpl<CommonInfoDao, CommonInfo> implements CommonInfoService {


    private final CommonInfoDao commonInfoDao;

    @Autowired
    public CommonInfoServiceImpl(CommonInfoDao commonInfoDao) {
        this.commonInfoDao = commonInfoDao;
    }

    @Override
    public Result update(CommonInfo commonInfo) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        commonInfo.setCreateAt(sdf.format(now));
        commonInfoDao.updateById(commonInfo);
        return Result.success();
    }


    @Override
    public CommonInfo findOne(int id) {
        return commonInfoDao.findOne(id);
    }

    @Override
    public Result delete(long id) {
        commonInfoDao.deleteById(id);
        return Result.success();
    }


    @Override
    public Result updateBody(CommonInfo commonInfo) {
        commonInfoDao.updateById(commonInfo);
        return Result.success();
    }

    @Override
    public Result findByType(Integer type) {
        return Result.success().put("data",commonInfoDao.findOne(type));
    }

    @Override
    public Result findByTypeAndCondition(String condition) {
        return Result.success().put("data",commonInfoDao.findByCondition(condition));
    }

    @Override
    public Result findByConditionAndMax(String max, String condition) {
        return Result.success().put("data",commonInfoDao.findByConditionAndMax(max, condition));
    }
    @Override
    public Result findByConditionAndValue(String value, String condition) {
        return Result.success().put("data",commonInfoDao.findByConditionAndValue(value, condition));
    }
}