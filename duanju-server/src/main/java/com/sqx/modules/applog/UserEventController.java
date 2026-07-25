package com.sqx.modules.applog;

import com.sqx.common.utils.Result;
// import com.sqx.modules.applog.service.MeterService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(value = "小程序用户事件埋点", tags = {"用户事件埋点"})
@RequestMapping(value = "/user_event")
@Slf4j
public class UserEventController {

    // @Autowired
    // private MeterService meterService;

    // @Autowired
    // private UserEventService userEventService;

    // @PostMapping("/record")
    // public Result record(@RequestBody List<RecordRequest> requestList){
    //
    //     meterService.recordUserEvent(requestList);
    //
    //     log.info("记录{}", requestList.size());
    //
    //
    //     return Result.success();
    // }

}
