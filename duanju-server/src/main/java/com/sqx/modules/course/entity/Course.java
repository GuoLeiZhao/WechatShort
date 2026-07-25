package com.sqx.modules.course.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sqx.modules.orders.entity.Orders;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description course  短剧
 */
@Data
@TableName("course")
@NoArgsConstructor
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 短剧id
     */
    @TableId(type = IdType.AUTO)
    private Long courseId;

    /**
     * 轮播图
     */
    private String bannerImg;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面图
     */
    @TableField("title_img")
    private String titleImg;

    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 积分价格
     */
    private BigDecimal jifen;

    /**
     * 上下架 1上架  2下架
     */
    private Integer status;

    /**
     * 是否完结 0未完结 1已完结
     */
    @TableField("is_over")
    private Integer isOver;

    /**
     * 分类
     */
    @TableField("classify_id")
    private Long classifyId;
    /**
     * 短剧分类对象
     */
    @TableField(exist = false)
    private CourseClassification courseClassification;

    /**
     * 购买次数
     */
    @TableField("pay_num")
    private Integer payNum;

    /**
     * 短剧标签
     */
    @TableField("course_label")
    private String courseLabel;

    /**
     * 内容图
     */
    private String img;

    /**
     * 短剧介绍
     */
    private String details;

    /**
     * 删除标识 0未删除 1已删除
     */
    @TableField("is_delete")
    private Integer isDelete;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private String updateTime;
    /**
     * 文件地址
     */
    @TableField("msg_url")
    private String msgUrl;
    /**
     * 上传方式0OSS-1本地
     */
    @TableField("msg_type")
    private Integer msgType;

    /**
     * 是否是推荐商品
     */
    @TableField("is_recommend")
    private Integer isRecommend;
    /**
     * 首页金刚区分类
     */
    private  Integer bannerId;

    /**
     * 是否收费 1是 2免费
     */
    private Integer isPrice;

    /**
     * 短剧目录
     */
    @TableField(exist = false)
    private List<CourseDetails> listsDetail;

    /**
     * 短剧分类 1短剧 2链接 3文档
     */
    private Integer courseType;

    /**
     * 播放量
     */
    private Integer viewCounts;

    /**
     * 微信媒资剧目ID
     */
    @TableField("wx_media_id")
    private Long wxMediaId;

    /**
     * 微信媒资过期时间
     */
    @TableField("wx_media_out_date")
    private Date wxMediaOutDate;

    /**
     * 抖音短剧ID
     */
    @TableField("tt_album_id")
    private String ttAlbumId;

    @TableField(exist = false)
    private String remark;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private String courseCount;

    @TableField(exist = false)
    private Integer isMyCourse;

    @TableField(exist = false)
    private Orders orders;

    @TableField(exist = false)
    private String courseDetailsName;

    @TableField(exist = false)
    private Long courseDetailsId;

    @TableField(exist = false)
    private Integer courseDetailsCount;

}
