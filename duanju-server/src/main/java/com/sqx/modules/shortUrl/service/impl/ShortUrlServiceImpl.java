package com.sqx.modules.shortUrl.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.shortUrl.dao.ShortUrlDao;
import com.sqx.modules.shortUrl.entity.ShortUrl;
import com.sqx.modules.shortUrl.service.ShortUrlService;
import com.sqx.modules.utils.HashUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortUrlServiceImpl extends ServiceImpl<ShortUrlDao, ShortUrl> implements ShortUrlService {
    @Override
    public ShortUrl queryByShortUrl(String surl) {
        LambdaQueryWrapper<ShortUrl> last = new QueryWrapper<ShortUrl>().lambda()
                .eq(ShortUrl::getSurl, surl)
                .eq(ShortUrl::getDeleted, 1)
                .orderByDesc(ShortUrl::getCreatedAt);
        Integer i = baseMapper.selectCount(last);
        if (0 == i) {
            return null;
        }

        if (1 == i) {
            return baseMapper.selectOne(last.last("limit 1"));
        }

        List<ShortUrl> shortUrls = baseMapper.selectList(last);
        return shortUrls.get(Math.toIntExact(DateUtil.current() % i));
    }

    @Override
    public Result insert(ShortUrl shortUrl) {
        shortUrl.setSurl(HashUtils.hashToBase62(shortUrl.getLurl()));
        return Result.success().setData(this.save(shortUrl));
    }

    @Override
    public Result changeBak(String type) {
        LambdaQueryWrapper<ShortUrl> last = new QueryWrapper<ShortUrl>().lambda()
                .isNotNull(ShortUrl::getBakUrl)
                .eq(ShortUrl::getType, type);
        List<ShortUrl> shortUrls = baseMapper.selectList(last);

        if (CollUtil.isEmpty(shortUrls)) {
            return Result.error("不存在需要更换的备用短链");
        }

        shortUrls.forEach(shortUrl -> {
            String tmp = shortUrl.getBakUrl();
            shortUrl.setBakUrl(shortUrl.getLurl());
            shortUrl.setLurl(tmp);
            baseMapper.updateById(shortUrl);
        });

        return Result.success();
    }
}
