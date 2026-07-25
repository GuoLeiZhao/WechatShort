package com.sqx.modules.wechat.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.parser.ParserConfig;
import com.sqx.common.utils.ConfigConstant;
import com.sqx.common.utils.RedisUtils;
import com.sqx.modules.wechat.response.GetOffAccTicketResponse;
import com.sqx.modules.wechat.response.GetUserInfoResponse;
import com.sqx.modules.wechat.response.GetUserPhoneNumberResponse;
import com.sqx.modules.wechat.response.WechatOfficeAccountSignResponse;
import com.sqx.modules.wechat.service.WechatMpService;
import com.sqx.modules.wechat.service.WechatOfficeAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WechatOfficeAccountServiceImpl implements WechatOfficeAccountService {


    private final WechatMpService wechatMpService;
    private final RedisUtils redisUtils;


    private static final String WX_OFA_AUTH_TICKET_KEY = "WX_OFA_AUTH_TICKET_KEY";

    private static final String GET_TICKET = "/cgi-bin/ticket/getticket?access_token={}&type=jsapi";
    private static final String GET_USER_INFO = "/cgi-bin/user/info?access_token={}&openid={}&lang=zh_CN";

    private String getTicket() {
        String ticket = redisUtils.get(WX_OFA_AUTH_TICKET_KEY);
        if (StrUtil.isNotBlank(ticket)) {
            return ticket;
        }

        synchronized (this) {
            ticket = redisUtils.get(WX_OFA_AUTH_TICKET_KEY);
            if (StrUtil.isNotBlank(ticket)) {
                return ticket;
            }
            String accessToken = wechatMpService.getAccessToken(new ConfigConstant.App1Gzh());
            return wechatMpService.executeWechatApi(Method.GET, GET_TICKET, null, "获取微信公众号jsapi_ticket", accessToken, (response) -> {
                GetOffAccTicketResponse getOffAccTicketResponse = JSON.parseObject(response.body(), GetOffAccTicketResponse.class);
                redisUtils.set(WX_OFA_AUTH_TICKET_KEY, getOffAccTicketResponse.getTicket(), getOffAccTicketResponse.getExpires_in());
                return getOffAccTicketResponse.getTicket();
            });
        }
    }

    @Override
    public String getAccessToken() {
        return wechatMpService.getAccessToken(new ConfigConstant.App1Gzh());
    }

    @Override
    public GetUserInfoResponse getUserInfo(String openId) {
        String accessToken = getAccessToken();
        String url = StrUtil.format(GET_USER_INFO, accessToken, openId);

        return wechatMpService.executeWechatApi(Method.GET, url, null, "获取微信公众号用户信息", accessToken, (response) -> {
            ParserConfig parserConfig = new ParserConfig();
            parserConfig.propertyNamingStrategy = PropertyNamingStrategy.SnakeCase;
            return JSONObject.parseObject(response.body(), GetUserPhoneNumberResponse.class, parserConfig);
        });
    }

    @Override
    public WechatOfficeAccountSignResponse sign(String url) {
        String jsapiTicket = getTicket();
        String nonceStr = createNonceStr();
        String timestamp = createTimestamp();
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        String paramsStr = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp + "&url=" + url;

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(paramsStr.getBytes(StandardCharsets.UTF_8));
            signature = byteToHex(crypt.digest());
        } catch (Exception e) {
            log.error("WechatOfficeAccountService.sign error!", e);
        }

        WechatOfficeAccountSignResponse wechatOfficeAccountSignResponse = new WechatOfficeAccountSignResponse();
        wechatOfficeAccountSignResponse.setUrl(url);
        wechatOfficeAccountSignResponse.setNonceStr(nonceStr);
        wechatOfficeAccountSignResponse.setTimestamp(timestamp);
        wechatOfficeAccountSignResponse.setSignature(signature);
        wechatOfficeAccountSignResponse.setAppId(ConfigConstant.App1Gzh.APP_ID);

        return wechatOfficeAccountSignResponse;
    }

    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    private String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
