package com.sqx.modules.ad.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.base.BasePagingRequest;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.dao.AdAppDao;
import com.sqx.modules.ad.entity.AdApp;
import com.sqx.modules.ad.entity.AdCustomer;
import com.sqx.modules.ad.entity.AdPlay;
import com.sqx.modules.ad.request.AdAppQueryReq;
import com.sqx.modules.ad.request.AdPlayQueryReq;
import com.sqx.modules.ad.service.AdAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdAppServiceImpl extends ServiceImpl<AdAppDao, AdApp> implements AdAppService {
    @Override
    public IPage<AdApp> page(AdAppQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("created_at", false)));
        return baseMapper.selectPage(
                new Query<AdApp>().getPage(req),
                new QueryWrapper<AdApp>().lambda()
                        .eq(StrUtil.isNotBlank(req.getAppId()), AdApp::getAppId, req.getAppId())
                        .like(StrUtil.isNotBlank(req.getAppName()), AdApp::getAppName, req.getAppName())
                        .gt(null != req.getCreatedStart(), AdApp::getCreatedAt, req.getCreatedStart())
                        .lt(null != req.getCreatedEnd(), AdApp::getCreatedAt, req.getCreatedEnd())
        );
    }

    @Override
    public List<AdApp> dropList(AdAppQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("created_at", false)));
        return baseMapper.selectList(
                new QueryWrapper<AdApp>().lambda()
                        .eq(StrUtil.isNotBlank(req.getAppId()), AdApp::getAppId, req.getAppId())
                        .like(StrUtil.isNotBlank(req.getAppName()), AdApp::getAppName, req.getAppName())
        );
    }

    @Override
    public Result insert(AdApp adApp) {
        int rows = baseMapper.insert(adApp);
        if (rows > 0) {
            return Result.success("添加成功！");
        } else {
            return Result.error("添加失败");
        }
    }

    @Override
    public Result deleteById(String appId) {
        int rows = baseMapper.deleteById(appId);
        if (rows > 0) {
            return Result.success("删除成功！");
        } else {
            return Result.error("删除失败");
        }
    }

    @Override
    public Result update(AdApp adApp) {
        QueryWrapper<AdApp> whereWrapper = new QueryWrapper<>();
        whereWrapper.eq("app_id", adApp.getAppId());
        int rows = baseMapper.update(adApp, whereWrapper);
        if (rows > 0) {
            return Result.success("更新成功！");
        } else {
            return Result.error("更新失败");
        }
    }

    @Override
    public Result selectById(String appId) {
        AdApp adApp = baseMapper.selectById(appId);
        if (adApp != null) {
            return Result.success().setData(adApp);
        } else {
            return Result.error("获取失败");
        }
    }

    @Override
    public Result selectBatchIds(List<String> ids) {
        List<AdApp> adAppList = baseMapper.selectBatchIds(ids);
        if (adAppList != null) {
            return Result.success().setData(adAppList);
        } else {
            return Result.error("获取失败");
        }
    }

}
