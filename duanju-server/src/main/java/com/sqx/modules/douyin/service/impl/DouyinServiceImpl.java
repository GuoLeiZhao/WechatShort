package com.sqx.modules.douyin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.RedisUtils;
import com.sqx.config.DouyinConfig;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.course.dao.CourseDao;
import com.sqx.modules.course.dao.CourseDetailsDao;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseDetails;
import com.sqx.modules.douyin.request.AdWebhookRequest;
import com.sqx.modules.douyin.request.Code2SessionRequest;
import com.sqx.modules.douyin.request.CreateOrderRequest;
import com.sqx.modules.douyin.request.GetAccessTokenRequest;
import com.sqx.modules.douyin.response.*;
import com.sqx.modules.douyin.service.DouyinService;
import com.sqx.modules.orders.dao.OrdersDao;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.utils.DouyinSignUtil;
import com.sqx.modules.utils.ID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class DouyinServiceImpl implements DouyinService {

    static final String ACCESS_TOKEN_KEY_PREFIX = "douyin:access_key:";
    static final String BASE_URL = "https://developer.toutiao.com";
    static final String ACCESS_TOKEN_URL = "/api/apps/v2/token";
    static final String CODE_2_SESSION_URL = "/api/apps/v2/jscode2session";
    static final String CREATE_ORDER_URL = "/api/apps/ecpay/v1/create_order";

    private final OrdersDao ordersDao;
    private final CourseDao courseDao;
    private final RedisUtils redisUtils;
    private final PayDetailsDao payDetailsDao;
    private final CourseDetailsDao courseDetailsDao;
    private final CommonInfoService commonInfoService;
    private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    @Override
    public String getAccessToken(String app) {
        String token = redisUtils.get(ACCESS_TOKEN_KEY_PREFIX + app);
        if (StrUtil.isNotBlank(token)) {
            return token;
        }

        String appid = DouyinConfig.getDouyinConfig(app).getAppid();

        synchronized (appid) {
            token = redisUtils.get(ACCESS_TOKEN_KEY_PREFIX + app);
            if (StrUtil.isNotBlank(token)) {
                return token;
            }
            return accessTokenExecute(app);
        }
    }

    @Override
    public Code2SessionResponse code2Session(String app, Code2SessionRequest request) {

        String appid = DouyinConfig.getDouyinConfig(app).getAppid();
        String secret = DouyinConfig.getDouyinConfig(app).getSecret();

        request.setAppid(appid);
        request.setSecret(secret);

        DouyinBaseResponse<Code2SessionResponse> response = executeDouyinApi(Method.POST, CODE_2_SESSION_URL, request, "code2Session",
                (httpResponse) -> JSONObject.parseObject(httpResponse.body(), new TypeReference<DouyinBaseResponse<Code2SessionResponse>>() {
                }, Feature.DisableFieldSmartMatch));
        return response.getData();
    }

    @Override
    public CreateOrderResponse createOrder(String app, CreateOrderRequest createOrderRequest) {
        // 本平台相关
        reentrantReadWriteLock.writeLock().lock();
        String generalOrder = ID.get();
        try {
            PayDetails payDetails = new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(DateUtil.now());
            payDetails.setOrderId(generalOrder);
            payDetails.setUserId(createOrderRequest.getUserId());
            payDetails.setMoney(createOrderRequest.getMoney().doubleValue());
            // 微信小程序
            payDetails.setClassify(5);
            payDetails.setType(2);
            payDetailsDao.insert(payDetails);
            Orders order = new Orders();
            order.setOrdersNo(generalOrder);
            order.setUserId(createOrderRequest.getUserId());
            Long courseId = createOrderRequest.getCourseId();
            order.setCourseId(courseId);
            Long courseDetailsId = createOrderRequest.getCourseDetailsId();
            order.setCourseDetailsId(courseDetailsId);
            order.setPayMoney(createOrderRequest.getMoney());
            order.setStatus(0);
            order.setCreateTime(DateUtil.now());
            order.setOrdersType(3);
            ordersDao.insertOrders(order);

            // 抖音相关
            CreateOrderRequest.DouyinCreateOrderRequest request = createOrderRequest.getDouyinCreateOrderRequest();
            if (null == request) {
                request = new CreateOrderRequest.DouyinCreateOrderRequest();
            }

            request.setTotalAmount(createOrderRequest.getMoney().multiply(new BigDecimal(100)).longValue());
            request.setOutOrderNo(generalOrder);

            if (null != courseId && 0L != courseId) {
                Course course = courseDao.selectById(courseId);
                request.setSubject(course.getTitle());
            } else {
                request.setSubject("用户充值");
            }

            if (null != courseDetailsId && 0L != courseDetailsId) {
                CourseDetails courseDetails = courseDetailsDao.selectById(courseDetailsId);
                request.setBody(courseDetails.getCourseDetailsName());
            } else {
                request.setBody("充值金额" + createOrderRequest.getMoney());
            }

            CommonInfo backendUrl = commonInfoService.findOne(12);

            String appid = DouyinConfig.getDouyinConfig(app).getAppid();
            String salt = DouyinConfig.getDouyinConfig(app).getSalt();

            request.setAppId(appid);
            // 设置过期时间 1 小时
            request.setValidTime(3600L);
            request.setNotifyUrl(backendUrl.getValue() + "/sqx_fast/app/douyin/" + app + "/payNotify");

            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("app_id", request.getAppId());
            paramsMap.put("out_order_no", request.getOutOrderNo());
            paramsMap.put("total_amount", request.getTotalAmount());
            paramsMap.put("subject", request.getSubject());
            paramsMap.put("body", request.getBody());
            paramsMap.put("notify_url", request.getNotifyUrl());
            paramsMap.put("disable_msg", request.getDisableMsg());
            paramsMap.put("valid_time", request.getValidTime());

            String sign = new DouyinSignUtil(salt).requestSign(paramsMap);
            request.setSign(sign);

            paramsMap.put("sign", sign);
            DouyinBaseResponse<CreateOrderResponse> response = executeDouyinApi(Method.POST, CREATE_ORDER_URL, paramsMap, "createOrder", (httpResponse) ->
                    JSONObject.parseObject(httpResponse.body(), new TypeReference<DouyinBaseResponse<CreateOrderResponse>>() {
                    }, Feature.DisableFieldSmartMatch));
            return response.getData();

        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }


    /**
     * 获取access_token
     *
     * @param app
     * @return
     */
    private String accessTokenExecute(String app) {
        String appid = DouyinConfig.getDouyinConfig(app).getAppid();
        String secret = DouyinConfig.getDouyinConfig(app).getSecret();

        DouyinBaseResponse<GetAccessTokenResponse> response = executeDouyinApi(Method.POST, ACCESS_TOKEN_URL, new GetAccessTokenRequest(appid, secret), "获取access_token", (httpResponse) -> {
            ParserConfig parserConfig = new ParserConfig();
            parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            return JSONObject.parseObject(httpResponse.body(), DouyinBaseResponse.class, parserConfig);
        });
        GetAccessTokenResponse responseData = response.getData();
        redisUtils.set(ACCESS_TOKEN_KEY_PREFIX + app, responseData.getAccessToken(), responseData.getExpiresIn());
        return responseData.getAccessToken();
    }

    /**
     * 执行抖音api
     *
     * @param method
     * @param url
     * @param reqParam
     * @param apiName
     * @param mapper
     * @param <R>
     * @return
     */
    private <R> R executeDouyinApi(Method method, String url, Object reqParam, String apiName, Function<HttpResponse, ? extends R> mapper) {
        HttpRequest request = HttpUtil.createRequest(method, BASE_URL + url);
        request.contentType(ContentType.JSON.toString());
        request.timeout(1500);
        request.setMaxRedirectCount(2);
        if (null != reqParam) {
            if (reqParam instanceof String && StrUtil.isNotBlank(reqParam.toString())) {
                // 是 string 直接塞进去
                request.body(reqParam.toString());
            } else {
                // others
                SerializeConfig serializeConfig = new SerializeConfig();
                // 驼峰转下划线
                serializeConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
                request.body(JSON.toJSONString(reqParam, serializeConfig));
            }
        }

        try (HttpResponse httpResponse = request.execute()) {
            if (200 == httpResponse.getStatus()) {
                // 200 成功
                JSONObject responseJson = JSONObject.parseObject(httpResponse.body());
                if (!responseJson.containsKey("err_no") || 0 == responseJson.getInteger("err_no")) {
//                    // 0 成功
                    return mapper.apply(httpResponse);
                } else {
                    logReqAndRes(request, httpResponse);
                    throw new SqxException(apiName + " 接口错误，请查看日志并检查配置！");
                }
            } else {
                logReqAndRes(request, httpResponse);
                throw new SqxException(apiName + " 接口错误，请查看日志并检查配置！");
            }
        }
    }

    private void logReqAndRes(HttpRequest request, HttpResponse response) {
        log.error("Douyin request: {}", request);
        log.error("Douyin response: {}", response);
    }

    @Override
    public AdWebhookResponse adWebhook(AdWebhookRequest adWebhookRequest) {
        AdWebhookResponse response = new AdWebhookResponse();
        response.setLogId(adWebhookRequest.getLogId());
        response.setEvent(adWebhookRequest.getEvent());
        response.setClientKey(adWebhookRequest.getClientKey());
        response.setFromUserId(adWebhookRequest.getFromUserId());

        Object content = adWebhookRequest.getContent();
        List<AdWebhookResponse.ContentItem> contentItems;
        if (content instanceof String) {
            // 如果 content 是 JSON 字符串，解析为 List
            contentItems = JSONObject.parseObject((String) content, new TypeReference<List<AdWebhookResponse.ContentItem>>() {
            }, Feature.DisableFieldSmartMatch);

        } else {
            // 如果 content 已解析为 List 或其他类型，转为 JSON 再解析
            contentItems = JSON.parseObject(
                    JSON.toJSONString(content),
                    new TypeReference<List<AdWebhookResponse.ContentItem>>() {
                    }
            );
        }

        response.setContentItems(contentItems);

        return response;
    }

}
