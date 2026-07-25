package com.sqx.modules.integral.service.Impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.RedisUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.common.dao.CommonInfoDao;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.integral.dao.UserIntegralDetailsDao;
import com.sqx.modules.integral.entity.UserIntegral;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.integral.service.UserIntegralService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserIntegralDetailsServiceImpl extends ServiceImpl<UserIntegralDetailsDao, UserIntegralDetails> implements UserIntegralDetailsService {

    private static final String USER_SIGN_DAY_PREFIX = "USER_SIGN_DAY_";
    private static final String USER_TASK_DAY_PREFIX = "USER_TASK_DAY_";

    private final UserIntegralDetailsDao userIntegralDetailsDao;
    private final UserIntegralService userIntegralService;
    private final CommonInfoService commonInfoService;
    private final UserMoneyService userMoneyService;
    private final UserMoneyDetailsService userMoneyDetailsService;
    private final RedisUtils redisUtils;
    private final CommonInfoDao commonInfoDao;

    @Override
    public IPage selectUserIntegralDetailsByUserId(int page, int limit, Long userId) {

        IPage<UserIntegralDetails> page1 = userIntegralDetailsDao.selectPage(new Page<>(page, limit), new QueryWrapper<UserIntegralDetails>().eq(userId != null, "user_id", userId).orderByDesc("create_time"));

        return page1;
    }
    @Override
    public IPage payDetails(int page, int limit, Long userId) {

        IPage<UserIntegralDetails> page1 = userIntegralDetailsDao.selectPage(new Page<>(page, limit),
                new QueryWrapper<UserIntegralDetails>()
                        .eq(userId != null, "user_id", userId)
                        .eq("type", UserIntegralDetails.Constants.TYPE.DECREASE)
                        .orderByDesc("create_time"));

        return page1;
    }

    @Override
    public IPage creditsExchangeList(int page, int limit, Long userId) {

        IPage<UserIntegralDetails> page1 = userIntegralDetailsDao.selectPage(new Page<>(page, limit),
                new QueryWrapper<UserIntegralDetails>()
                        .eq(userId != null, "user_id", userId)
                        .eq( null != userId, "type", UserIntegralDetails.Constants.TYPE.DECREASE)
                        .orderByDesc("create_time")
        );

        return page1;
    }


    @Override
    public Result signIn(Long userId) {
        //先判断今天是否签过到
        UserIntegralDetails userIntegralDetails1 = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, new Date());
        if (userIntegralDetails1 != null) {
            return Result.error("今天已经签到过了，请明天再来吧！");
        }
        userIntegralService.selectById(userId);
        //每周初始积分
        CommonInfo one = commonInfoService.findOne(102);
        //累计签到叠加积分
        CommonInfo two = commonInfoService.findOne(103);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        //获取当前日期时第几天  第一天则重新开始计时
        int num = 0;
        int day = 1;
        UserIntegralDetails userIntegralDetails2 = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, cal.getTime());
        if (userIntegralDetails2 == null) {
            num = Integer.parseInt(one.getValue());
        } else {
            if (userIntegralDetails2.getDay() == 7) {
                num = Integer.parseInt(one.getValue());
            } else {
                num = userIntegralDetails2.getNum() + Integer.parseInt(two.getValue());
                day = userIntegralDetails2.getDay() + 1;
            }

        }

        saveSignIntegralDetail(userId, num, day);

        return Result.success("签到成功，获得" + num + "积分");
    }

    @Override
    public Result signInNew(Long userId, int signDayId) {
        if (userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, new Date()) != null) {
            return Result.error("今天已经签到过了，请明天再来吧！");
        }

        UserIntegral userIntegral = userIntegralService.selectById(userId);
        if (userIntegral == null) {
            return Result.error("用户不存在！");
        }

        CommonInfo one = commonInfoService.findOne(signDayId);
        saveSignIntegralDetail(userId, Integer.parseInt(one.getValue()), Integer.parseInt(one.getMax()));

        return Result.success("签到成功，获得" + one.getValue() + "积分");

    }

    private void saveSignIntegralDetail(Long userId, int num, int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        userIntegralService.updateIntegral(UserIntegralDetails.Constants.TYPE.INCREASE, userId, num);
        UserIntegralDetails userIntegralDetails = new UserIntegralDetails();
        userIntegralDetails.setClassify(UserIntegralDetails.Constants.CLASSIFY.SIGN_IN);
        userIntegralDetails.setContent("签到获得:" + num + "积分");
        userIntegralDetails.setCreateTime(sdf.format(new Date()));
        userIntegralDetails.setNum(num);
        userIntegralDetails.setType(UserIntegralDetails.Constants.TYPE.INCREASE);
        userIntegralDetails.setUserId(userId);
        userIntegralDetails.setDay(day);
        userIntegralDetailsDao.insert(userIntegralDetails);
    }

    @Override
    public Result selectIntegralDay(Long userId) {
        Calendar cal = Calendar.getInstance();
        UserIntegralDetails nowIntegral = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        UserIntegralDetails yesterdayIntegral = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, cal.getTime());
        Map<String, Object> result = new HashMap<>();
        result.put("nowIntegral", nowIntegral);
        result.put("yesterdayIntegral", yesterdayIntegral);
        return Result.success().put("data", result);
    }

    @Override
    public Result selectIntegralDayNew(Long userId) {
        UserIntegralDetails lastIntegral = userIntegralDetailsDao.selectLastUserIntegralDetails(userId);
        UserIntegralDetails nowIntegral = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, DateUtil.date());
        Map<String, Object> result = new HashMap<>();
        result.put("nowIntegral", nowIntegral);
        result.put("lastIntegral", lastIntegral);
        return Result.success().put("data", result);
    }

    @Override
    public Result creditsExchange(Long userId, Integer integral) {
        UserIntegral userIntegral = userIntegralService.selectById(userId);
        CommonInfo one = commonInfoService.findOne(104);
        if (userIntegral.getIntegralNum() >= integral) {
            BigDecimal money = BigDecimal.valueOf(integral).divide(new BigDecimal(one.getValue())).setScale(2, BigDecimal.ROUND_DOWN);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userIntegralService.updateIntegral(2, userId, integral);
            UserIntegralDetails userIntegralDetails = new UserIntegralDetails();
            userIntegralDetails.setClassify(UserIntegralDetails.Constants.CLASSIFY.COST);
            userIntegralDetails.setContent("积分兑换金额，消耗积分：" + integral + ",兑换金额：" + money);
            userIntegralDetails.setCreateTime(sdf.format(new Date()));
            userIntegralDetails.setNum(integral);
            userIntegralDetails.setType(UserIntegralDetails.Constants.TYPE.DECREASE);
            userIntegralDetails.setUserId(userId);
            userIntegralDetailsDao.insert(userIntegralDetails);
            double v = Double.parseDouble(String.valueOf(money));
            userMoneyService.updateMoney(1, userId, v);
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setTitle("[积分]积分兑换");
            userMoneyDetails.setContent("增加金额:" + money);
            userMoneyDetails.setType(1);
            userMoneyDetails.setMoney(BigDecimal.valueOf(v));
            userMoneyDetails.setCreateTime(sdf.format(new Date()));
            userMoneyDetailsService.save(userMoneyDetails);
            return Result.success("积分兑换成功！");
        } else {
            return Result.error("积分数量不足！");
        }
    }

    @Override
    public Result creditsExchange(UserIntegralDetails userIntegralDetails) {
        Long userId = userIntegralDetails.getUserId();
        Integer integral = userIntegralDetails.getNum();
        userIntegralDetails.setCreateTime(DateUtil.now());

        UserIntegral userIntegral = userIntegralService.selectById(userId);
        Integer type = userIntegralDetails.getType();

        if (UserIntegralDetails.Constants.TYPE.INCREASE.equals(type)) {
            userIntegralService.updateIntegral(UserIntegralDetails.Constants.TYPE.INCREASE, userId, integral);
            userIntegralDetailsDao.insert(userIntegralDetails);
            return Result.success("积分获取成功！");
        }

        if (userIntegral.getIntegralNum() >= integral) {
            userIntegralDetailsDao.insert(userIntegralDetails);
            userIntegralService.updateIntegral(UserIntegralDetails.Constants.TYPE.DECREASE, userId, integral);
            return Result.success("积分兑换成功！");
        } else {
            return Result.error("积分数量不足！");
        }
    }

    @Override
    public Result completeTask(Integer taskId, Long userId) {
        if (taskId == 824) {
            // 824 任务需要完成 10 次短剧的点赞，这里单独处理逻辑。
            // 巨丑陋，但是我懒得改了，以后有时间了再改
            String count = redisUtils.get(USER_TASK_DAY_PREFIX + userId + "_" + 824);
            if (StrUtil.isBlank(count)) {
                count = "0";
            }

            int countI = Integer.parseInt(count);

            if (10 == countI) {
                return Result.error("今天已经执行过任务了！");
            }

            countI++;

            redisUtils.set(USER_TASK_DAY_PREFIX + userId + "_" + 824, countI);
            // 加完之后还不等于，相当于未完成
            if (10 != countI) {
                return Result.success();
            }

        } else if (StrUtil.isNotBlank(redisUtils.get(USER_TASK_DAY_PREFIX + userId + "_" + taskId))) {
            return Result.error("今天已经执行过任务了！");
        }

        CommonInfo one = commonInfoDao.findOne(taskId);
        if (null == one) {
            return Result.error("任务不存在");
        }

        /*
        // 获取积分
        UserIntegralDetails userIntegralDetails = new UserIntegralDetails();
        userIntegralDetails.setContent("完成每日任务 " + one.getMin() + " 获得:" + one.getValue()+"积分");
        userIntegralDetails.setClassify(UserIntegralDetails.Constants.CLASSIFY.TASK_COMPLETE);
        userIntegralDetails.setType(UserIntegralDetails.Constants.TYPE.INCREASE);
        userIntegralDetails.setNum(Integer.valueOf(one.getValue()));
        userIntegralDetails.setUserId(userId);
        userIntegralDetails.setCreateTime(DateUtil.now());
        */
        Boolean updateRes = userMoneyService.updateMoneyByUserId(userId, UserMoneyDetails.Constant.Type.RECHARGE, Double.parseDouble(one.getValue())
                , "任务完成", "任务 " + one.getMin() + "完成，奖励 " + one.getValue() + " 看点");

        if (taskId != 824) {
            // 非 824 的任务，redis 里面放值
            redisUtils.set(USER_TASK_DAY_PREFIX + userId + "_" + taskId, "已执行");
        }
        return Result.success().put("data", updateRes);
    }


    @Override
    public Result isCompleteTask(Integer taskId, Long userId) {
        if (taskId != 824) {
            return Result.success().put("data", StrUtil.isNotBlank(redisUtils.get(USER_TASK_DAY_PREFIX + userId + "_" + taskId)));
        }
        return Result.success().put("data", redisUtils.get(USER_TASK_DAY_PREFIX + userId + "_" + taskId));
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void removeAllTaskUser() {
        redisUtils.deleteAll(USER_TASK_DAY_PREFIX);
    }
}