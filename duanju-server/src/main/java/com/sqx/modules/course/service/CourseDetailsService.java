package com.sqx.modules.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.course.entity.CourseDetails;
import com.sqx.modules.course.entity.CourseDetailsMulti;

public interface CourseDetailsService extends IService<CourseDetails> {
    Result insert(CourseDetails courseDetails);

    /**
     * 批量插入
     * @param courseDetailsMulti
     * @return
     */
    Result insertMulti(CourseDetailsMulti courseDetailsMulti);

    /**
     * 更新课程详情
     * @param courseDetails
     * @return
     */
    Result updateCourseDetails(CourseDetails courseDetails);

    /**
     * 删除课程详情
     * @param ids
     * @return
     */
    Result deleteCourseDetails(String ids);

    /**
     * 根据id查询课程详情
     * @param id
     * @param token
     * @return
     */
    Result selectCourseDetailsById(Long id,String token);

    /**
     * 查询课程详情列表
     * @param page
     * @param limit
     * @param token
     * @return
     */
    Result selectCourseDetailsList(Integer page,Integer limit,String token);

    Result selectCourseDetailsByCourseName(Long dramaId, String name);
}
