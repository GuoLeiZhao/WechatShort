package com.sqx.modules.sys.controller.app;

import com.sqx.common.utils.IPUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.sys.entity.SysLogEntity;
import com.sqx.modules.sys.service.SysLogService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


/**
 * APP系统日志
 */
@Controller
@RequestMapping("/app/sys/log")
@RequiredArgsConstructor
public class AppSysLogController extends AbstractController {

    private final SysLogService sysLogService;


    /**
     * 埋点记录
     */
    @PostMapping
    @ApiOperation("埋点记录")
    @ResponseBody
    public Result save(@RequestBody SysLogEntity sysLogEntity, HttpServletRequest request){
        sysLogEntity.setCreateDate(new Date());
        sysLogEntity.setTime(0L);
        sysLogEntity.setType(SysLogEntity.Type.user.name());
        //设置IP地址
        sysLogEntity.setIp(IPUtils.getIpAddr(request));
        return Result.success().put("data", sysLogService.save(sysLogEntity));
    }
}
