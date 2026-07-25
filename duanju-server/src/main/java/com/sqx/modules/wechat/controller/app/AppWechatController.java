package com.sqx.modules.wechat.controller.app;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.ConfigConstant;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.wechat.request.SignRequest;
import com.sqx.modules.wechat.request.XpayCoinPayNotifyRequest;
import com.sqx.modules.wechat.response.XPayPushMessageCommonResponse;
import com.sqx.modules.wechat.service.WechatOfficeAccountService;
import com.sqx.modules.wechat.service.WechatVirtualPaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
@RestController
@Api(value = "微信公众号", tags = {"微信公众号"})
@RequestMapping(value = "/app/wechatOfficeAccount")
@RequiredArgsConstructor
public class AppWechatController {
    private final WechatOfficeAccountService wechatOfficeAccountService;
    private final WechatVirtualPaymentService wechatVirtualPaymentService;

    @PostMapping(value = "/sign")
    @ResponseBody
    @ApiOperation("微信公众号 sign")
    public Result updatePwd(String url) {
        return Result.success().put("data", wechatOfficeAccountService.sign(url));
    }

    @PostMapping(value = "/check/{platform}")
    @ResponseBody
    @ApiOperation("支付回调")
    public XPayPushMessageCommonResponse xpay_coin_pay_notify(@PathVariable String platform, @RequestBody String requestBody) {
        log.info("wx callback req : {}", requestBody);
        JSONObject msgBody = JSON.parseObject(requestBody);
        if (!msgBody.containsKey("Event") || !"xpay_coin_pay_notify".equals(msgBody.getString("Event"))) {
            return new XPayPushMessageCommonResponse();
        }

        XpayCoinPayNotifyRequest xpayCoinPayNotifyRequest = JSON.parseObject(requestBody, XpayCoinPayNotifyRequest.class);
        wechatVirtualPaymentService.init(platform);
        return wechatVirtualPaymentService.payCallBack(xpayCoinPayNotifyRequest);
    }

    @PostMapping("/getSign/{platform}")
    @ResponseBody
    @ApiOperation("获取签名")
    public Result getSecurityString(@PathVariable String platform, @RequestBody SignRequest signRequest) {
        wechatVirtualPaymentService.init(platform);
        return wechatVirtualPaymentService.sign(signRequest);
    }

    @GetMapping("/check/{platform}")
    public String checkSignature(@PathVariable String platform, String signature, String timestamp, String nonce, String echostr) {
        ConfigConstant.WechatConfig wechatConfig = ConfigConstant.wxConfigMap.get(platform);
        String[] tmpArr = {wechatConfig.getToken(), timestamp, nonce};
        Arrays.sort(tmpArr);
        String tmpStr = String.join("", tmpArr);

        String hash = DigestUtil.sha1Hex(tmpStr, StandardCharsets.UTF_8.name());

        if (hash.equalsIgnoreCase(signature)) {
            return echostr;
        }
        return "error";
    }
}
