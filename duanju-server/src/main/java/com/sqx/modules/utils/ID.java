package com.sqx.modules.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @version 1.0
 */
public class ID {

    private static Integer	no		= 1;

    private static String	time	= DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT);

    /**
     * yyyyMMddHHmmss
     *
     * @return
     */
    synchronized public static String get() {
        String timestamp = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_FORMAT);
        StringBuilder sb = new StringBuilder(timestamp);

        if (StrUtil.equals(time, timestamp)) {
            no += 1;
        } else {
            time = timestamp;
            no = 1;
        }
        // 202005151739147931 80 1000

        String processId = getProcessID() + "";
        if (processId.length() > 4) {
            processId = processId.substring(0, 4);
        } else {
            processId = StrUtil.padPre(processId, 4, "0");
        }
        sb.append(processId);
        sb.append(StrUtil.padPre(RandomUtil.randomInt(0, 99) + "", 2, "0"));
        sb.append(StrUtil.padPre(no + "", 6, "0"));
        return sb.toString();
    }

    public static int getProcessID() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        return Integer.parseInt(runtimeMXBean.getName().split("@")[0]);
    }

    // public static void main(String[] args) {
    // long time = System.currentTimeMillis();
    // Set<String> set = new HashSet<>();
    // for (int i = 0; i < 5000000; i++) {
    // String s = get();
    // set.add(s);
    // }
    // System.out.println("总数: " + set.size() + "耗时: " + (System.currentTimeMillis()
    // - time) + "ms");
    // }
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        CountDownLatch setCount = new CountDownLatch(10);
        Map<String, Boolean> set = new ConcurrentHashMap<>();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < 10000; j++) {
                    set.put(ID.get(), true);
                    System.out.println("set.size() = " + set.size());
                }
                System.out.println("Thread.currentThread() = " + Thread.currentThread());
                setCount.countDown();
            }).start();
            countDownLatch.countDown();
        }
        setCount.await();
        System.out.println("set.size() = " + set.size());
    }
}

