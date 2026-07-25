package com.sqx.modules.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.entity.CommonInfo;

/**
 */
public interface CommonInfoService extends IService<CommonInfo> {

    /**
     * 保存对象
     *
     * @param
     */
    Result update(CommonInfo commonInfo);

    /**
     * 获取一个对象
     */
    CommonInfo findOne(int id);

    /**
     * 删除一个
     */
    Result delete(long id);

    /**
     * 修改
     */
    Result updateBody(CommonInfo commonInfo);
    /**
     * 通过类型查询
     */
    Result findByType(Integer type);

    /**
     * 通过类型查询
     */
    Result findByTypeAndCondition(String condition);

    /**
     * 通过类型查询
     */
    Result findByConditionAndMax(String max, String condition);

    Result findByConditionAndValue(String value, String condition);
}