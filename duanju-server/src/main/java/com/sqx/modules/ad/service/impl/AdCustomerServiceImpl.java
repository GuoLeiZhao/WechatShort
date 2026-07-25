package com.sqx.modules.ad.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.base.BasePagingRequest;
import com.sqx.common.constant.OceanEngineConstant;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.dao.AdCustomerDao;
import com.sqx.modules.ad.entity.AdCustomer;
import com.sqx.modules.ad.request.AdCustomerQueryReq;
import com.sqx.modules.ad.service.AdCustomerService;
import com.sqx.modules.place.response.oceanengine.OECustomCenterAdListResponse;
import com.sqx.modules.place.service.manager.OceanEngineManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdCustomerServiceImpl extends ServiceImpl<AdCustomerDao, AdCustomer> implements AdCustomerService {

    private final OceanEngineManager oceanEngineManager;

    @Override
    public IPage<AdCustomer> page(AdCustomerQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("advertiser_id", false)));
        return baseMapper.selectPage(
                new Query<AdCustomer>().getPage(req),
                new QueryWrapper<AdCustomer>().lambda()
                        .eq(null != req.getAdvertiserId() && 0L != req.getAdvertiserId(), AdCustomer::getAdvertiserId, req.getAdvertiserId())
                        .like(StrUtil.isNotBlank(req.getAdvertiserName()), AdCustomer::getAdvertiserName, req.getAdvertiserName())
                        .gt(null != req.getCreatedStart(), AdCustomer::getCreatedAt, req.getCreatedStart())
                        .lt(null != req.getCreatedEnd(), AdCustomer::getCreatedAt, req.getCreatedEnd())
        );
    }

    @Override
    public List<AdCustomer> dropList(AdCustomerQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("created_at", false)));
        return baseMapper.selectList(
                new QueryWrapper<AdCustomer>().lambda()
                        .eq(null != req.getAdvertiserId(), AdCustomer::getAdvertiserId, req.getAdvertiserId())
                        .like(StrUtil.isNotBlank(req.getAdvertiserName()), AdCustomer::getAdvertiserName, req.getAdvertiserName())
        );
    }

    @Override
    public Result selectById(Long advertiserId) {
        AdCustomer adCustomer = baseMapper.selectById(advertiserId);
        if (adCustomer != null) {
            return Result.success().setData(adCustomer);
        } else {
            return Result.error("获取失败");
        }
    }

    @Override
    public Result selectBatchIds(List<Long> ids) {
        List<AdCustomer> adCustomerList = baseMapper.selectBatchIds(ids);
        if (adCustomerList != null) {
            return Result.success().setData(adCustomerList);
        } else {
            return Result.error("获取失败");
        }
    }

    @Override
    public void syncAdCustomerList() {
        List<String> ACCOUNT_IDS = OceanEngineConstant.ACCOUNT_IDS;
        ACCOUNT_IDS.forEach(accountId -> {
            Collection<OECustomCenterAdListResponse.Advertiser> allAdvertisers = oceanEngineManager.getAdvertiserList(accountId);
            if (allAdvertisers.isEmpty()) return;
            List<AdCustomer> adCustomers = allAdvertisers.stream()
                    .map(advertiser -> convertToAdCustomer(advertiser, accountId))
                    .collect(Collectors.toList());
            this.saveOrUpdateBatch(adCustomers);
        });
    }

    @Scheduled(cron = "0 0 2 * * ?")
    public void syncAdCustomerListEveryDay() {
        this.syncAdCustomerList();
    }

    private AdCustomer convertToAdCustomer(OECustomCenterAdListResponse.Advertiser advertiser, String ccAccountId) {
        AdCustomer adCustomer = new AdCustomer();
        adCustomer.setCcAccountId(ccAccountId);
        adCustomer.setAdvertiserId(advertiser.getAdvertiser_id());
        adCustomer.setAdvertiserName(advertiser.getAdvertiser_name());
        adCustomer.setAdvertiserType(advertiser.getAdvertiser_type());
        return adCustomer;
    }

}
