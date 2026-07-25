package com.sqx.modules.ad.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.dao.AdBlackListDao;
import com.sqx.modules.ad.entity.AdApp;
import com.sqx.modules.ad.entity.AdBlackList;
import com.sqx.modules.ad.request.AdBlackListQueryReq;
import com.sqx.modules.ad.response.AdBlackQueryRes;
import com.sqx.modules.ad.service.AdAppService;
import com.sqx.modules.ad.service.AdBlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdBlackListServiceImpl extends ServiceImpl<AdBlackListDao, AdBlackList> implements AdBlackListService {

    private final AdAppService adAppService;

    @Override
    public IPage<AdBlackQueryRes> page(AdBlackListQueryReq req) {
        IPage<AdBlackList> adBlackListIPage =  baseMapper.selectPage(
                new Query<AdBlackList>().getPage(req),
                new QueryWrapper<AdBlackList>().lambda()
                        .eq(null != req.getBlackKey(), AdBlackList::getBlackKey, req.getBlackKey())
                        .eq(StrUtil.isNotBlank(req.getAppId()), AdBlackList::getAppId, req.getAppId())
                        .like(StrUtil.isNotBlank(req.getBlackValue()), AdBlackList::getBlackValue, req.getBlackValue())
        );
        List<String> appIds = adBlackListIPage.getRecords().stream().map(AdBlackList::getAppId).collect(Collectors.toList());
        Map<String, AdApp> adAppMap = batchQueryAdApp(appIds);
        // 转换数据
        IPage<AdBlackQueryRes> adLinkQueryResIPage = adBlackListIPage.convert(item -> {
            AdBlackQueryRes adBlackQueryRes = new AdBlackQueryRes();
            BeanUtil.copyProperties(item, adBlackQueryRes);
            // 设置应用名称
            adBlackQueryRes.setAppName(getAppName(item.getAppId(), adAppMap));

            return adBlackQueryRes;
        });

        return adLinkQueryResIPage;

    }

    @Override
    public Result insert(AdBlackList adBlackList) {
        Result selectOneRes = selectOne(adBlackList);
        if (selectOneRes.get("code").equals(0)) {
            return Result.error("黑名单已存在");
        }

        int rows = baseMapper.insert(adBlackList);
        if (rows > 0) {
            return Result.success("添加成功！");
        } else {
            return Result.error("添加失败");
        }
    }

    @Override
    public Result selectOne(AdBlackList adBlackList) {
        if (StrUtil.isBlank(adBlackList.getBlackValue()) || adBlackList.getBlackKey() == null) {
            return Result.error("参数错误");
        }
        LambdaQueryWrapper<AdBlackList> adBlackListLambdaQueryWrapper = new QueryWrapper<AdBlackList>().lambda()
                .eq(AdBlackList::getBlackValue, adBlackList.getBlackValue())
                .eq(AdBlackList::getBlackKey, adBlackList.getBlackKey())
                .eq(StrUtil.isNotBlank(adBlackList.getAppId()), AdBlackList::getAppId, adBlackList.getAppId())
                .last("limit 1");
        AdBlackList one = baseMapper.selectOne(adBlackListLambdaQueryWrapper);
        if (null != one) {
            return Result.success().setData(one);
        } else {
            return Result.error();
        }
    }

    @Override
    public Result deleteById(Long id) {
        int rows = baseMapper.deleteById(id);
        if (rows > 0) {
            return Result.success("删除成功！");
        } else {
            return Result.error("删除失败");
        }
    }

    private Map<String, AdApp> batchQueryAdApp(List<String> ids) {
        // 批量查询应用
        Result result = adAppService.selectBatchIds(ids);
        if (result.get("code").equals(0)) {
            List<AdApp> adApps = (List<AdApp>) result.get("data");
            return adApps.stream().collect(Collectors.toMap(AdApp::getAppId, adApp -> adApp));
        }
        return Collections.emptyMap();
    }

    private String getAppName(String id, Map<String, AdApp> adAppMap) {
        AdApp adApp = adAppMap.get(id);
        return adApp != null ? adApp.getAppName() : "";
    }

}
