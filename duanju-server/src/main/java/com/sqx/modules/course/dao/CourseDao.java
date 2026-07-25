package com.sqx.modules.course.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.course.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CourseDao extends BaseMapper<Course> {

    int updateDelete(@Param("id") Long id);

    IPage<Map<String, Object>> selectCourse(Page<Map<String, Object>> pages, @Param("classifyId") Long classifyId,
                                            @Param("title") String title,@Param("isRecommend") Integer isRecommend,
                                            @Param("status") Integer status,@Param("bannerId") Long bannerId,
                                            @Param("sort") Integer sort,@Param("startTime") String startTime,
                                            @Param("endTime") String endTime,@Param("userId") Long userId,
                                            @Param("isPrice") Integer isPrice);

    IPage<Map<String, Object>> selectCourseAdmin(Page<Map<String, Object>> pages, @Param("classifyId") Long classifyId,
                                            @Param("title") String title,@Param("isRecommend") Integer isRecommend,
                                            @Param("status") Integer status,@Param("bannerId") Long bannerId,
                                            @Param("sort") Integer sort,@Param("startTime") String startTime,
                                            @Param("endTime") String endTime,@Param("userId") Long userId,
                                            @Param("isPrice") Integer isPrice);

    /**
     * 根据title 模糊查询短剧
     * @param pages
     * @param title
     * @return
     */
    IPage<Map<String, Object>> selectCourseTitle(Page<Map<String, Object>> pages, @Param("title")String title);

    /**
     * 查询过期课程
     * @return
     */
    List<Course> selectOutDateCourse();

    /**
     * 查询所有带微信 mediaId 的初始化课程
     * @return
     */
    List<Course> selectAllWxMediaCourse();

    Course selectCourseInfoByWxMediaId(Long id);
}
