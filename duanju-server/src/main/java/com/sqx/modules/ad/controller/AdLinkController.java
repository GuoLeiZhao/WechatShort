package com.sqx.modules.ad.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdLink;
import com.sqx.modules.ad.request.AdLinkQueryReq;
import com.sqx.modules.ad.service.AdLinkService;
import com.sqx.modules.douyin.request.AdQqWebhookRequest;
import com.sqx.modules.douyin.request.AdWebhookRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(value = "链接管理", tags = {"链接管理"})
@RequestMapping(value = "/ad_link")
@RequiredArgsConstructor
public class AdLinkController {
    private final AdLinkService adLinkService;

    @PostMapping()
    @ApiOperation("新建")
    public Result save(@RequestBody AdLink adLink) {
        return adLinkService.insert(adLink);
    }

    @GetMapping()
    @ApiOperation("查询")
    public Result page(AdLinkQueryReq req) {
        return Result.success().setData(adLinkService.page(req));
    }

    @PutMapping()
    @ApiOperation("编辑")
    public Result update(@RequestBody AdLink adLink) {
        return adLinkService.update(adLink);
    }

    // https://developer.open-douyin.com/docs/resource/zh-CN/mini-app/develop/server/basic-abilities/traffic-permission/ad-data-webhook
    @PostMapping("/webhook")
    @ApiOperation("抖音广告webhook")
    public Object adWebhook(@RequestBody AdWebhookRequest adWebhookRequest) {
        log.info("抖音广告webhook:{}", adWebhookRequest);
        String event = adWebhookRequest.getEvent();
        Object content = adWebhookRequest.getContent();
        if (event.equals("verify_webhook")) {
            return content;
        } else if (event.equals("ma_ecpm")) {
            try {
                adLinkService.adWebhook(adWebhookRequest);
            } catch (Exception e) {
                log.error("adWebhook: {}", adWebhookRequest, e);
            }
            return "{\"err_tips\": \"success\", \"err_no\": 0}";
        } else {
            return "{\"err_tips\": \"success\", \"err_no\": 0}";
        }
    }

    @PostMapping("/webhook_qq")
    @ApiOperation("腾讯广告webhook")
    public Object adWebhook(@RequestBody AdQqWebhookRequest adQqWebhookRequest) {
        log.info("腾讯广告webhook:{}", adQqWebhookRequest);
        String event = adQqWebhookRequest.getEventId();
        Object content = adQqWebhookRequest.getEventData();
        if (event.equals("verify_webhook")) {
            return content;
        } else if (event.equals("ma_ecpm")) {
            try {
//                adLinkService.adWebhook(adWebhookRequest);
            } catch (Exception e) {
//                log.error("adWebhook: {}", adWebhookRequest, e);
            }
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            data.put("event_id", adQqWebhookRequest.getEventId());
            response.put("code", 0);
            response.put("data", data);
            return response;
        } else {
            Map<String, Object> response = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            data.put("event_id", adQqWebhookRequest.getEventId());
            response.put("code", 0);
            response.put("data", data);
            return response;
        }
    }
}
