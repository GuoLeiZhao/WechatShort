package com.sqx.modules.ad.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.base.BasePagingRequest;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.dao.AdLinkDao;
import com.sqx.modules.ad.entity.*;
import com.sqx.modules.ad.enums.ReturnType;
import com.sqx.modules.ad.request.AdLinkListReq;
import com.sqx.modules.ad.request.AdLinkQueryReq;
import com.sqx.modules.ad.request.AdMaEcpmListReq;
import com.sqx.modules.ad.response.AdLinkQueryRes;
import com.sqx.modules.ad.service.*;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.douyin.request.AdWebhookRequest;
import com.sqx.modules.douyin.request.ConversionRequest;
import com.sqx.modules.douyin.response.AdWebhookResponse;
import com.sqx.modules.douyin.service.DouyinOcAdService;
import com.sqx.modules.douyin.service.DouyinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdLInkServiceImpl extends ServiceImpl<AdLinkDao, AdLink> implements AdLinkService {

    private final AdPlayService adPlayService;
    private final AdPlaySeriesService adPlaySeriesService;
    private final AdAppService adAppService;
    private final AdCustomerService adCustomerService;
    private final DouyinService douyinService;
    private final AdMaEcpmService adMaEcpmService;
    private final DouyinOcAdService douyinOcAdService;
    private final AdEventHistoryService adEventHistoryService;
    private final UserService userService;

    @Override
    public Result insert(AdLink adLink) {
        if (StrUtil.isBlank(adLink.getLinkId())) {
            adLink.setLinkId(IdUtil.simpleUUID());
        }
        int rows = baseMapper.insert(adLink);
        if (rows > 0) {
            return Result.success("添加成功！");
        } else {
            return Result.error("添加失败");
        }
    }

    @Override
    public List<AdLink> selectList(AdLinkListReq req) {
        List<AdLink> adLinkList = baseMapper.selectList(new QueryWrapper<AdLink>().lambda()
                .eq(StrUtil.isNotBlank(req.getLinkId()), AdLink::getLinkId, req.getLinkId())
                .eq(req.getReturnType().getValue() != null, AdLink::getReturnType, req.getReturnType())
                .eq(req.getReturnCycle().getValue() != null, AdLink::getReturnCycle, req.getReturnCycle())
                .eq(req.getDeleted() != null, AdLink::getDeleted, req.getDeleted())
        );
        return adLinkList;
    }

    @Override
    public IPage<AdLinkQueryRes> page(AdLinkQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("created_at", false)));
        IPage<AdLink> adLinkIPage = baseMapper.selectPage(
                new Query<AdLink>().getPage(req),
                new QueryWrapper<AdLink>().lambda()
                        .eq(StrUtil.isNotBlank(req.getLinkId()), AdLink::getLinkId, req.getLinkId())
                        .nested(wrapper ->
                            wrapper.eq(StrUtil.isNotBlank(req.getUserId()), AdLink::getUserId, req.getUserId())
                                .or()
                                .isNull(AdLink::getUserId)
                        )
                        .like(StrUtil.isNotBlank(req.getLinkName()), AdLink::getLinkName, req.getLinkName())
                        .like(StrUtil.isNotBlank(req.getAdvertiserID()), AdLink::getAdvertiserId, req.getAdvertiserID())
                        .gt(StrUtil.isNotBlank(req.getCreatedStart()), AdLink::getCreatedAt, req.getCreatedStart())
                        .lt(StrUtil.isNotBlank(req.getCreatedEnd()), AdLink::getCreatedAt, req.getCreatedEnd())
        );
        if (adLinkIPage.getRecords().isEmpty()) {
            return new Page<>();
        }

        // 提取所有需要查询的 ID
        List<Long> advertiserIds = adLinkIPage.getRecords().stream().map(AdLink::getAdvertiserId).collect(Collectors.toList());
        List<String> albumIds = adLinkIPage.getRecords().stream().map(AdLink::getAlbumId).collect(Collectors.toList());
        List<String> episodeIds = adLinkIPage.getRecords().stream().map(AdLink::getEpisodeId).collect(Collectors.toList());
        List<String> appIds = adLinkIPage.getRecords().stream().map(AdLink::getAppId).collect(Collectors.toList());

        // 批量查询相关数据
        Map<Long, AdCustomer> advertiserMap = batchQueryAdCustomer(advertiserIds);
        Map<String, AdPlay> adPlayMap = batchQueryAdPlay(albumIds);
        Map<String, AdPlaySeries> adPlaySeriesMap = batchQueryAdPlaySeries(episodeIds);
        Map<String, AdApp> adAppMap = batchQueryAdApp(appIds);

        // 转换数据
        IPage<AdLinkQueryRes> adLinkQueryResIPage = adLinkIPage.convert(adLink -> {
            AdLinkQueryRes adLinkQueryRes = new AdLinkQueryRes();
            BeanUtil.copyProperties(adLink, adLinkQueryRes);

            // 设置广告主名称
            adLinkQueryRes.setAdvertiserName(getAdvertiserName(adLink.getAdvertiserId(), advertiserMap));

            // 设置专辑名称
            adLinkQueryRes.setAlbumName(getAlbumName(adLink.getAlbumId(), adPlayMap));

            // 设置集数名称
            adLinkQueryRes.setEpisodeName(getEpisodeName(adLink.getEpisodeId(), adPlaySeriesMap));

            // 设置应用名称
            adLinkQueryRes.setAppName(getAppName(adLink.getAppId(), adAppMap));

            return adLinkQueryRes;
        });

        return adLinkQueryResIPage;
    }

    @Override
    public Result update(AdLink adLink) {
        QueryWrapper<AdLink> whereWrapper = new QueryWrapper<>();
        whereWrapper.eq("link_id", adLink.getLinkId());
        int rows = baseMapper.update(adLink, whereWrapper);
        if (rows > 0) {
            return Result.success("更新成功！");
        } else {
            return Result.error("更新失败");
        }
    }

    @Override
    public AdLink selectOneByLinkId(String linkId) {
        return baseMapper.selectById(linkId);
    }

    @Override
    public void adWebhook(AdWebhookRequest adWebhookRequest) {
        AdWebhookResponse adWebhookResponse = douyinService.adWebhook(adWebhookRequest);
        List<AdWebhookResponse.ContentItem> contentItemList = adWebhookResponse.getContentItems();
        if (ArrayUtil.isNotEmpty(contentItemList)) {
            for (AdWebhookResponse.ContentItem contentItem : contentItemList) {
                AdMaEcpm adMaEcpm = new AdMaEcpm();
                adMaEcpm.setLogId(adWebhookRequest.getLogId());
                adMaEcpm.setEvent(adWebhookRequest.getEvent());
                adMaEcpm.setClientKey(adWebhookRequest.getClientKey());
                adMaEcpm.setFromUserId(adWebhookRequest.getFromUserId());
                adMaEcpm.setContentId(contentItem.getId());
                adMaEcpm.setMpId(contentItem.getMpId());
                adMaEcpm.setCost(contentItem.getCost());
                adMaEcpm.setOpenId(contentItem.getOpenId());
                long timestamp = Long.parseLong(contentItem.getEventTime());
                adMaEcpm.setEventTime(DateUtil.date(timestamp * 1000));
                adMaEcpm.setReqId(contentItem.getReqId());
                adMaEcpm.setAdType(contentItem.getAdType());
                adMaEcpm.setIsHandle(0);
                // 通过openId获取到linkId
                UserEntity userEntity = userService.queryByOpenId(adMaEcpm.getOpenId());
                String clickId = null;
                if (userEntity != null) {
                    adMaEcpm.setLinkId(userEntity.getTtLinkId());
                    clickId = userEntity.getTtClickId();
                } else {
                    log.info("幽灵用户:{}", adMaEcpm);
                }
                adMaEcpmService.insert(adMaEcpm);
                uploadData(adMaEcpm, clickId); // 上报数据
            }
        }
    }

    @Override
    public List<AdCustomer> getAllAdCustomer() {
        QueryWrapper<AdLink> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT(advertiser_id)");
        List<AdLink> adLinkList = baseMapper.selectList(queryWrapper);
        if (adLinkList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> advertiserIds = adLinkList.stream().map(AdLink::getAdvertiserId).collect(Collectors.toList());
        Result result = adCustomerService.selectBatchIds(advertiserIds);
        if (result.get("code").equals(0)) {
            return (List<AdCustomer>) result.get("data");
        }
        return new ArrayList<>();
    }

    private Map<Long, AdCustomer> batchQueryAdCustomer(List<Long> ids) {
        // 批量查询广告主
        Result result = adCustomerService.selectBatchIds(ids);
        if (result.get("code").equals(0)) {
            List<AdCustomer> customers = (List<AdCustomer>) result.get("data");
            return customers.stream().collect(Collectors.toMap(AdCustomer::getAdvertiserId, customer -> customer));
        }
        return Collections.emptyMap();
    }

    private Map<String, AdPlay> batchQueryAdPlay(List<String> ids) {
        // 批量查询专辑
        Result result = adPlayService.selectBatchIds(ids);
        if (result.get("code").equals(0)) {
            List<AdPlay> adPlays = (List<AdPlay>) result.get("data");
            return adPlays.stream().collect(Collectors.toMap(AdPlay::getPlayId, adPlay -> adPlay));
        }
        return Collections.emptyMap();
    }

    private Map<String, AdPlaySeries> batchQueryAdPlaySeries(List<String> ids) {
        // 批量查询剧集
        Result result = adPlaySeriesService.selectBatchIds(ids);
        if (result.get("code").equals(0)) {
            List<AdPlaySeries> adPlaySeries = (List<AdPlaySeries>) result.get("data");
            return adPlaySeries.stream().collect(Collectors.toMap(AdPlaySeries::getSeriesId, adPlaySeries1 -> adPlaySeries1));
        }
        return Collections.emptyMap();
    }

    private Map<String, AdApp> batchQueryAdApp(List<String> ids) {
        // 批量查询应用
        Result result = adAppService.selectBatchIds(ids);
        if (result.get("code").equals(0)) {
            List<AdApp> adApps = (List<AdApp>) result.get("data");
            return adApps.stream().collect(Collectors.toMap(AdApp::getAppId, adApp -> adApp));
        }
        return Collections.emptyMap();
    }

    private String getAdvertiserName(Long id, Map<Long, AdCustomer> advertiserMap) {
        AdCustomer customer = advertiserMap.get(id);
        return customer != null ? customer.getAdvertiserName() : "";
    }

    private String getAlbumName(String id, Map<String, AdPlay> adPlayMap) {
        AdPlay adPlay = adPlayMap.get(id);
        return adPlay != null ? adPlay.getPlayName() : "";
    }

    private String getEpisodeName(String id, Map<String, AdPlaySeries> adPlaySeriesMap) {
        AdPlaySeries adPlaySeries = adPlaySeriesMap.get(id);
        return adPlaySeries != null ? adPlaySeries.getSeriesTitle() : "";
    }

    private String getAppName(String id, Map<String, AdApp> adAppMap) {
        AdApp adApp = adAppMap.get(id);
        return adApp != null ? adApp.getAppName() : "";
    }

    private void uploadData(AdMaEcpm adMaEcpm, String clickId) {
        AdLink adLink = selectOneByLinkId(adMaEcpm.getLinkId());
        if (adLink != null) {
            ReturnType returnType = adLink.getReturnType();
            if (returnType.equals(ReturnType.LT_ROI)) {
                uploadUserRightStatement(adMaEcpm, returnType, clickId);
            } else if (returnType.equals(ReturnType.GAME_ADDICTION)) {
                uploadGameAddData(adMaEcpm, adLink, clickId);
            }
        } else {
            log.info("uploadData error: {}", "linkId is not found");
        }
    }

    private void uploadUserRightStatement(AdMaEcpm adMaEcpm, ReturnType returnType, String clickId) {
        String openId = adMaEcpm.getOpenId();
        String cost = adMaEcpm.getCost();
        String linkId = adMaEcpm.getLinkId();
        String eventType = returnType.getValue().toLowerCase();

        // 如果是关键行为，查询是否已经上传过。
//        if (returnType.equals(ReturnType.GAME_ADDICTION)) {
//            AdEventHistory adEventHistoryReq2 = new AdEventHistory();
//            adEventHistoryReq2.setOpenId(openId);
//            adEventHistoryReq2.setEventType(eventType);
//            AdEventHistory adEventHistory2 = adEventHistoryService.getAdEventHistoryOne(adEventHistoryReq2);
//            if (adEventHistory2 != null) {
//                return;
//            }
//        }

        AdLink adLinkRes = baseMapper.selectById(linkId);
        if (adLinkRes == null) {
            return;
        }

        ReturnType linkReturnType = adLinkRes.getReturnType();
        if (linkReturnType != returnType) {
            return;
        }

        // 构建并上传 ConversionRequest
        ConversionRequest conversionRequest = buildConversionRequest(eventType, cost, clickId, returnType);
        if (conversionRequest != null) {
            douyinOcAdService.uploadConversion(conversionRequest);

            AdEventHistory adEventHistory = new AdEventHistory();
            adEventHistory.setAppId(adLinkRes.getAppId());
            adEventHistory.setOpenId(openId);
            adEventHistory.setEventType(eventType);
            adEventHistory.setLinkId(linkId);
            adEventHistory.setAlbumId(adLinkRes.getAlbumId());
            adEventHistory.setEpisodeId(adLinkRes.getEpisodeId());
            adEventHistory.setCreatedAt(new Date());
            adEventHistory.setModifiedAt(new Date());
            adEventHistoryService.insert(adEventHistory);

            updateAdMaEcpm(adMaEcpm);
        }
    }

    /**
     * 构建 ConversionRequest 对象
     */
    private ConversionRequest buildConversionRequest(String eventType, String cost, String clickId, ReturnType returnType) {
        ConversionRequest conversionRequest = new ConversionRequest();
        conversionRequest.setEvent_type(eventType);
        conversionRequest.setTimestamp(System.currentTimeMillis());

        ConversionRequest.Context context = new ConversionRequest.Context();
        ConversionRequest.Ad ad = new ConversionRequest.Ad();
        ad.setCallback(clickId);
        context.setAd(ad);
        conversionRequest.setContext(context);

        // 处理 LT_ROI 类型的 eventWeight
        if (returnType == ReturnType.LT_ROI) {
            try {
                long costMicro = Long.parseLong(cost);
                double eventWeight = costMicro * 0.00001;
                conversionRequest.setEvent_weight(eventWeight);
            } catch (NumberFormatException e) {
                log.error("Cost is not a valid number: {}", cost);
                return null; // 解析失败时返回 null，避免上传错误数据
            }
        }

        return conversionRequest;
    }

    /**
     * 更新 AdMaEcpm 的处理状态
     */
    private void updateAdMaEcpm(AdMaEcpm adMaEcpm) {
        AdMaEcpm adMaEcpmUpdate = new AdMaEcpm();
        adMaEcpmUpdate.setId(adMaEcpm.getId());
        adMaEcpmUpdate.setIsHandle(1);
        adMaEcpmService.update(adMaEcpmUpdate);
    }

    // 判断是否通过的方法（rate为0-100）
    private boolean isPassCal(BigDecimal rate) {
        if (rate == null) {
            return true;
        }
        // 确保 rate 在 [0, 100] 范围内
        BigDecimal clampedRate = rate.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO
                : rate.compareTo(new BigDecimal(100)) > 0 ? new BigDecimal(100)
                : rate;
        // 转换为 0~1 的小数概率（保留4位小数避免精度丢失）
        BigDecimal probability = clampedRate.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
        double passRate = probability.doubleValue();
        // 生成随机数判断是否通过
        return ThreadLocalRandom.current().nextDouble() < passRate;
    }

    /**
     * 关键行为上报
     */
    private void uploadGameAddData(AdMaEcpm adMaEcpm, AdLink adLink, String clickId) {
        BigDecimal rate = adLink.getRate();
        boolean isPass = isPassCal(rate);
        if (!isPass) {
            return;
        }

        // 获取到该链接下一小时内的ecpm记录
        String openId = adMaEcpm.getOpenId();
        String linkId = adLink.getLinkId();

        AdMaEcpmListReq adMaEcpmListReq = new AdMaEcpmListReq();
        adMaEcpmListReq.setLinkId(linkId);
//        adMaEcpmListReq.setIsHandle(0);
        adMaEcpmListReq.setOpenId(openId);
        adMaEcpmListReq.setCreatedStart(DateUtil.offsetHour(new Date(), -1));
        List<AdMaEcpm> userEcpmList = adMaEcpmService.selectList(adMaEcpmListReq);

        int ipu = userEcpmList.size(); // IPU 为该用户的记录数量
        BigDecimal ipuThreshold = adLink.getIpu(); // IPU 阈值
        BigDecimal ecpmArpuThreshold = adLink.getEcpmArpu(); // ECPM 或 ARPU 阈值

        switch (adLink.getReturnSetting()) {
            case IPU_ECPM_HIGH:
                log.info("IPU_ECPM_HIGH");
                // 假设ipu为3 ecpm设置300
                // 则用户需满足ipu大于等于3且其中有一次的ecpm大于300，ecpm=cost/100
                boolean hasHighEcpm = userEcpmList.stream().anyMatch(ecpm -> {
                    BigDecimal cost = new BigDecimal(ecpm.getCost());
                    BigDecimal costTimes100 = cost.divide(BigDecimal.valueOf(100));
                    return costTimes100.compareTo(ecpmArpuThreshold) >= 0;
                });
                if (new BigDecimal(ipu).compareTo(ipuThreshold) >= 0 && hasHighEcpm) {
                    log.info("User {} meets IPU_ECPM_HIGH condition for link {}", openId, linkId);
                    // 添加回传逻辑
                    uploadUserRightStatement(adMaEcpm, adLink.getReturnType(), clickId);
                }
                break;
            case IPU_ECPM_FILTER:
                log.info("IPU_ECPM_FILTER");
                // 假设ipu为3 ecpm设置300
                // 则用户需满足ipu大于等于3且其中每一次的ecpm大于300，ecpm=cost/100
                boolean allHighEcpm = userEcpmList.stream().allMatch(ecpm -> {
                    BigDecimal cost = new BigDecimal(ecpm.getCost());
                    BigDecimal costTimes100 = cost.divide(BigDecimal.valueOf(100));
                    return costTimes100.compareTo(ecpmArpuThreshold) >= 0;
                });
                if (new BigDecimal(ipu).compareTo(ipuThreshold) >= 0 && allHighEcpm) {
                    log.info("User {} meets IPU_ECPM_FILTER condition for link {}", openId, linkId);
                    // 添加回传逻辑
                    uploadUserRightStatement(adMaEcpm, adLink.getReturnType(), clickId);
                }
                break;
            case IPU_ARPU:
                // 假设ipu为3 arpu设置0.7
                // 则用户需满足ipu大于等于3且用户总的ecpm大于700，ecpm=cost/100 arpu=ecpm/1000=cost/100000
                log.info("IPU_ARPU");
                BigDecimal totalArpu = userEcpmList.stream()
                        .map(arpu -> new BigDecimal(arpu.getCost()))
                        .map(cost -> cost.divide(BigDecimal.valueOf(100000)))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (new BigDecimal(ipu).compareTo(ipuThreshold) >= 0 && totalArpu.compareTo(ecpmArpuThreshold) >= 0) {
                    log.info("User {} meets IPU_ARPU condition for link {}", openId, linkId);
                    // 添加回传逻辑
                    uploadUserRightStatement(adMaEcpm, adLink.getReturnType(), clickId);
                }

                break;
        }

    }

    @Override
    public AdLink selectOneByAdvertiserId(String advertiserId) {
        LambdaQueryWrapper<AdLink> adLink = new QueryWrapper<AdLink>().lambda()
                .eq(AdLink::getAdvertiserId, advertiserId);
        return baseMapper.selectOne(adLink);
    }
}
