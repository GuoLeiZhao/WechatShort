package com.sqx.modules.shortUrl.controller.app;

import cn.hutool.core.util.StrUtil;
import com.sqx.common.utils.Result;
import com.sqx.modules.shortUrl.entity.ShortUrl;
import com.sqx.modules.shortUrl.entity.ShortUrlLog;
import com.sqx.modules.shortUrl.response.IPAddressInfoResponse;
import com.sqx.modules.shortUrl.service.ShortUrlLogService;
import com.sqx.modules.shortUrl.service.ShortUrlService;
import com.sqx.modules.utils.IPUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@Api(value = "商品信息", tags = {"商品信息"})
@RequestMapping(value = "/short_url/")
@RequiredArgsConstructor
public class AppShortUrlController {

    private final ShortUrlService shortUrlService;
    @GetMapping("/{shortURL}")
    public String redirect(HttpServletRequest request, @PathVariable String shortURL, String mzuid) {
        if ("favicon.ico".equals(shortURL)) {
            return null;
        }
        long time = System.currentTimeMillis();

        try {
            ShortUrl longURL = shortUrlService.queryByShortUrl(shortURL);
            if (null == longURL || StrUtil.isBlank(longURL.getLurl())) {
                return "redirect:/s/404";
            }
            String lurl = longURL.getLurl();
            if (StrUtil.isBlank(mzuid)) {
                return "redirect:" + lurl;
            }

            if (0 < lurl.indexOf("?")) {
                return "redirect:" + lurl + "&mzuid=" + mzuid;
            } else {
                return "redirect:" + lurl + "?mzuid=" + mzuid;
            }
        } catch (Exception e) {
            log.error("点击异常: ", e);
        }
        log.info("shortUrl: {}, all time: {}ms", shortURL, System.currentTimeMillis() - time);
        //没有对应的原始链接，直接返回首页
        return "redirect:/s/404";
    }

}
