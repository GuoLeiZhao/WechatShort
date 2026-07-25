package com.sqx.modules.ad.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.util.concurrent.RateLimiter;
import com.sqx.common.base.BasePagingRequest;
import com.sqx.common.utils.Query;
import com.sqx.modules.ad.dao.AdStaticsDao;
import com.sqx.modules.ad.entity.AdCustomer;
import com.sqx.modules.ad.entity.AdStatics;
import com.sqx.modules.ad.enums.TimeGranularityType;
import com.sqx.modules.ad.request.AdCustomerQueryReq;
import com.sqx.modules.ad.request.AdStaticsHourReq;
import com.sqx.modules.ad.request.AdStaticsQueryReq;
import com.sqx.modules.ad.request.AdStaticsReq;
import com.sqx.modules.ad.response.AdStaticsRes;
import com.sqx.modules.ad.service.AdCustomerService;
import com.sqx.modules.ad.service.AdLinkService;
import com.sqx.modules.ad.service.AdStaticsService;
import com.sqx.modules.place.request.oceanengine.ReportRequest;
import com.sqx.modules.place.response.oceanengine.OEAdvertiserFundResponse;
import com.sqx.modules.place.response.oceanengine.OEAdvertiserReportResponse;
import com.sqx.modules.place.service.manager.OceanEngineManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdStaticsServiceImpl extends ServiceImpl<AdStaticsDao, AdStatics> implements AdStaticsService {

    private final OceanEngineManager oceanEngineManager;
    private final AdCustomerService adCustomerService;
    private final AdLinkService adLinkService;

    // 同时同步按天和按时数据，qps减半
    // 使用Guava的RateLimiter控制QPS=50
    private static final RateLimiter rateLimiter = RateLimiter.create(15.0);
    // 配置线程池（核心线程数50，最大线程数50，确保与QPS对齐）
    private final ExecutorService reportExecutor = Executors.newFixedThreadPool(15);

    @Override
    public IPage<AdStatics> page(AdStaticsQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("payment", false)));
        IPage<AdStatics> adStaticsIPage = baseMapper.selectPage(
                new Query<AdStatics>().getPage(req),
                new QueryWrapper<AdStatics>().lambda()
                        .ge(AdStatics::getStatDatetime, req.getStartDate())
                        .lt(AdStatics::getStatDatetime, req.getEndDate())
                        .eq(req.getTimeGranularity() != null, AdStatics::getTimeGranularity, req.getTimeGranularity())
                        .in(req.getAdvertiserIds() != null, AdStatics::getAdvertiserId, req.getAdvertiserIds())
        );
        if (adStaticsIPage.getRecords().isEmpty()) {
            return new Page<>();
        }
        List<Long> advertiserIds = adStaticsIPage.getRecords().stream().map(AdStatics::getAdvertiserId).collect(Collectors.toList());

        // 批量查询相关数据
        Map<Long, OEAdvertiserFundResponse.BalanceData> balanceDataMap = batchQueryAdFund(advertiserIds);

        // 转换数据
        IPage<AdStatics> adStaticsQueryResIPage=  adStaticsIPage.convert(item -> {
            AdStatics adStatics = new AdStatics();
            BeanUtil.copyProperties(item, adStatics);

            // 设置余额
            adStatics.setBalance(getAdvertiserBalance(item.getAdvertiserId(), balanceDataMap));

            return adStatics;
        });

        return adStaticsQueryResIPage;

    }


    private Map<Long, OEAdvertiserFundResponse.BalanceData> batchQueryAdFund(List<Long> advertiserIds) {
        List<OEAdvertiserFundResponse.BalanceData> oeAdvertiserFundResponseList = oceanEngineManager.getAdvertiserFund(advertiserIds);

        log.info("oeAdvertiserFundResponseList: {}", oeAdvertiserFundResponseList);
        if (CollUtil.isNotEmpty(oeAdvertiserFundResponseList)) {
            return oeAdvertiserFundResponseList.stream().collect(Collectors.toMap(OEAdvertiserFundResponse.BalanceData::getAccount_id, balanceData -> balanceData));
        }
        return Collections.emptyMap();
    }

    private Float getAdvertiserBalance(Long id, Map<Long, OEAdvertiserFundResponse.BalanceData> balanceDataMap) {
        OEAdvertiserFundResponse.BalanceData balanceData = balanceDataMap.get(id);
        return balanceData != null ? balanceData.getBalance() : 0L;
    }

    @Override
    public AdStaticsRes getInfo(AdStaticsReq req) {
        QueryWrapper<AdStatics> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("SUM(payment) AS sumPayment, SUM(cost) AS sumCost");
        queryWrapper.ge("stat_datetime", req.getStartDate());
        queryWrapper.lt("stat_datetime", req.getEndDate());
        queryWrapper.eq(req.getTimeGranularity() != null, "time_granularity", req.getTimeGranularity());
        queryWrapper.in(req.getAdvertiserIds() != null, "advertiser_id", req.getAdvertiserIds());
        AdStatics adStatics = baseMapper.selectOne(queryWrapper);
        log.info("adStatics: {}", adStatics);
        AdStaticsRes adStaticsRes = getAdStaticsRes(adStatics);
        return adStaticsRes;
    }

    private static AdStaticsRes getAdStaticsRes(AdStatics adStatics) {
        AdStaticsRes adStaticsRes = new AdStaticsRes();
        float defaultZero = 0f;
        if (adStatics == null) {
            adStaticsRes.setAllPayments(defaultZero);
            adStaticsRes.setAllCost(defaultZero);
            adStaticsRes.setAllRoi(defaultZero);
        } else {
            adStaticsRes.setAllPayments(adStatics.getSumPayment());
            adStaticsRes.setAllCost(adStatics.getSumCost());
            float roi = adStatics.getSumPayment() == 0 ? defaultZero :
                (float)(Math.round(adStatics.getSumCost() / adStatics.getSumPayment() * 100) / 100.0);
            adStaticsRes.setAllRoi(roi);
        }
        return adStaticsRes;
    }

    @Override
    public List<AdStatics> getHourData(AdStaticsHourReq req) {
        List<AdStatics> resultList = new ArrayList<>();

        // 创建日期时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 解析请求中的起止时间
        LocalDateTime start = LocalDateTime.parse(req.getStartDate(), formatter);
        LocalDateTime end = LocalDateTime.parse(req.getEndDate(), formatter);

        // 按小时循环查询
        LocalDateTime currentHour = start;
        while (currentHour.isBefore(end)) {
            // 生成当前小时的结束时间
            LocalDateTime nextHour = currentHour.plusHours(1);

            // 创建新的查询条件（每次循环新建避免条件叠加）
            QueryWrapper<AdStatics> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("SUM(payment) AS sumPayment, SUM(cost) AS sumCost");
            queryWrapper.ge("stat_datetime", currentHour.format(formatter));
            queryWrapper.lt("stat_datetime", nextHour.format(formatter));
            queryWrapper.eq("time_granularity", TimeGranularityType.STAT_TIME_GRANULARITY_HOURLY.getValue());
            queryWrapper.in(req.getAdvertiserIds() != null, "advertiser_id", req.getAdvertiserIds());

            // 执行查询
            AdStatics hourData = baseMapper.selectOne(queryWrapper);
            if (hourData != null) {
                // 可选：设置小时标识
                hourData.setHourTime(currentHour.format(DateTimeFormatter.ofPattern("HH")));
                resultList.add(hourData);
            }

            // 移动到下一小时
            currentHour = nextHour;
        }

        return resultList;
    }

//    @PostConstruct
//    public void init() throws InterruptedException {
//        syncAdvertiserDayReports();  // 启动时立即执行一次
//    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void syncAdvertiserDayReports() throws InterruptedException {
        String date = LocalDate.now(ZoneId.of("Asia/Shanghai"))
                .format(DateTimeFormatter.ISO_LOCAL_DATE); // 根据需求调整日期逻辑

        List<AdCustomer> adCustomerList = adLinkService.getAllAdCustomer();

        // 使用CountDownLatch等待所有请求完成
        CountDownLatch latch = new CountDownLatch(adCustomerList.size());

        // 提交所有任务到线程池
        for (AdCustomer advertiser : adCustomerList) {
            reportExecutor.submit(() -> {
                try {
                    rateLimiter.acquire(); // 控制QPS

                    String token = oceanEngineManager.getToken();

                    ReportRequest reportRequest = new ReportRequest();
                    reportRequest.setAdvertiserId(advertiser.getAdvertiserId());
                    reportRequest.setDate(date);
                    reportRequest.setPageSize(30);

                    // 同步按时统计数据
                    reportRequest.setTimeGranularity(TimeGranularityType.STAT_TIME_GRANULARITY_HOURLY);
                    postAdStatics(reportRequest, advertiser, token);

                    // 同步按天统计数据
                    reportRequest.setTimeGranularity(TimeGranularityType.STAT_TIME_GRANULARITY_DAILY);
                    postAdStatics(reportRequest, advertiser, token);

                    log.info("Report fetched for advertiserId: {}", advertiser.getAdvertiserId());
                } catch (Exception e) {
                    log.error("Failed to fetch report for advertiserId: {}", advertiser.getAdvertiserId(), e);
                } finally {
                    latch.countDown();
                }
            });
        }

        // 等待当前批次全部完成（最多等待4.5分钟，防止影响下次任务）
        latch.await(270, TimeUnit.SECONDS);
    }

    private void postAdStatics(ReportRequest reportRequest, AdCustomer advertiser, String token) {
        try {
            List<OEAdvertiserReportResponse.Report> reports = oceanEngineManager.getAdvertiserDayReport(reportRequest, token);
            if (CollUtil.isEmpty(reports)) return;

            deleteDayReports(reportRequest.getDate(), advertiser.getAdvertiserId(), reportRequest.getTimeGranularity());
            for (OEAdvertiserReportResponse.Report report : reports) {
                AdStatics adStatics = getAdStatics(advertiser, report);
                adStatics.setTimeGranularity(reportRequest.getTimeGranularity());
                baseMapper.insert(adStatics);
            }
        } catch (Exception e) {
            log.error("postAdStatics", e);
        }
    }

    private static AdStatics getAdStatics(AdCustomer advertiser, OEAdvertiserReportResponse.Report report) {
        AdStatics adStatics = new AdStatics();
        adStatics.setAdvertiserId(report.getAdvertiser_id());
        adStatics.setStatDatetime(report.getStat_datetime());
        adStatics.setAdvertiserName(advertiser.getAdvertiserName());
        adStatics.setLinkId("");
        adStatics.setBalance((float) 0); // TODO: 从数据库或其他数据源获取
        adStatics.setPayment(report.getCost());
        adStatics.setCost(report.getAttribution_micro_game_0d_ltv());
        adStatics.setRoi(report.getAttribution_micro_game_0d_roi());
        adStatics.setAdShow(report.getShow());
        adStatics.setCostPerThousandShow(report.getAvg_show_cost()); // 平均千次展现费用
        adStatics.setClick(report.getClick());
        adStatics.setClickRate(report.getCtr());
        adStatics.setCostPerClick(report.getAvg_click_cost()); // 平均点击单价
        adStatics.setJumpCount(report.getConvert());
        adStatics.setJumpRate(report.getConvert_rate());
        adStatics.setLaunchCount(0L); // TODO  成功拉起广告的用户数
        adStatics.setLaunchRate(report.getDeep_convert_rate()); // TODO 拉起数/跳转数
        adStatics.setLaunchCost((float) 0); // TODO 拉起成本 拉起数/跳转数
        adStatics.setArpu((float) 0); // TODO 回收/拉起数
        adStatics.setIpu((float) 0); // TODO 当日总广告次数/拉起数
        adStatics.setAvgEcpm((float) 0); // TODO 回收/拉起数/ipu*1000
        return adStatics;
    }

    private boolean deleteDayReports(String date, Long advertiserId, TimeGranularityType timeGranularity) {
        QueryWrapper<AdStatics> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("stat_datetime", date);
        queryWrapper.eq("advertiser_id", advertiserId);
        queryWrapper.eq("time_granularity", timeGranularity.getValue());
        int rows = baseMapper.delete(queryWrapper);
        return rows > 0;
    }
}
