package com.sqx.modules.ad.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.ad.entity.AdBlackList;
import com.sqx.modules.ad.request.AdBlackListQueryReq;
import com.sqx.modules.ad.service.AdBlackListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(value = "黑名单管理", tags = {"黑名单管理"})
@RequestMapping(value = "/ad_black_list")
@RequiredArgsConstructor
public class AdBlackListController {
    private final AdBlackListService adBlackListService;

    @GetMapping()
    @ApiOperation("查询")
    public Result page(AdBlackListQueryReq req) {
        return Result.success().setData(adBlackListService.page(req));
    }

    @GetMapping("/selectOne")
    @ApiOperation("详情")
    public Result selectOne(AdBlackList req) {
        return Result.success().setData(adBlackListService.selectOne(req));
    }

    @PostMapping()
    @ApiOperation("新建")
    public Result save(@RequestBody AdBlackList req) {
        return adBlackListService.insert(req);
    }

    @DeleteMapping()
    @ApiOperation("删除")
    public Result deleteById(Long id) {
        return adBlackListService.deleteById(id);
    }

}
