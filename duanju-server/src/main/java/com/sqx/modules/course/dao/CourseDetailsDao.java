package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.course.entity.CourseDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseDetailsDao extends BaseMapper<CourseDetails> {

    List<CourseDetails> findByCourseId(@Param("id") Long id,@Param("userId") Long userId);
    List<CourseDetails> selectByCourseId(@Param("id") Long id);

    IPage<CourseDetails> selectCoursePageByCourseId(Page<CourseDetails> page, @Param("id") Long id,@Param("good") Integer good);

    List<CourseDetails> findByCourseIdNotUrl(@Param("id") Long id,@Param("userId") Long userId);

    int deleteCourseDetails(String[] ids);

    IPage<CourseDetails> selectCourseDetailsList(Page<CourseDetails> page);

    /**
     * 更新课程详情
     * @param list
     * @param courseId
     * @return
     */
    int updateVideoByCourseIdAndName(List<CourseDetails.UpdateCourseVideoDto> list, @Param("courseId") Long courseId);
    int updateWxMediaIdByCourseIdAndName(List<CourseDetails.UpdateCourseMediaIdDto> list, @Param("courseId") Long courseId);

    CourseDetails queryFreeMaxMin(@Param("courseId") Long courseId);
}
