package com.sqx.modules.course.service.impl;

import com.sqx.modules.course.dao.CourseDao;
import com.sqx.modules.course.dao.CourseDetailsDao;
import com.sqx.modules.course.entity.CourseDetails;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.wechat.request.ListMediaRequest;
import com.sqx.modules.wechat.response.ListMediaResponse;
import com.sqx.modules.wechat.service.impl.WechatMpServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private CourseDetailsDao courseDetailsDao;

    @Autowired
    private WechatMpServiceImpl wechatMpService;

}