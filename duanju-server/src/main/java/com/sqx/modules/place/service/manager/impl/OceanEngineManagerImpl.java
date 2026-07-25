package com.sqx.modules.place.service.manager.impl;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.bytedance.ads.ApiClient;
import com.bytedance.ads.model.ReportAdvertiserGetV2Response;
import com.sqx.common.constant.OceanEngineConstant;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.RedisUtils;
import com.sqx.modules.ad.enums.TimeGranularityType;
import com.sqx.modules.place.request.oceanengine.AccessTokenRequest;
import com.sqx.modules.place.request.oceanengine.CallbackRequest;
import com.sqx.modules.place.request.oceanengine.RefreshTokenRequest;
import com.sqx.modules.place.request.oceanengine.ReportRequest;
import com.sqx.modules.place.response.oceanengine.*;
import com.sqx.modules.place.service.manager.OceanEngineManager;
import com.sqx.modules.utils.ID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OceanEngineManagerImpl implements OceanEngineManager {

    public static final String AD_BASE_API = "https://ad.oceanengine.com";
    public static final String API_BASE_API = "https://api.oceanengine.com";
    private final RedisUtils redisUtils;

    @Override
    public String getToken() {

        if (redisUtils.hasKey(OceanEngineConstant.ACCESS_TOKEN_KEY)) {
            return redisUtils.get(OceanEngineConstant.ACCESS_TOKEN_KEY);
        }
        String lockValue = ID.get();
        if (!redisUtils.lock(OceanEngineConstant.ACCESS_TOKEN_LOCK, lockValue, 60)) {
            return redisUtils.get(OceanEngineConstant.ACCESS_TOKEN_KEY);
        }

        // DCL
        if (redisUtils.hasKey(OceanEngineConstant.ACCESS_TOKEN_KEY)) {
            redisUtils.unlock(OceanEngineConstant.ACCESS_TOKEN_LOCK, lockValue);
            return redisUtils.get(OceanEngineConstant.ACCESS_TOKEN_KEY);
        }

        String authCode = redisUtils.get(OceanEngineConstant.AUTH_CODE_KEY);
        String refreshToken = redisUtils.get(OceanEngineConstant.REFRESH_TOKEN_KEY);

        HttpRequest post = HttpUtil.createPost(StrUtil.isNotBlank(refreshToken) ? API_BASE_API + "/open_api/oauth2/refresh_token/" : AD_BASE_API + "/open_api/oauth2/access_token/");
        post.contentType(ContentType.JSON.getValue());

        SerializeConfig serializeConfig = new SerializeConfig();
        // 驼峰转下划线
        serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
        post.body(JSON.toJSONString(
                StrUtil.isNotBlank(refreshToken) ? new RefreshTokenRequest(OceanEngineConstant.APP_ID, OceanEngineConstant.APP_SECRET, refreshToken) : new AccessTokenRequest(OceanEngineConstant.APP_ID, OceanEngineConstant.APP_SECRET, authCode)
                , serializeConfig));

        try (HttpResponse httpResponse = post.execute()) {
            if (200 != httpResponse.getStatus()) {
                log.info("request: " + post);
                log.info("response: " + httpResponse);
                throw new SqxException("获取 authToken 接口错误，请查看日志并检查配置！");
            }
            OceanEngineBaseResponse<OEAuthTokenResponse> response = JSONObject.parseObject(httpResponse.body(), new TypeReference<OceanEngineBaseResponse<OEAuthTokenResponse>>() {
            });
            if (0 != response.getCode()) {
                if (StrUtil.isNotBlank(refreshToken)) {
                    redisUtils.delete(OceanEngineConstant.REFRESH_TOKEN_KEY);
                }
                log.info("获取 authToken 接口错误，code: {}, message {}, requestId {}", response.getCode(), response.getMessage(), response.getRequestId());
                return null;
            }
            OEAuthTokenResponse data = response.getData();
            redisUtils.set(OceanEngineConstant.ACCESS_TOKEN_KEY, data.getAccessToken(), data.getExpiresIn());
            redisUtils.set(OceanEngineConstant.REFRESH_TOKEN_KEY, data.getRefreshToken(), data.getRefreshTokenExpiresIn());
            return data.getAccessToken();
        } finally {
            redisUtils.unlock(OceanEngineConstant.ACCESS_TOKEN_LOCK, lockValue);
        }
    }

    @Override
    public ReportAdvertiserGetV2Response reportAdvertiserGet() {
//        ReportAdvertiserGetV2Api reportAdvertiserGetV2Api = new ReportAdvertiserGetV2Api(initApiClient(AD_BASE_API));
//        ReportAdvertiserGetV2Response reportAdvertiserGetV2Response = reportAdvertiserGetV2Api.openApi2ReportAdvertiserGetGet();
//        return reportAdvertiserGetV2Response;
        return new ReportAdvertiserGetV2Response();
    }

    // https://open.oceanengine.com/developer/admin/app_info/basic/__OCEANENGINE_APP_ID__
    // https://open.oceanengine.com/audit/oauth.html?app_id=__OCEANENGINE_APP_ID__&state=your_custom_params&material_auth=1&rid=82a3swmschb
    @Override
    public Collection<OECustomCenterAdListResponse.Advertiser> getAdvertiserList(String advertiserId) {
        if (StrUtil.isBlank(advertiserId)) {
            throw new SqxException("advertiserId不能为空");
        }
        String token = getToken();
        String baseUrl = AD_BASE_API + "/open_api/2/customer_center/advertiser/list/";
        Collection<OECustomCenterAdListResponse.Advertiser> allAdvertisers = new ArrayList<>(); // 替换为实际数据类型
        int requestCount = 0;
        int currentPage = 1;
        int totalPage = 1;
        Integer pageSize = 100; // 初始请求每页数量

        do {
            HttpRequest get = HttpUtil.createGet(baseUrl)
                    .header("Access-Token", token)
                    .form("cc_account_id", advertiserId)
                    .form("page", currentPage)
                    .form("page_size", pageSize);

            try (HttpResponse httpResponse = get.execute()) {
                // 响应状态检查
                if (httpResponse.getStatus() != 200) {
                    log.error("Request failed. Status: {} | Request: {} | Response: {}",
                            httpResponse.getStatus(), get, httpResponse);
                    throw new SqxException("API请求失败");
                }

                // 响应解析
                OceanEngineBaseResponse<OECustomCenterAdListResponse> response = JSONObject.parseObject(
                        httpResponse.body(),
                        new TypeReference<OceanEngineBaseResponse<OECustomCenterAdListResponse>>() {
                        }
                );

                // API错误处理
                if (response.getCode() != 0) {
                    log.error("API Error: Code={}, Message={}, RequestId={}",
                            response.getCode(), response.getMessage(), response.getRequestId());
                    throw new SqxException("API返回错误: " + response.getMessage());
                }

                OECustomCenterAdListResponse data = response.getData();
                allAdvertisers.addAll(data.getList());

                // 分页信息处理
                OEPageInfo pageInfo = data.getPage_info();
                if (currentPage == 1) {
                    // 首次请求处理逻辑
                    totalPage = pageInfo.getTotal_page();

                    // 检查实际生效的page_size
                    if (pageInfo.getPage_size() != pageSize) {
                        pageSize = pageInfo.getPage_size();
                        totalPage = (int) Math.ceil((double) pageInfo.getTotal_number() / pageSize);
                        log.info("Page size调整为: {}，总页数更新为: {}", pageSize, totalPage);
                    }
                }

                // 速率控制
                if (++requestCount % 5 == 0) {
                    Thread.sleep(1000); // 每5次请求暂停1秒
                    log.debug("速率控制：已发送{}次请求，暂停1秒", requestCount);
                }

                log.info("账户ID:{} 已获取第{}/{}页数据，本页数量：{}，累计数量：{}",
                        advertiserId, currentPage, totalPage, data.getList().size(), allAdvertisers.size());

                currentPage++;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SqxException("请求被中断", e);
            } catch (Exception e) {
                log.error("分页请求异常: ", e);
                throw new SqxException("数据获取失败", e);
            }
        } while (currentPage <= totalPage);

        log.info("数据拉取完成！账户ID:{} 总计获取: {}条数据", advertiserId, allAdvertisers.size());
        return allAdvertisers;
    }

    @Override
    public void appCallbackGet(CallbackRequest callbackRequest) {
        redisUtils.delete(OceanEngineConstant.ACCESS_TOKEN_KEY);
        redisUtils.delete(OceanEngineConstant.REFRESH_TOKEN_KEY);
        redisUtils.set(OceanEngineConstant.AUTH_CODE_KEY, callbackRequest.getAuth_code(), 600);
        this.getToken();
    }

    /**
     * 获取当日、按小时纬度 的广告主数据 https://open.oceanengine.com/labels/34/docs/1696710550620160?origin=left_nav
     */
    @Override
    public List<OEAdvertiserReportResponse.Report> getAdvertiserDayReport(ReportRequest reportRequest, String token) {
        if (reportRequest.getAdvertiserId() == null || StrUtil.isBlank(reportRequest.getDate())) {
            throw new SqxException("advertiserId或者date不能为空");
        }

        token = StrUtil.isBlank(token) ? getToken() : token;
        String baseUrl = AD_BASE_API + "/open_api/2/report/advertiser/get/";
        int maxRetries = 3;
        int retryCount = 0;
        long retryDelay = 1000; // milliseconds

        while (retryCount <= maxRetries) {
            HttpRequest get = null;
            try {
                get = HttpUtil.createGet(baseUrl)
                        .header("Access-Token", token)
                        .timeout(1500)
                        .form("advertiser_id", reportRequest.getAdvertiserId())
                        .form("start_date", reportRequest.getDate())
                        .form("end_date", reportRequest.getDate())
                        .form("fields", "[\"cost\",\"show\",\"avg_show_cost\",\"click\",\"ctr\",\"avg_click_cost\",\"convert\",\"convert_rate\",\"convert_cost\",\"attribution_micro_game_0d_ltv\",\"attribution_micro_game_0d_roi\"]")
                        .form("time_granularity", StrUtil.isBlank(reportRequest.getTimeGranularity().getValue()) ? TimeGranularityType.STAT_TIME_GRANULARITY_DAILY.getValue() : reportRequest.getTimeGranularity().getValue())
                        .form("order_field", StrUtil.isBlank(reportRequest.getOrderField()) ? "cost" : reportRequest.getOrderField())
                        .form("order_type", StrUtil.isBlank(reportRequest.getOrderType()) ? "DESC" : reportRequest.getOrderType())
                        .form("page", reportRequest.getPage() == null ? 1 : reportRequest.getPage())
                        .form("page_size", reportRequest.getPageSize() == null ? 20 : reportRequest.getPageSize());

                try (HttpResponse httpResponse = get.execute()) {
                    if (httpResponse.getStatus() != 200) {
                        log.error("Request failed. Status: {} | Request: {} | Response: {}",
                                httpResponse.getStatus(), get, httpResponse);
                        // 服务器错误（5xx）可重试
                        if (httpResponse.getStatus() >= 500 && httpResponse.getStatus() < 600) {
                            throw new RetryableException("API请求失败，可重试");
                        } else {
                            throw new SqxException("API请求失败");
                        }
                    }

                    OceanEngineBaseResponse<OEAdvertiserReportResponse> response = JSONObject.parseObject(
                            httpResponse.body(),
                            new TypeReference<OceanEngineBaseResponse<OEAdvertiserReportResponse>>() {
                            }
                    );

                    if (response.getCode() != 0) {
                        log.error("API Error: Code={}, Message={}, RequestId={}",
                                response.getCode(), response.getMessage(), response.getRequestId());
                        // 判断是否为可重试的错误码，40100（接口总频控）
                        if (response.getCode() == 40100) {
                            throw new RetryableException("API请求被限流，可重试");
                        } else {
                            throw new SqxException("API返回错误: " + response.getMessage());
                        }
                    }

                    OEAdvertiserReportResponse data = response.getData();
                    return data.getList();
                }
            } catch (RetryableException e) {
                if (retryCount < maxRetries) {
                    retryCount++;
                    log.warn("请求失败，准备进行第{}次重试，原因: {}", retryCount, e.getMessage());
                    try {
                        Thread.sleep(retryDelay);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SqxException("重试被中断", ie);
                    }
                } else {
                    throw new SqxException("请求失败，已达到最大重试次数", e);
                }
            } catch (Exception e) {
                // 处理其他可能的重试场景，例如网络异常
                if (e instanceof IORuntimeException || e instanceof TimeoutException) {
                    if (retryCount < maxRetries) {
                        retryCount++;
                        log.warn("网络异常，准备进行第{}次重试，原因: {}", retryCount, e.getMessage());
                        try {
                            Thread.sleep(retryDelay);
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            throw new SqxException("重试被中断", ie);
                        }
                    } else {
                        throw new SqxException("请求失败，已达到最大重试次数", e);
                    }
                } else {
                    throw new SqxException("数据获取失败", e);
                }
            }
        }
        throw new SqxException("请求失败，超出重试次数");
    }

    @Override
    public List<OEAdvertiserFundResponse.BalanceData> getAdvertiserFund(List<Long> advertiserIds) {
        String token = getToken();
        String baseUrl = API_BASE_API + "/open_api/v3.0/account/fund/get/";
        HttpRequest get;
        try {
            get = HttpUtil.createGet(baseUrl)
                    .header("Access-Token", token)
                    .form("account_type", "AD")
                    .form("account_ids", JSON.toJSONString(advertiserIds));
            try (HttpResponse httpResponse = get.execute()) {
                if (httpResponse.getStatus() != 200) {
                    log.error("Request failed. Status: {} | Request: {} | Response: {}",
                            httpResponse.getStatus(), get, httpResponse);
                    throw new SqxException("API请求失败");
                }

                OceanEngineBaseResponse<OEAdvertiserFundResponse> response = JSONObject.parseObject(
                        httpResponse.body(),
                        new TypeReference<OceanEngineBaseResponse<OEAdvertiserFundResponse>>() {
                        }
                );

                if (response.getCode() != 0) {
                    log.error("API Error: Code={}, Message={}, RequestId={}",
                            response.getCode(), response.getMessage(), response.getRequestId());
                    throw new SqxException("API返回错误: " + response.getMessage());
                }

                OEAdvertiserFundResponse data = response.getData();
                return data.getList();
            }
        } catch (Exception e) {
            throw new SqxException("数据获取失败", e);
        }
    }

    public class RetryableException extends SqxException {
        public RetryableException(String message) {
            super(message);
        }

        public RetryableException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * 根据 baseApi 生成 ApiClient
     *
     * @param baseApi
     * @return
     */
    private ApiClient initApiClient(String baseApi) {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(baseApi);
        apiClient.addDefaultHeader("Access-Token", getToken());
        return apiClient;
    }
}
