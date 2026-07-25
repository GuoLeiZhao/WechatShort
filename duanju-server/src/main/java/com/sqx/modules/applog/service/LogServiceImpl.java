package com.sqx.modules.applog.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.applog.dao.LogDao;
import com.sqx.modules.applog.entity.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogServiceImpl extends ServiceImpl<LogDao, Log> implements LogService{
}
