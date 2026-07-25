package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.course.entity.CourseClassification;
import com.sqx.modules.course.response.ClassificationResponse;
import com.sqx.modules.course.response.CurriculumResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseClassificationDao extends BaseMapper<CourseClassification> {
    IPage<Map<String, Object>> selectCourseClassification(Page<Map<String, Object>> pages, @Param("classificationName") String classificationName);

    int updateDelete(@Param("id") Long id);

    /**
     * 查询短剧的分类信息
     */
    List<ClassificationResponse> queryClassification();

    /**
     * 查询推荐短剧信息
     */
    List<CurriculumResponse> queryCurriculum(String limit);
}
