package com.sqx.modules.ad.controller.app;

import cn.hutool.core.util.StrUtil;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdEventHistory;
import com.sqx.modules.ad.service.AdAppService;
import com.sqx.modules.ad.service.AdEventHistoryService;
import com.sqx.modules.douyin.request.ConversionRequest;
import com.sqx.modules.douyin.service.DouyinOcAdService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(value = "小程序管理", tags = {"小程序管理"})
@RequestMapping(value = "/app/ad_app")
@RequiredArgsConstructor
public class AppAdAppController {
    private final AdAppService adAppService;
    private final AdEventHistoryService adEventHistoryService;
    private final DouyinOcAdService douyinOcAdService;

    @GetMapping("/{appId}")
    @ApiOperation("查询")
    public Result selectById(@PathVariable String appId) {
        return adAppService.selectById(appId);
    }

    @PostMapping("/event_back")
    @ApiOperation("广告事件回传")
    public Result save(@RequestBody AdEventHistory adEventHistory) {
        if (StrUtil.isEmpty(adEventHistory.getOpenId())) {
            log.info("event_back 该用户不存在:{}", adEventHistory);
        } else if (adEventHistoryService.getAdEventHistoryOne(adEventHistory) != null) {
            log.info("event_back 用户已激活:{}", adEventHistory);
//            return Result.error("用户已激活");
        }

        ConversionRequest.Ad ad = new ConversionRequest.Ad();
        ad.setCallback(adEventHistory.getClickid());
        ConversionRequest.Context context = new ConversionRequest.Context();
        context.setAd(ad);
        ConversionRequest conversionRequest = new ConversionRequest();
        conversionRequest.setEvent_type(adEventHistory.getEventType());
        conversionRequest.setContext(context);
        Result result = douyinOcAdService.uploadConversion(conversionRequest);
        if (result.get("code").equals(500)) {
            return result;
        }
        return adEventHistoryService.insert(adEventHistory);
    }

}
