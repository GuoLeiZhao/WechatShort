package com.sqx.modules.shortUrl.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.sqx.common.exception.SqxException;
import com.sqx.common.utils.Result;
import com.sqx.modules.shortUrl.entity.ShortUrl;
import com.sqx.modules.shortUrl.service.ShortUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Api(value = "短链", tags = {"短链"})
@RequestMapping(value = "/admin/shortUrl")
@RequiredArgsConstructor
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    @PostMapping
    @ApiOperation("添加短链信息")
    public Result addShortUrl(String lurl, String name) {
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setLurl(lurl);
        shortUrl.setName(name);
        return shortUrlService.insert(shortUrl);
    }

    @GetMapping("/change_bak/{date}/{type}")
    @ApiOperation("更换备用")
    public Result changeBak(@PathVariable String date, @PathVariable String type) {
        if (StrUtil.isBlank(date) || !DateUtil.format(new Date(), "yyyyMMdd").equals(date)) {
            throw new SqxException("日期错误");
        }
        if (StrUtil.isBlank(type)) {
            throw new SqxException("类型错误");
        }
        return shortUrlService.changeBak(type);
    }
}
