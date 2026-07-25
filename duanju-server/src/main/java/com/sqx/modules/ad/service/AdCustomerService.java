package com.sqx.modules.ad.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdCustomer;
import com.sqx.modules.ad.request.AdCustomerQueryReq;

import java.util.List;

public interface AdCustomerService extends IService<AdCustomer> {

    IPage<AdCustomer> page(AdCustomerQueryReq req);

    List<AdCustomer> dropList(AdCustomerQueryReq req);

    Result selectById(Long advertiserId);

    Result selectBatchIds(List<Long> ids);

    void syncAdCustomerList();

}
