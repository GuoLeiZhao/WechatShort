package com.sqx.modules.course.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description course_details  短剧目录
 */
@Data
public class CourseDetailsMulti implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 短剧id
     */
    private Long courseId;

    /**
     * 封面图
     */
    private String titleImg;

    /**
     * 视频地址
     */
    private List<Url> videoUrls;

    /**
     * 价格
     */
    private BigDecimal price;
    
    /**
     * 积分价格
     */
    private BigDecimal jifen;

    /**
     * 是否收费 1是 2免费
     */
    private Integer isPrice;

    public CourseDetailsMulti() {}

    @Data
    public static class Url implements Serializable {
        private static final long serialVersionUID = 1867635732891863061L;

        private String url;
        private String fileName;
    }
}
