package com.sqx.modules.shortUrl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.shortUrl.dao.ShortUrlLogDao;
import com.sqx.modules.shortUrl.entity.ShortUrlLog;
import com.sqx.modules.shortUrl.service.ShortUrlLogService;
import org.springframework.stereotype.Service;

@Service
public class ShortUrlLogServiceImpl extends ServiceImpl<ShortUrlLogDao, ShortUrlLog> implements ShortUrlLogService {
}
