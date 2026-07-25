package com.sqx.modules.kuaishou.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.kuaishou.entity.KuaiShouWatchEntity;
import com.sqx.modules.kuaishou.service.KuaiShouWatchService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.utils.ID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "快手监测", tags = {"快手监测"})
@RequestMapping(value = "/kuaishou/watch")
@RequiredArgsConstructor
public class KuaiShouController extends AbstractController {

    private final KuaiShouWatchService kuaiShouWatchService;

    @GetMapping("/callback")
    @ApiOperation("回调")
    public Result insert(KuaiShouWatchEntity kuaiShouWatchEntity) {
        kuaiShouWatchEntity.setId(ID.get());
        return Result.success().setData(kuaiShouWatchService.save(kuaiShouWatchEntity));
    }

}
