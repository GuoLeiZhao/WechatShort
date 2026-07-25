package com.sqx.modules.course.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.RedisUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.utils.JwtUtils;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.course.dao.CourseCollectDao;
import com.sqx.modules.course.dao.CourseDao;
import com.sqx.modules.course.dao.CourseDetailsDao;
import com.sqx.modules.course.dao.CourseUserDao;
import com.sqx.modules.course.entity.Course;
import com.sqx.modules.course.entity.CourseDetails;
import com.sqx.modules.course.entity.CourseUser;
import com.sqx.modules.course.service.CourseService;
import com.sqx.modules.course.service.CourseUserService;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.search.service.AppSearchService;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.ID;
import com.sqx.modules.wechat.request.GetMediaLinkRequest;
import com.sqx.modules.wechat.request.ListMediaRequest;
import com.sqx.modules.wechat.response.GetMediaLinkResponse;
import com.sqx.modules.wechat.response.ListMediaResponse;
import com.sqx.modules.wechat.service.WechatMpService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseDao, Course> implements CourseService {

    public static final String COURSE_UPDATE_LOCK_KEY = "course:update:lock:";

    @Autowired
    private CourseDetailsDao courseDetailsDao;
    @Autowired
    private CourseCollectDao courseCollectDao;
    @Autowired
    private CourseUserDao courseUserDao;
    @Autowired
    private AppSearchService appSearchService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private CourseUserService courseUserService;
    @Autowired
    private WechatMpService wechatMpService;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 创建线程池处理业务逻辑
     */
    private ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().build();
    private ExecutorService singleThreadPool = new ThreadPoolExecutor(30, 100,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());


    private static boolean sys = false;

    @Override
    public Result selectCount() {
        return Result.success().put("data", count(new QueryWrapper<Course>().lambda().eq(Course::getIsDelete, Boolean.FALSE)));
    }

    @Override
    public Result insertCourse(Course course) {
        //设置删除标识
        course.setIsDelete(0);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //设置创建时间
        course.setCreateTime(df.format(new Date()));
        //设置更新时间
        course.setUpdateTime(df.format(new Date()));
        if (course.getCourseType().equals(2) || course.getCourseType().equals(3)) {
            baseMapper.insert(course);
            CourseDetails courseDetails = new CourseDetails();
            courseDetails.setCourseId(course.getCourseId());
            courseDetails.setVideoUrl(course.getRemark());
            courseDetailsDao.insert(courseDetails);
        } else {
            baseMapper.insert(course);
        }
        return Result.success("操作成功！");
    }

    @Override
    public Result updateCourse(Course course) {
        baseMapper.updateById(course);
        return Result.success("操作成功！");
    }

    @Override
    public Result updateDelete(Long id) {
        baseMapper.updateDelete(id);
        return Result.success("操作成功！");
    }

    @Override
    public Result selectCourse(Integer page, Integer limit, Long classifyId, String title, Integer isRecommend, Integer status,
                               Long bannerId, Integer sort, String token, Integer isPrice, Integer admin) {
        Long userId = null;
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = jwtUtils.getClaimByToken(token);
            if (claims != null && !jwtUtils.isTokenExpired(claims.getExpiration())) {
                userId = Long.parseLong(claims.getSubject());
            }
        }
        Page<Map<String, Object>> pages = new Page<>(page, limit);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        // 一周第一天为周日，所以此处日+1
        calendar.setWeekDate(calendar.getWeekYear(), calendar.get(Calendar.WEEK_OF_YEAR), 2);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);
        String startTime = sdf.format(calendar.getTime());
        // 一周第一天为周日，所以此处为下一周第一天
        calendar.setWeekDate(calendar.getWeekYear(), calendar.get(Calendar.WEEK_OF_YEAR) + 1, 1);
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                23, 59, 59);
        String endTime = sdf.format(calendar.getTime());
        if (admin == null) {
            return Result.success().put("data", new PageUtils(baseMapper.selectCourse(pages, classifyId, title, isRecommend, status, bannerId,
                    sort, startTime, endTime, userId, isPrice)));
        }
        return Result.success().put("data", new PageUtils(baseMapper.selectCourseAdmin(pages, classifyId, title, isRecommend, status, bannerId,
                sort, startTime, endTime, userId, isPrice)));
    }

    /*@Override
    public Result selectCourseById(Long id, Long userId) {
        //查询短剧信息
        Course bean = baseMapper.selectById(id);
        if (userId != null) {
            bean.setIsCollect(courseCollectDao.selectCount(new QueryWrapper<CourseCollect>().eq("user_id", userId).eq("course_id", id)));
        } else {
            bean.setIsCollect(0);
        }
        //查询用户是否购买了这本书
        CourseUser courseUser = courseUserDao.selectCourseUser(id, userId);
        Orders one = ordersService.selectOrdersByCourseIdAndUserId(userId,id);
        if (courseUser != null) {
            bean.setListsDetail(courseDetailsDao.findByCourseId(id));
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            courseUser.setUpdateTime(df.format(new Date()));
            courseUserDao.updateCourseTime(courseUser);
            bean.setIsMyCourse(2);
            bean.setOrders(one);
        }else{
            if(bean.getCourseType()==null || bean.getCourseType().equals(1)){
                bean.setListsDetail(courseDetailsDao.findByCourseIdNotUrl(id));
            }
            bean.setIsMyCourse(1);
        }
        return Result.success().put("data", bean);
    }*/

    @Override
    public Result selectCourseById(Integer page, Integer limit, Long id, Integer good) {
        Page<CourseDetails> pages = new Page<>(page, limit);
        return Result.success().put("data", new PageUtils(courseDetailsDao.selectCoursePageByCourseId(pages, id, good)));
    }

    @Override
    public Result selectCourseTitle(Integer page, Integer limit, String title, Long userId) {
        //分页
        Page<Map<String, Object>> pages = new Page<>(page, limit);
        if (userId != null) {
            //记录或更新搜索内容
            appSearchService.insetAppSearch(title, userId);
        }
        //拼接模糊查询
        String title1 = null;
        if (StringUtils.isNotBlank(title)) {
            title1 = "%" + title + "%";
            return Result.success().put("data", new PageUtils(baseMapper.selectCourseTitle(pages, title1)));
        } else {
            return Result.error("请输入要搜索的内容！");
        }

    }

    @Override
    public Result synCourse() {
        if (sys) {
            return Result.error("视频正在同步中，请稍等！");
        }
        sys = true;
        singleThreadPool.submit(() -> {
            try {
                String value = commonInfoService.findOne(250).getValue();
                String s = HttpClientUtil.doGet(value);
                log.error("返回值：" + s);
                JSONArray jsonArray = JSONArray.parseArray(s);
                for (int i = 2; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String cname = jsonObject.getString("cname");
                    String name = jsonObject.getString("name");
                    String m3u8url = jsonObject.getString("m3u8url");
                    String picurl = jsonObject.getString("picurl");
                    String description = jsonObject.getString("description");
                    Course course = baseMapper.selectOne(new QueryWrapper<Course>().eq("title", cname));
                    if (course == null) {
                        course = new Course();
                        course.setTitle(cname);
                        course.setTitleImg(picurl);
                        course.setPrice(BigDecimal.ZERO);
                        course.setPayNum(0);
                        course.setImg(picurl);
                        course.setDetails(description);
                        course.setIsDelete(0);
                        course.setCreateTime(DateUtils.format(new Date()));
                        course.setUpdateTime(course.getCreateTime());
                        course.setIsRecommend(0);
                        course.setStatus(2);
                        course.setIsPrice(2);
                        course.setViewCounts(0);
                        baseMapper.insert(course);
                    } else {
                        course.setTitle(cname);
                        course.setTitleImg(picurl);
                        course.setImg(picurl);
                        course.setDetails(description);
                        baseMapper.updateById(course);
                    }
                    Integer count = courseDetailsDao.selectCount(new QueryWrapper<CourseDetails>().eq("course_details_name", name));
                    if (count == 0) {
                        CourseDetails courseDetails = new CourseDetails();
                        courseDetails.setCourseId(course.getCourseId());
                        courseDetails.setCourseDetailsName(name);
                        courseDetails.setVideoUrl(m3u8url);
                        courseDetails.setCreateTime(DateUtils.format(new Date()));
                        courseDetails.setTitleImg(picurl);
                        courseDetails.setContent(description);
                        courseDetails.setGoodNum(0);
                        courseDetails.setPrice(BigDecimal.ZERO);
                        courseDetails.setIsPrice(2);
                        courseDetailsDao.insert(courseDetails);
                    } else {
                        CourseDetails courseDetails = courseDetailsDao.selectOne(new QueryWrapper<CourseDetails>().eq("course_details_name", name).last(" limit 1"));
                        courseDetails.setCourseDetailsName(name);
                        courseDetails.setVideoUrl(m3u8url);
                        courseDetails.setCreateTime(DateUtils.format(new Date()));
                        courseDetails.setTitleImg(picurl);
                        courseDetails.setContent(description);
                        courseDetailsDao.updateById(courseDetails);
                    }
                }
            } catch (Exception e) {
                log.error("同步视频出错：" + e.getMessage(), e);
            } finally {
                sys = false;
            }
        });
        return Result.success();
    }

    @Override
    public Result updateCourseDetails(String ids, BigDecimal price, BigDecimal jifen, String content, String titleImg) {
        for (String id : ids.split(",")) {
            CourseDetails courseDetails = courseDetailsDao.selectById(Long.parseLong(id));
            courseDetails.setPrice(price);
            courseDetails.setJifen(jifen);
            if (price.doubleValue() == 0 && jifen.doubleValue() == 0) {
                courseDetails.setIsPrice(2);
            } else {
                courseDetails.setIsPrice(1);
            }
            courseDetails.setContent(content);
            courseDetails.setTitleImg(titleImg);
            courseDetailsDao.updateById(courseDetails);
        }
        return Result.success();
    }

    @Override
    public Result updateCourseStatus(String ids, Integer status) {
        for (String id : ids.split(",")) {
            Course course = baseMapper.selectById(Long.parseLong(id));
            course.setStatus(status);
            baseMapper.updateById(course);
        }
        return Result.success();
    }


    @Override
    public Result deleteCourseByIds(String ids) {
        for (String id : ids.split(",")) {
            baseMapper.deleteById(Long.parseLong(id));
            courseDetailsDao.delete(new QueryWrapper<CourseDetails>().eq("course_id", Long.parseLong(id)));
        }
        return Result.success();
    }

    @Override
    public Result deleteCourseDetailsByIds(String ids) {
        for (String id : ids.split(",")) {
            courseDetailsDao.deleteById(Long.parseLong(id));
        }
        return Result.success();
    }

    @Override
    public Result courseNotify(Long userId, Long courseId, Long courseDetailsId) {
        CourseUser courseUser = new CourseUser();
        //设置短剧id
        courseUser.setCourseId(courseId);
        courseUser.setCourseDetailsId(courseDetailsId);
        courseUser.setClassify(2);
        //设置用户id
        courseUser.setUserId(userId);

        //加入我的列表
        courseUserService.insertCourseUser(courseUser);
        return Result.success();
    }


    @Override
    public void refreshWechatVideoUrl(){
        List<Course> courses = baseMapper.selectOutDateCourse();
        if (CollUtil.isEmpty(courses)) {
            return;
        }
        courses.forEach(item -> {
            String lockKey = COURSE_UPDATE_LOCK_KEY + item.getCourseId();
            if (redisUtils.hasKey(lockKey)) {
                return;
            }

            threadPoolTaskExecutor.execute(() -> {
                // double check 看有没有上锁
                String lockValue = ID.getProcessID() + Thread.currentThread().getName();
                // 超时时间设置为 15 分钟
                if (!redisUtils.lock(lockKey, lockValue, 60 * 15)) {
                    return;
                }

            try {
                List<CourseDetails> courseDetails = courseDetailsDao.selectByCourseId(item.getCourseId());
                DateTime outDate = DateUtil.offsetHour(new Date(), 2);
                long second = outDate.getTime() / 1000;
                // 每个视频去获取临时链接，有效时间 2 小时
                List<CourseDetails.UpdateCourseVideoDto> updateCourseVideoDtos = courseDetails.stream().map(courseDetail -> {
                    GetMediaLinkResponse mediaLink = wechatMpService.getMediaLink(new GetMediaLinkRequest(courseDetail.getWxMediaId(), second));
                    // 我的演艺 - 第1集
                    String mediaName = mediaLink.getMediaInfo().getName().split(" - ")[1];
                    String name = mediaName.replace("第", "").replace("集", "").replace(".mp4", "");
                    return new CourseDetails.UpdateCourseVideoDto(name, mediaLink.getMediaInfo().getMp4Url());
                }).collect(Collectors.toList());
                // 批量更新
                courseDetailsDao.updateVideoByCourseIdAndName(updateCourseVideoDtos, item.getCourseId());

                // 更新过期时间
                item.setWxMediaOutDate(outDate);
                baseMapper.updateById(item);
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                redisUtils.unlock(lockKey, lockValue);
            }

            });
        });

    }

//    @Scheduled(fixedRate = 6000 * 10)
    public void task(){
        this.refreshWechatVideoUrl();
    }

    @Override
    public void initWxCourseMedia() {
        baseMapper.selectAllWxMediaCourse().forEach(course -> {
            long page = Long.parseLong(course.getCourseCount()) / 100;
            for (int i = 0; i < page + 1; i++) {
                // 更新每部剧的剧集 mediaId
                ListMediaResponse listMediaResponse = wechatMpService.listMedia(new ListMediaRequest(course.getWxMediaId(), 100, i));
                List<ListMediaResponse.MediaInfo> mediaInfoList = listMediaResponse.getMediaInfoList();
                List<CourseDetails.UpdateCourseMediaIdDto> updateCourseMediaIdDtos = mediaInfoList.stream().map(mediaInfo -> {
                    // 我的演艺 - 第1集
                    String mediaName = mediaInfo.getName().split(" - ")[1];
                    String name = mediaName.replace("第", "").replace("集", "").replace(".mp4", "");
                    return new CourseDetails.UpdateCourseMediaIdDto(name, mediaInfo.getMediaId());
                }).collect(Collectors.toList());

                courseDetailsDao.updateWxMediaIdByCourseIdAndName(updateCourseMediaIdDtos, course.getCourseId());
            }
            course.setWxMediaOutDate(new Date());
            baseMapper.updateById(course);
        });

        // 获取最新的播放链接
        refreshWechatVideoUrl();
        System.out.println(111);
    }

}
