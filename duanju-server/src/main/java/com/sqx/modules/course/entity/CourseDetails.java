package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description course_details  短剧目录
 */
@Data
@TableName("course_details")
public class CourseDetails implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 短剧目录id
     */
    @TableId(type = IdType.AUTO)
    private Long courseDetailsId;

    /**
     * 短剧id
     */
    private Long courseId;

    @TableField(exist = false)
    private Course course;

    @TableField(exist = false)
    private String title;

    /**
     * 封面图
     */
    private String titleImg;

    /**
     * 介绍
     */
    private String content;

    /**
     * 短剧名称
     */
    private String courseDetailsName;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 点赞数
     */
    private Integer goodNum;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 积分价格
     */
    private BigDecimal jifen;

    /**
     * 是否推荐 1是 2否
     */
    private Integer good;

    /**
     * 是否收费 1是 2免费
     */
    private Integer isPrice;
    /**
     * 微信视频id
     */
    private Long wxMediaId;

    /**
     * 抖音剧id
     */
    @TableField(exist = false)
    private String ttAlbumId;

    /**
     * 抖音剧集id
     */
    private String ttEpisodeId;

    @TableField(exist = false)
    private Integer isCollect;

    @TableField(exist = false)
    private Integer isGood;

    @TableField(exist = false)
    private Integer courseDetailsCount;

    /**
     * 免费最小集
     */
    @TableField(exist = false)
    private String min;

    /**
     * 免费最大集
     */
    @TableField(exist = false)
    private String max;

    public CourseDetails() {}

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCourseVideoDto implements Serializable {
        private static final long serialVersionUID = -1079282679105785609L;

        private String name;
        private String videoUrl;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCourseMediaIdDto implements Serializable {
        private static final long serialVersionUID = -1079282679105785609L;

        private String name;
        private Long mediaId;
    }
}
