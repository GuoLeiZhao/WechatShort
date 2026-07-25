package com.sqx.modules.ad.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdPlaySeries;
import com.sqx.modules.ad.request.AdPlaySeriesQueryReq;
import com.sqx.modules.ad.service.AdPlaySeriesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(value = "剧集管理", tags = {"剧集管理"})
@RequestMapping(value = "/ad_play_series")
@RequiredArgsConstructor
public class AdPlaySeriesController {
    private final AdPlaySeriesService adPlaySeriesService;

    @GetMapping()
    @ApiOperation("查询")
    public Result page(AdPlaySeriesQueryReq req) {
        return Result.success().setData(adPlaySeriesService.page(req));
    }

    @GetMapping("/dropList")
    @ApiOperation("下拉列表")
    public Result dropList(AdPlaySeriesQueryReq req) {
        if (req.getPlayId() == null) {
            return Result.success().setData(new ArrayList<>());
        }
        return Result.success().setData(adPlaySeriesService.dropList(req));
    }

    @PostMapping()
    @ApiOperation("新建")
    public Result save(@RequestBody AdPlaySeries adPlaySeries) {
        return adPlaySeriesService.insert(adPlaySeries);
    }

    @DeleteMapping()
    @ApiOperation("删除")
    public Result deleteById(String seriesId) {
        return adPlaySeriesService.deleteById(seriesId);
    }

    @PutMapping()
    @ApiOperation("编辑")
    public Result update(@RequestBody AdPlaySeries adPlaySeries) {
        return adPlaySeriesService.update(adPlaySeries);
    }

}
