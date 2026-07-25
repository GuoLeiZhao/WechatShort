package com.sqx.modules.applog;

import com.sqx.common.utils.Result;
import com.sqx.modules.applog.entity.Log;
import com.sqx.modules.applog.service.LogService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "小程序日志", tags = {"日志"})
@RequestMapping(value = "/app_log")
@Slf4j
public class AppLogController {

    @Autowired
    private LogService logService;

    @PostMapping()
    public Result insert(@RequestBody Log data){
        log.info("data: {}", data);
        logService.save(data);
        return Result.success();
    }
}
