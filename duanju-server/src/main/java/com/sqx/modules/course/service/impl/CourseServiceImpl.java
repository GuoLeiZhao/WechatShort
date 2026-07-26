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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseServiceImpl extends ServiceImpl<CourseDao, Course> implements CourseService {

    public static final String COURSE_UPDATE_LOCK_KEY = "course:update:lock:";

    /**
     * 微信媒资分页每页条数，微信侧上限就是 100
     */
    private static final int WX_MEDIA_PAGE_SIZE = 100;

    /**
     * 微信媒资最多拉取页数，防止接口异常时死循环
     */
    private static final int WX_MEDIA_MAX_PAGE = 50;

    /**
     * 微信媒资文件名里剧名和集名的分隔符，如「我的演艺 - 第1集」
     */
    private static final String WX_MEDIA_NAME_SEPARATOR = " - ";

    /**
     * 从媒资文件名里抽集号的正则
     */
    private static final Pattern EPISODE_NO_PATTERN = Pattern.compile("\\d+");

    /**
     * 媒资文件名结尾的扩展名，解析集号前要先去掉
     */
    private static final Pattern FILE_EXTENSION_PATTERN = Pattern.compile("\\.[A-Za-z0-9]{2,5}$");

    /**
     * IAA（看广告解锁）模式下免费的集数：前 N 集免费，其余锁定靠看广告解锁。
     * 注意后端算免费区间的 queryFreeMaxMin 用的是 varchar 的 min/max（字符串排序），
     * 这个值超过 9 会算错（如 10 集会得到 '9'），要调大得先修那条 SQL。
     */
    private static final int IAA_FREE_EPISODE_COUNT = 1;

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

    @Override
    public Result syncWechatDrama() {
        List<ListMediaResponse.MediaInfo> allMedia;
        try {
            allMedia = listAllWxMedia();
        } catch (Exception e) {
            log.error("拉取微信媒资失败", e);
            return Result.error("拉取微信媒资失败：" + e.getMessage());
        }
        if (CollUtil.isEmpty(allMedia)) {
            return Result.error("微信媒资库里没有查到视频，请先在微信公众平台上传剧集");
        }

        List<String> warnings = new ArrayList<>();
        // dramaId 为 0 表示这条媒资还没归到任何剧目下，同步不了
        long noDramaCount = allMedia.stream().filter(media -> media.getDramaId() <= 0).count();
        if (noDramaCount > 0) {
            warnings.add("有 " + noDramaCount + " 个视频未归属到任何剧目，已跳过");
        }
        Map<Integer, List<ListMediaResponse.MediaInfo>> mediaByDrama = allMedia.stream()
                .filter(media -> media.getDramaId() > 0)
                .collect(Collectors.groupingBy(ListMediaResponse.MediaInfo::getDramaId, LinkedHashMap::new, Collectors.toList()));

        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        List<Map<String, Object>> report = new ArrayList<>();
        int totalInserted = 0;
        int totalUpdated = 0;

        for (Map.Entry<Integer, List<ListMediaResponse.MediaInfo>> entry : mediaByDrama.entrySet()) {
            long dramaId = entry.getKey();
            List<ListMediaResponse.MediaInfo> mediaList = entry.getValue();

            Course course = baseMapper.selectOne(new QueryWrapper<Course>().lambda()
                    .eq(Course::getWxMediaId, dramaId)
                    .eq(Course::getIsDelete, 0)
                    .orderByAsc(Course::getCourseId)
                    .last("limit 1"));
            boolean isNewCourse = course == null;
            if (isNewCourse) {
                course = buildCourseFromMedia(dramaId, mediaList, now);
                baseMapper.insert(course);
            } else if (StringUtils.isBlank(course.getCourseLabel())) {
                // 老剧只在标签本来就空着时补一次，不覆盖后台填过的文案
                Course patch = new Course();
                patch.setCourseId(course.getCourseId());
                patch.setCourseLabel(mediaList.size() + "集全");
                baseMapper.updateById(patch);
                course.setCourseLabel(patch.getCourseLabel());
            }

            // 已有的集按集号建索引，同步是幂等的：同名集只补 wx_media_id，不重复建
            Map<String, CourseDetails> existsByName = new HashMap<>();
            for (CourseDetails exist : courseDetailsDao.selectByCourseId(course.getCourseId())) {
                if (StringUtils.isNotBlank(exist.getCourseDetailsName())) {
                    existsByName.putIfAbsent(exist.getCourseDetailsName().trim(), exist);
                }
            }

            int inserted = 0;
            int updated = 0;
            int unchanged = 0;
            int auditPassed = 0;
            int auditPending = 0;
            int auditRejected = 0;
            for (ListMediaResponse.MediaInfo media : mediaList) {
                int auditStatus = media.getAuditDetail() == null ? 0 : media.getAuditDetail().getStatus();
                if (auditStatus == 3) {
                    auditPassed++;
                } else if (auditStatus == 1) {
                    auditPending++;
                } else {
                    auditRejected++;
                }

                String episodeNo = parseEpisodeNo(media.getName());
                if (episodeNo == null) {
                    warnings.add("剧目 " + dramaId + " 的视频「" + media.getName() + "」解析不出集号，已跳过");
                    continue;
                }

                CourseDetails exist = existsByName.get(episodeNo);
                if (exist == null) {
                    CourseDetails details = new CourseDetails();
                    details.setCourseId(course.getCourseId());
                    details.setCourseDetailsName(episodeNo);
                    details.setWxMediaId(media.getMediaId());
                    details.setCreateTime(now);
                    details.setGoodNum(0);
                    details.setGood(2);
                    details.setPrice(BigDecimal.ZERO);
                    details.setJifen(BigDecimal.ZERO);
                    // 前 IAA_FREE_EPISODE_COUNT 集免费(2)，其余锁定(1)，锁定的集靠看广告解锁
                    details.setIsPrice(isFreeEpisode(episodeNo) ? 2 : 1);
                    // 直接走 dao，绕开 CourseDetailsService.insert 里的抖音上传
                    courseDetailsDao.insert(details);
                    existsByName.put(episodeNo, details);
                    inserted++;
                } else if (!Long.valueOf(media.getMediaId()).equals(exist.getWxMediaId())) {
                    // 只更新 wx_media_id，其余字段为 null 不参与更新，不动后台已配好的价格封面
                    CourseDetails patch = new CourseDetails();
                    patch.setCourseDetailsId(exist.getCourseDetailsId());
                    patch.setWxMediaId(media.getMediaId());
                    courseDetailsDao.updateById(patch);
                    exist.setWxMediaId(media.getMediaId());
                    updated++;
                } else {
                    unchanged++;
                }
            }
            totalInserted += inserted;
            totalUpdated += updated;

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("dramaId", dramaId);
            row.put("courseId", course.getCourseId());
            row.put("title", course.getTitle());
            row.put("isNew", isNewCourse ? 1 : 0);
            row.put("mediaCount", mediaList.size());
            row.put("inserted", inserted);
            row.put("updated", updated);
            row.put("unchanged", unchanged);
            row.put("auditPassed", auditPassed);
            row.put("auditPending", auditPending);
            row.put("auditRejected", auditRejected);
            report.add(row);
        }

        String msg = "同步完成：" + report.size() + " 部剧，新增 " + totalInserted + " 集，更新 " + totalUpdated + " 集";
        return Result.success(msg).put("data", report).put("warnings", warnings);
    }

    /**
     * 不传 dramaId 调 listmedia，分页拉取小程序账号下的全部媒资
     */
    private List<ListMediaResponse.MediaInfo> listAllWxMedia() {
        List<ListMediaResponse.MediaInfo> all = new ArrayList<>();
        for (int page = 0; page < WX_MEDIA_MAX_PAGE; page++) {
            ListMediaRequest request = new ListMediaRequest();
            request.setLimit(WX_MEDIA_PAGE_SIZE);
            request.setOffset(page * WX_MEDIA_PAGE_SIZE);
            List<ListMediaResponse.MediaInfo> mediaInfoList = wechatMpService.listMedia(request).getMediaInfoList();
            if (CollUtil.isEmpty(mediaInfoList)) {
                break;
            }
            all.addAll(mediaInfoList);
            if (mediaInfoList.size() < WX_MEDIA_PAGE_SIZE) {
                break;
            }
        }
        return all;
    }

    /**
     * 用媒资信息拼一部新剧。默认下架 + 免费，封面简介等补齐后再在后台手动上架
     */
    private Course buildCourseFromMedia(long dramaId, List<ListMediaResponse.MediaInfo> mediaList, String now) {
        String title = null;
        for (ListMediaResponse.MediaInfo media : mediaList) {
            title = parseDramaTitle(media.getName());
            if (StringUtils.isNotBlank(title)) {
                break;
            }
        }
        if (StringUtils.isBlank(title)) {
            title = "微信剧目" + dramaId;
        }

        Course course = new Course();
        course.setTitle(title);
        // 小程序首页渲染的是「全集·{courseLabel}」，不填会显示成「全集·null」
        course.setCourseLabel(mediaList.size() + "集全");
        course.setWxMediaId(dramaId);
        course.setCourseType(1);
        course.setStatus(2);
        course.setIsDelete(0);
        course.setIsOver(0);
        // 必须是 1（收费）。填 2 的话 WechatDramaPlayerServiceImpl 会直接走「全集免费」分支，
        // 一集都不锁，看广告解锁永远触发不了
        course.setIsPrice(1);
        course.setPrice(BigDecimal.ZERO);
        course.setJifen(BigDecimal.ZERO);
        course.setPayNum(0);
        course.setViewCounts(0);
        course.setIsRecommend(0);
        course.setCreateTime(now);
        course.setUpdateTime(now);
        return course;
    }

    /**
     * 这一集是否属于免费集。集号解析不出数字时按锁定处理，宁可少放不可多放
     */
    private boolean isFreeEpisode(String episodeNo) {
        try {
            return Integer.parseInt(episodeNo) <= IAA_FREE_EPISODE_COUNT;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 从媒资文件名解析剧名，如「我的演艺 - 第1集」→「我的演艺」；没有分隔符则返回 null
     */
    private String parseDramaTitle(String mediaName) {
        if (StringUtils.isBlank(mediaName)) {
            return null;
        }
        int index = mediaName.lastIndexOf(WX_MEDIA_NAME_SEPARATOR);
        if (index <= 0) {
            return null;
        }
        return mediaName.substring(0, index).trim();
    }

    /**
     * 从媒资文件名解析集号，取分隔符后半段里第一组数字，如「我的演艺 - 第12集.mp4」→「12」；解析不出返回 null
     */
    private String parseEpisodeNo(String mediaName) {
        if (StringUtils.isBlank(mediaName)) {
            return null;
        }
        int index = mediaName.lastIndexOf(WX_MEDIA_NAME_SEPARATOR);
        String tail = index < 0 ? mediaName : mediaName.substring(index + WX_MEDIA_NAME_SEPARATOR.length());
        // 先去掉扩展名，否则 .mp4 里的 4 会被当成集号
        tail = FILE_EXTENSION_PATTERN.matcher(tail).replaceAll("");
        Matcher matcher = EPISODE_NO_PATTERN.matcher(tail);
        if (!matcher.find()) {
            return null;
        }
        String episodeNo = matcher.group();
        // course_details_name 存纯数字，后端多处直接 Integer.parseInt，这里顺手去掉前导零
        try {
            return String.valueOf(Integer.parseInt(episodeNo));
        } catch (NumberFormatException e) {
            // 数字长到超出 int，文件名多半不是集号，原样存下让后台能看出来
            return episodeNo;
        }
    }

}
