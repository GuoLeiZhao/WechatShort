package com.sqx.modules.shortUrl.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "商品信息", tags = {"商品信息"})
@RequestMapping(value = "/admin/shortUrlLog")
@RequiredArgsConstructor
public class ShortUrlLogController {
}
