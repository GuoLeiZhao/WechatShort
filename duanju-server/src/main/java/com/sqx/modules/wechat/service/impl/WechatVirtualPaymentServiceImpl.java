package com.sqx.modules.wechat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.ConfigConstant;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.dao.CommonInfoDao;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.orders.dao.OrdersDao;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.utils.ID;
import com.sqx.modules.wechat.request.CurrencyPayRequest;
import com.sqx.modules.wechat.request.SignRequest;
import com.sqx.modules.wechat.request.XpayCoinPayNotifyRequest;
import com.sqx.modules.wechat.response.XPayPushMessageCommonResponse;
import com.sqx.modules.wechat.service.WechatMpService;
import com.sqx.modules.wechat.service.WechatVirtualPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class WechatVirtualPaymentServiceImpl implements WechatVirtualPaymentService {
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    public static final String CURRENCY_PAY = "/xpay/currency_pay?pay_sig={}&signature={}&access_token={}";

    private final InviteService inviteService;
    private final OrdersDao ordersDao;
    private final OrdersService ordersService;
    private final PayDetailsDao payDetailsDao;
    private final UserService userService;
    private final CommonInfoDao commonInfoDao;
    private final UserMoneyService userMoneyService;
    private final UserMoneyDetailsService userMoneyDetailsService;

    private ConfigConstant.WechatConfig wechatConfig;

    @Override
    public void init(String key) {
        this.wechatConfig = ConfigConstant.wxConfigMap.get(key);
    }

    @Override
    public Result pay(CurrencyPayRequest currencyPayRequest) {
        return Result.success();
    }

    @Override
    public String paySign(String uri, String postBody) {
        return DigestUtil.hmac(HmacAlgorithm.HmacSHA256, this.wechatConfig.getAppKey().getBytes(StandardCharsets.UTF_8)).digestHex(uri + "&" + postBody);
    }

    @Override
    public String userSign(String sessionKey, String postBody) {
        return DigestUtil.hmac(HmacAlgorithm.HmacSHA256, sessionKey.getBytes(StandardCharsets.UTF_8)).digestHex(postBody);
    }

    @Override
    public XPayPushMessageCommonResponse payCallBack(XpayCoinPayNotifyRequest xpayCoinPayNotifyRequest) {
        String outTradeNo = xpayCoinPayNotifyRequest.getOutTradeNo();
        PayDetails payDetails = payDetailsDao.selectByOrderId(outTradeNo);
        String now = DateUtil.now();
        if (payDetails.getState() == 0) {
            payDetailsDao.updateState(payDetails.getId(), 1, now, "");
            if (payDetails.getType() == 1) {
                Orders orders = ordersDao.selectOne(new QueryWrapper<Orders>().lambda().eq(Orders::getOrdersNo, outTradeNo));
                orders.setStatus(1);
                orders.setPayWay(3);
                ordersDao.updateById(orders);
                UserEntity user = userService.selectUserById(orders.getUserId());
                UserEntity byUser = userService.queryByInvitationCode(user.getInviterCode());
                inviteService.updateInvite(byUser, now, user.getUserId(), orders.getPayMoney());
                ordersDao.insertOrders(orders);
            } else {
                double allMoney = 0;
                try {
                    CommonInfo charge = commonInfoDao.findByConditionAndValue("" + payDetails.getMoney(), "charge");

                    if (StrUtil.isNotBlank(charge.getMax()) && NumberUtil.isNumber(charge.getMax())) {
                        allMoney = NumberUtil.parseDouble(charge.getMax()) + allMoney;
                    }
                    if (StrUtil.isNotBlank(charge.getMin()) && NumberUtil.isNumber(charge.getMin())) {
                        allMoney = NumberUtil.parseDouble(charge.getMin()) + allMoney;
                    }
                } catch (Exception exception) {
                    log.error("充值回调获取积分失败！payMoney is :" + payDetails.getMoney(), exception);
                    allMoney = payDetails.getMoney();
                }

                userMoneyService.updateMoney(1, payDetails.getUserId(), allMoney);
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setMoney(BigDecimal.valueOf(payDetails.getMoney()));
                userMoneyDetails.setUserId(payDetails.getUserId());
                userMoneyDetails.setContent("微信充值看点");
                userMoneyDetails.setTitle("微信充值：" + payDetails.getMoney() + "，增加看点：" + allMoney);
                userMoneyDetails.setType(1);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                userMoneyDetails.setCreateTime(simpleDateFormat.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);

                Orders orders = ordersService.selectOrderByOrdersNo(payDetails.getOrderId());
                orders.setPayWay(3);
                orders.setStatus(1);
                ordersService.updateById(orders);
            }

        }

        return new XPayPushMessageCommonResponse();
    }


    @Override
    public Result sign(SignRequest signRequest) {
        reentrantReadWriteLock.writeLock().lock();
        String generalOrder = ID.get();
        try {
            PayDetails payDetails = new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(DateUtil.now());
            payDetails.setOrderId(generalOrder);
            payDetails.setUserId(signRequest.getUserId());
            payDetails.setMoney(signRequest.getMoney().doubleValue());
            // 微信小程序
            payDetails.setClassify(3);
            payDetails.setType(2);
            payDetailsDao.insert(payDetails);
            Orders order = new Orders();
            order.setOrdersNo(generalOrder);
            order.setUserId(signRequest.getUserId());
            order.setCourseId(signRequest.getCourseId());
            order.setCourseDetailsId(signRequest.getCourseDetailsId());
            order.setPayMoney(signRequest.getMoney());
            order.setStatus(0);
            order.setCreateTime(DateUtil.now());
            order.setOrdersType(3);
            ordersDao.insertOrders(order);
            SignRequest.SignData signData = signRequest.getSignData();
            signData.setOutTradeNo(generalOrder);
            String signDataStr = JSONObject.toJSONString(signData);
            return Result.success().put("userSign", userSign(signRequest.getSessionKey(), signDataStr))
                    .put("paySign", paySign("requestVirtualPayment", signDataStr))
                    .put("orderId", generalOrder)
                    .put("signData", signDataStr);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }
}

