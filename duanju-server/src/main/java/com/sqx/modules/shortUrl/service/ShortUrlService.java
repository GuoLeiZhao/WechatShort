package com.sqx.modules.shortUrl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.shortUrl.entity.ShortUrl;

public interface ShortUrlService extends IService<ShortUrl> {
    ShortUrl queryByShortUrl(String shortURL);

    Result insert(ShortUrl shortUrl);

    Result changeBak(String type);
}
