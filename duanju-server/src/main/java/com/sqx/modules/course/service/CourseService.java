package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.Course;

import java.math.BigDecimal;

public interface CourseService extends IService<Course> {
    Result insertCourse(Course course);

    Result updateCourse(Course course);

    Result updateDelete(Long id);

    Result selectCourse(Integer page, Integer limit, Long classifyId, String title,Integer isRecommend,
                        Integer status,Long bannerId,Integer sort,String token, Integer isPrice,Integer admin);

    Result selectCourseById(Integer page,Integer limit,Long id,Integer good);

    Result selectCourseTitle(Integer page, Integer limit, String title, Long userId);

    Result synCourse();

    Result updateCourseDetails(String ids, BigDecimal price, BigDecimal jifen, String content, String titleImg);

    Result updateCourseStatus(String ids, Integer status);

    Result deleteCourseByIds(String ids);

    Result deleteCourseDetailsByIds(String ids);

    Result courseNotify(Long userId, Long courseId, Long courseDetailsId);


    void refreshWechatVideoUrl();

    void initWxCourseMedia();

    Result selectCount();

}
