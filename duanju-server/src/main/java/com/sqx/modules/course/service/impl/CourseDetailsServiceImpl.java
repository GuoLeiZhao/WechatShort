package com.sqx.modules.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.utils.JwtUtils;
import com.sqx.modules.course.dao.CourseCollectDao;
import com.sqx.modules.course.dao.CourseDao;
import com.sqx.modules.course.dao.CourseDetailsDao;
import com.sqx.modules.course.dao.CourseUserDao;
import com.sqx.modules.course.entity.*;
import com.sqx.modules.course.service.CourseDetailsService;
import com.sqx.modules.douyin.request.UploadTTVideoRequest;
import com.sqx.modules.douyin.response.UploadTTVideoResponse;
import com.sqx.modules.douyin.service.DouyinBURDService;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CourseDetailsServiceImpl  extends ServiceImpl<CourseDetailsDao, CourseDetails> implements CourseDetailsService {

    @Autowired
    private CourseDao courseDao;
    @Autowired
    private CourseCollectDao courseCollectDao;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CourseUserDao courseUserDao;
    @Autowired
    private UserService userService;
    @Autowired
    private DouyinBURDService douyinBURDService;


    @Override
    public Result insert(CourseDetails courseDetails) {
        if(courseDetails.getGoodNum()==null){
            courseDetails.setGoodNum(0);
        }

        // TODO 插入剧集的同时，同步抖音, 默认zhonggou小程序,由抖音服务负责。
        UploadTTVideoResponse uploadTTVideoResponse = uploadTTVideo(courseDetails);
        if (uploadTTVideoResponse != null) {
            courseDetails.setTtEpisodeId(uploadTTVideoResponse.getVideo_result().getOpen_video_id());
        }

        baseMapper.insert(courseDetails);
        return Result.success();
    }
    @Override
    public Result insertMulti(CourseDetailsMulti courseDetailsMulti) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 定义正则表达式模式，匹配连续的数字部分
        Pattern pattern = Pattern.compile("\\d+");


        courseDetailsMulti.getVideoUrls().stream().map(url -> {
            CourseDetails courseDetails = new CourseDetails();
            courseDetails.setCourseId(courseDetailsMulti.getCourseId());
            courseDetails.setTitleImg(courseDetailsMulti.getTitleImg());
            courseDetails.setCourseDetailsName(url.getFileName());
            courseDetails.setVideoUrl(url.getUrl());
            courseDetails.setPrice(courseDetailsMulti.getPrice());
            courseDetails.setJifen(courseDetailsMulti.getJifen());
            courseDetails.setIsPrice(courseDetailsMulti.getIsPrice());
            courseDetails.setCreateTime(sdf.format(new Date()));

            if(courseDetails.getGoodNum()==null){
                courseDetails.setGoodNum(0);
            }

            return courseDetails;

        }).sorted(Comparator.comparingInt(k -> {
            // 按照名称数字正序排序
            Matcher matcher = pattern.matcher(k.getCourseDetailsName());
            if (matcher.find()){
                return Integer.parseInt(matcher.group());
            }
            return 0;
        })).forEach(this::insert);
        return Result.success();
    }

    @Override
    public Result updateCourseDetails(CourseDetails courseDetails) {
        baseMapper.updateById(courseDetails);
        return Result.success();
    }

    @Override
    public Result deleteCourseDetails(String ids) {
        String[] split = ids.split(",");
        baseMapper.deleteCourseDetails(split);
        return Result.success();
    }

    @Override
    public Result selectCourseDetailsById(Long id,String token){
        Course bean = courseDao.selectById(id);
        if(bean == null){
            return Result.error("剧集不存在或已下架");
        }
        if(bean.getViewCounts()==null){
            bean.setViewCounts(1);
        }else{
            bean.setViewCounts(bean.getViewCounts()+1);
        }
        courseDao.updateById(bean);
        Long userId=null;
        if(StringUtils.isNotEmpty(token)){
            Claims claims = jwtUtils.getClaimByToken(token);
            if(claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())){
                userId=Long.parseLong(claims.getSubject());
            }
        }
        if (userId != null) {

            UserEntity userEntity = userService.selectUserById(userId);
            //查询用户是否购买了整集
            CourseUser courseUser = courseUserDao.selectCourseUser(id, userId);
            if (courseUser != null || (userEntity != null && userEntity.getMember()!=null && userEntity.getMember()==2)) {
                bean.setListsDetail(baseMapper.findByCourseId(id,userId));
            }else{
                bean.setListsDetail(baseMapper.findByCourseIdNotUrl(id,userId));
                //查询用户是否单独购买了集
                List<CourseUser> courseUsers = courseUserDao.selectCourseUserList(id, userId);
                if(!courseUsers.isEmpty()){
                    for (CourseUser courseUser1:courseUsers){
                        for (CourseDetails courseDetails:bean.getListsDetail()) {
                            if(courseUser1.getCourseDetailsId().equals(courseDetails.getCourseDetailsId())){
                                CourseDetails courseDetails1 = baseMapper.selectById(courseDetails.getCourseDetailsId());
                                courseDetails.setVideoUrl(courseDetails1.getVideoUrl());
                            }
                        }
                    }
                }
            }
            /*for (CourseDetails courseDetails:bean.getListsDetail()){
                courseDetails.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("course_details_id", courseDetails.getCourseDetailsId()).eq("classify",1)));
                courseDetails.setIsGood(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("course_details_id", courseDetails.getCourseDetailsId()).eq("classify",2)));
            }*/

        } else {
            bean.setListsDetail(baseMapper.findByCourseIdNotUrl(id,null));
        }
        return Result.success().put("data",bean);
    }

    @Override
    public Result selectCourseDetailsList(Integer page,Integer limit,String token){
        Long userId=null;
        if(StringUtils.isNotEmpty(token)){
            Claims claims = jwtUtils.getClaimByToken(token);
            if(claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())){
                userId=Long.parseLong(claims.getSubject());
            }
        }
        IPage<CourseDetails> courseDetailsIPage = baseMapper.selectCourseDetailsList(new Page<>(page, limit));

        List<CourseDetails> records = courseDetailsIPage.getRecords();
        for (CourseDetails courseDetails : records){
            if (null != userId) {
                courseDetails.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("course_details_id", courseDetails.getCourseDetailsId()).eq("classify",1)));
                courseDetails.setIsGood(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>()
                        .eq("user_id", userId).eq("course_details_id", courseDetails.getCourseDetailsId()).eq("classify",2)));
            }
            courseDetails.setCourse(courseDao.selectById(courseDetails.getCourseId()));
            courseDetails.setTitle(courseDetails.getCourse().getTitle());
            courseDetails.setCourseDetailsCount(baseMapper.selectCount(new QueryWrapper<CourseDetails>().eq("course_id",courseDetails.getCourseId())));
        }
        return Result.success().put("data",new PageUtils(courseDetailsIPage));
    }

    @Override
    public Result selectCourseDetailsByCourseName(Long dramaId, String name) {
        Course course = courseDao.selectOne(new QueryWrapper<Course>().lambda().eq(Course::getWxMediaId, dramaId).eq(Course::getIsDelete, Boolean.FALSE));
        if (null == course) {
            return Result.success();
        }
        return Result.success().put("data", baseMapper.selectOne(new QueryWrapper<CourseDetails>().lambda().eq(CourseDetails::getCourseId, course.getCourseId())
                .eq(CourseDetails::getCourseDetailsName, name)));
    }

    private UploadTTVideoResponse uploadTTVideo(CourseDetails courseDetails) {
        UploadTTVideoRequest uploadTTVideoRequest = new UploadTTVideoRequest();
        uploadTTVideoRequest.setResourceType(1);

        UploadTTVideoRequest.VideoMeta videoMeta =  new UploadTTVideoRequest.VideoMeta();
        videoMeta.setTitle(courseDetails.getCourseDetailsName());
        videoMeta.setFormat("mp4");
        videoMeta.setUrl(courseDetails.getVideoUrl());
        videoMeta.setUseDyCloud(true);

        uploadTTVideoRequest.setVideoMeta(videoMeta);

        return douyinBURDService.upLoadTTVideo("app4",uploadTTVideoRequest);
    }
}
