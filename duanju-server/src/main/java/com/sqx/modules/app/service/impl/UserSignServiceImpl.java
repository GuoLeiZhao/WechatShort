package com.sqx.modules.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.RedisUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserSignService;
import com.sqx.modules.common.dao.CommonInfoDao;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserSignServiceImpl implements UserSignService {

    private final RedisUtils redisUtils;
    private final CommonInfoDao commonInfoDao;
    private final UserMoneyService userMoneyService;

    @Override
    public Result signIn(Long userId) {

        // 获取当前时间
        DateTime now = DateTime.now();

        //判断两个日期相差的天数
        //  有时候我们计算相差天数的时候需要忽略时分秒。
        //  比如：2016-02-01 23:59:59和2016-02-02 00:00:00相差一秒
        //  如果isReset为 false相差天数为0。
        //  如果isReset为 true相差天数将被计算为1
        long day = DateUtil.betweenDay(DateUtil.beginOfMonth(now), now, true);
        String signKey = buildSignKey(userId, now);
        Boolean bitMap = redisUtils.getBitMap(signKey, day);

        if (null != bitMap && bitMap) {
            return Result.error("今天已经签到过了，请明天再来吧！");
        }
        try {
            // 签到
            redisUtils.setBitMap(signKey, day, Boolean.TRUE);
            int dayOfWeek = DateUtil.dayOfWeek(now);
            CommonInfo taskSignIn = commonInfoDao.findByConditionAndMax("" + dayOfWeek, "task_sign_in");

            // 用户看点增加逻辑
            Boolean updateRes = userMoneyService.updateMoneyByUserId(userId, UserMoneyDetails.Constant.Type.RECHARGE, Double.parseDouble(taskSignIn.getValue())
                    , "签到完成", "签到获得 " + taskSignIn.getValue() + " 看点");


            return Result.success().put("data", updateRes);
        } catch (Exception e) {
            // 签到
            // 如果异常，这里需要设置补偿机制
            redisUtils.setBitMap(signKey, day, Boolean.FALSE);
            return Result.success("签到失败，请重试");
        }
    }

    @Override
    public boolean checkSign(Long userId, Date date) {
        DateTime now = DateTime.now();
        long offset = DateUtil.betweenDay(DateUtil.beginOfMonth(now), now, true);
        return redisUtils.getBitMap(buildSignKey(userId, date), offset);
    }

    @Override
    public int getContinuousSignCount(Long userId, Date date) {
        // 所在月份的第几天
        int dayOfMonth = DateUtil.dayOfMonth(date);
        // 构建 key
        String key = buildSignKey(userId, date);

        int signCount = getSignCountFromRedisSingleMonth(key, dayOfMonth);
        if (dayOfMonth == signCount) {
            Date lastMonth = DateUtil.offsetMonth(date, -1);
            signCount += getContinuousSignCount(userId, lastMonth);
        }

        return signCount;
    }


    @Override
    public int getSignCountFromRedisSingleMonth(String key, int dayOfMonth) {

        // bitfield user:sign:5:202105 u14 0 ，u 表示无符号 ，14 表示今天是14号，0 表示索引，即从第一天开始
        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands
                .create()
                .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth))
                .valueAt(0);

        List<Long> list = redisUtils.bitField(key, bitFieldSubCommands);
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int signCount = 0;
        long count = list.get(0) == null ? 0 : list.get(0);

        // 移位操作：先右移再左移，结果未变则表示未签到，结果变了则表示签到了
        for (int i = dayOfMonth; i > 0; i--) { // i 表示位移的次数
            if (count >> 1 << 1 == count) {
                // 如果低位是0 且低位所在不是当天，说明连续签到中断
                if (i != dayOfMonth) break;
            } else {
                signCount++;
            }
            // 把最后一位丢弃
            count >>= 1;
        }
        return signCount;
    }

    @Override
    public Result selectSignDay(Long userId) {
        // 后续改成 redis 中判断，今天懒得改了。
        Map<String, Object> result = new HashMap<>();
        DateTime now = DateTime.now();
        int continuousSignCount = getContinuousSignCount(userId, now);

        result.put("total", continuousSignCount);

        // 拿到本周第一天，往后取到今天，就可以判断本周内的签到天数
        // 注意需要考虑跨月问题
        DateTime weekFirstDay = DateUtil.beginOfWeek(now, false);

        if (now.month() != weekFirstDay.month()) {
            // 如果今天所在的这一周的周日跨月了，需要取上个月的签到情况
            BitFieldSubCommands.BitFieldSubCommand[] fieldSubCommands = new BitFieldSubCommands.BitFieldSubCommand[2];
            // 前一个月

            // 本周第一天（周日）到上月尾还有几天
            int day = new Long(DateUtil.betweenDay(weekFirstDay, DateUtil.endOfMonth(weekFirstDay), true)).intValue();
            BitFieldSubCommands.BitFieldGet lastMonthGet =
                    BitFieldSubCommands.BitFieldGet.create(
                            // 周日到前一个月月尾还有几天，就拿几位
                            BitFieldSubCommands.BitFieldType.unsigned(day),
                            // 周日是前一个月的第几天，就 offset 几
                            BitFieldSubCommands.Offset.offset(DateUtil.dayOfMonth(weekFirstDay) - 1)
                    );
            fieldSubCommands[0] = lastMonthGet;

            // 本月
            BitFieldSubCommands.BitFieldGet currMonthGet =
                    BitFieldSubCommands.BitFieldGet.create(
                            // 当前是本周第几天，减去上月的天数，就是本月的位数
                            BitFieldSubCommands.BitFieldType.unsigned(now.dayOfWeek() - day),
                            // 跨月从 0 开是位移
                            BitFieldSubCommands.Offset.offset(0)
                    );
            fieldSubCommands[1] = currMonthGet;

            BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create(fieldSubCommands);
            List<Long> list = redisUtils.bitField(buildSignKey(userId, now), bitFieldSubCommands);
            char[] firstPartSign = toFullBinaryString(list.get(0), day).toCharArray();
            char[] secondPartSign = toFullBinaryString(list.get(1), now.dayOfWeek() - day).toCharArray();

            char[] res = new char[firstPartSign.length + secondPartSign.length];
            ArrayUtil.copy(firstPartSign,0,res,0,firstPartSign.length);
            ArrayUtil.copy(secondPartSign,0,res,firstPartSign.length,secondPartSign.length);

            result.put("latestWeek", new String(res).split(""));
        } else {
            // 否则取本月即可
            BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands
                    .create()
                    // 今天是星期几，取几位。 周日为 1 ，周二则是从周日开始取 3 位
                    .get(BitFieldSubCommands.BitFieldType.unsigned(now.dayOfWeek()))
                    // 从本周的第一天开始
                    .valueAt(DateUtil.beginOfWeek(now, false).dayOfMonth() - 1);
            List<Long> list = redisUtils.bitField(buildSignKey(userId, now), bitFieldSubCommands);
            String fullBinaryString = toFullBinaryString(list.get(0), now.dayOfWeek());
            result.put("latestWeek",  new String(fullBinaryString.toCharArray()).split(""));
        }

        result.put("isSign", checkSign(userId, now));

        return Result.success().put("data", result);
    }

    private String buildSignKey(Long userId, Date date) {
        return String.format("u:sign:%d:%s", userId, DateUtil.format(date, "yyyyMM"));
    }

    /**
     * 将 long 类型数据转成二进制的字符串，不足 long 类型位数时在前面添“0”以凑足位数
     * @param num
     * @return
     */
    private String toFullBinaryString(long num, int size) {
        char[] chs = new char[size];
        for(int i = 0; i < size; i++) {
            chs[size - 1 - i] = (char)(((num >> i) & 1) + '0');
        }
        return new String(chs);
    }


}
