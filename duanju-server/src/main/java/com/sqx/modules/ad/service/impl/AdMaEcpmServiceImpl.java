package com.sqx.modules.ad.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.base.BasePagingRequest;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.ad.dao.AdMaEcpmDao;
import com.sqx.modules.ad.entity.AdMaEcpm;
import com.sqx.modules.ad.request.AdMaEcpmListReq;
import com.sqx.modules.ad.request.AdMaEcpmQueryReq;
import com.sqx.modules.ad.service.AdMaEcpmService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.utils.ID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdMaEcpmServiceImpl extends ServiceImpl<AdMaEcpmDao, AdMaEcpm> implements AdMaEcpmService {

    private final CommonInfoService commonRepository;

    private Wrapper<AdMaEcpm> queryWrapper(AdMaEcpmQueryReq req) {
        return new QueryWrapper<AdMaEcpm>().lambda()
                .eq(StrUtil.isNotBlank(req.getMpId()), AdMaEcpm::getMpId, req.getMpId())
                .eq(StrUtil.isNotBlank(req.getOpenId()), AdMaEcpm::getOpenId, req.getOpenId())
                .eq(StrUtil.isNotBlank(req.getLinkId()), AdMaEcpm::getLinkId, req.getLinkId())
                .eq(null != req.getIsHandle(), AdMaEcpm::getIsHandle, req.getIsHandle())
                .gt(null != req.getCreatedStart(), AdMaEcpm::getCreatedAt, req.getCreatedStart())
                .lt(null != req.getCreatedEnd(), AdMaEcpm::getCreatedAt, req.getCreatedEnd());
    }

    @Override
    public IPage<AdMaEcpm> page(AdMaEcpmQueryReq req) {
        req.setOrders(Collections.singletonList(new BasePagingRequest.Order("created_at", false)));
        return baseMapper.selectPage(
                new Query<AdMaEcpm>().getPage(req),
                queryWrapper(req)
        );
    }

    @Override
    public Result download(AdMaEcpmQueryReq req) {
        List<AdMaEcpm> download = baseMapper.selectList(
                queryWrapper(req)
        );
        log.info("download size:{}", download.size());
        File templateFile = new File("/template");
        if (!templateFile.exists()) {
            boolean ignored = templateFile.mkdir();
        }
        String id = ID.get();
        File file = new File("/template/" + id +".xlsx");
        try (BigExcelWriter writer = ExcelUtil.getBigWriter("/template/" + id + ".xlsx")) {
            writer.addHeaderAlias("id", "id");
            writer.addHeaderAlias("event", "event");
            writer.addHeaderAlias("clientKey", "clientKey");
            writer.addHeaderAlias("contentId", "contentId");
            writer.addHeaderAlias("mpId", "mpId");
            writer.addHeaderAlias("cost", "cost");
            writer.addHeaderAlias("openId", "openId");
            writer.addHeaderAlias("eventTime", "eventTime");
            writer.addHeaderAlias("reqId", "reqId");
            writer.addHeaderAlias("adType", "adType");
            writer.addHeaderAlias("linkId", "linkId");
            writer.addHeaderAlias("logId", "logId");
            writer.addHeaderAlias("createdAt", "创建时间");
            writer.setOnlyAlias(true);
            if (CollUtil.isNotEmpty(download)) {
                //数据放进去
                writer.write(download, true);
            }
            writer.renameSheet(0, "cost记录导出");
            String fileName = URLEncoder.encode("cost记录导出", StandardCharsets.UTF_8.toString());
            writer.flush(file);
            String url =  uploadOSS(commonRepository.findOne(71).getValue(), Files.newInputStream(file.toPath()), fileName + ".xlsx");
            log.info("download url:{}", url);
            return Result.success().setData(url);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
//            boolean ignored = file.delete();
        }
        return Result.error();
    }

    private String uploadOSS(String bucketName, InputStream inputStream, String fileName) {
        String value = commonRepository.findOne(234).getValue();
        OSSClient client = new OSSClient(commonRepository.findOne(68).getValue(), commonRepository.findOne(69).getValue(), commonRepository.findOne(70).getValue());
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newFileName = RandomUtil.randomString(10) + "." + prefix;
        String url = commonRepository.findOne(19).getValue()+"/img/" + newFileName;
        try {
            client.putObject(bucketName, newFileName, inputStream);
            return url;
        } catch (OSSException oe) {
            System.out.println(oe.getMessage());
        } catch (ClientException ce) {
            System.out.println(ce.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            client.shutdown();
        }
        throw new OSSException("upload error...");
    }

    @Override
    public Result insert(AdMaEcpm adMaEcpm) {
        int rows = baseMapper.insert(adMaEcpm);
        if (rows > 0) {
            return Result.success("添加成功！");
        } else {
            return Result.error("添加失败");
        }
    }

    @Override
    public Result update(AdMaEcpm adMaEcpm) {
        QueryWrapper<AdMaEcpm> whereWrapper = new QueryWrapper<>();
        whereWrapper.eq("id", adMaEcpm.getId());
        int rows = baseMapper.update(adMaEcpm, whereWrapper);
        if (rows > 0) {
            return Result.success("更新成功！");
        } else {
            return Result.error("更新失败");
        }
    }

    @Override
    public List<AdMaEcpm> selectList(AdMaEcpmListReq req) {
        List<AdMaEcpm> adMaEcpmList = baseMapper.selectList(new QueryWrapper<AdMaEcpm>().lambda()
                .eq(StrUtil.isNotBlank(req.getLinkId()), AdMaEcpm::getLinkId, req.getLinkId())
                .eq(StrUtil.isNotBlank(req.getOpenId()), AdMaEcpm::getOpenId, req.getOpenId())
                .eq(req.getIsHandle() != null, AdMaEcpm::getIsHandle, req.getIsHandle())
                .gt(null != req.getCreatedStart(), AdMaEcpm::getCreatedAt, req.getCreatedStart())
                .lt(null != req.getCreatedEnd(), AdMaEcpm::getCreatedAt, req.getCreatedEnd())
        );
        return adMaEcpmList;
    }

}
