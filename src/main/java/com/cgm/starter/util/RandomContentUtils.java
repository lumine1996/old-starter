package com.cgm.starter.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 * @author angel
 */
public class RandomContentUtils {
    private static final String NUMBER_CHAR = "0123456789";
    private static final String LOWER_CHAR = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CHAR = "ABCDEFGHIJKLMNOPQRSTUXWXYZ";
    private static final Random RAND = new Random();

    private RandomContentUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 生成一个日期
     *
     * @return 日期
     */
    public static String fakeDate() {
        return fakeDate(0, Calendar.DATE);
    }

    /**
     * 生成一个日期
     *
     * @param offset 偏移量
     * @param field  Calender字段，日/月/年等
     * @return 日期
     */
    public static String fakeDate(Integer offset, int field) {
        long timeStampMark = 1584602423156L;
        long now = System.currentTimeMillis();
        int intervalSeconds = (int) ((now - timeStampMark) / 1000);
        Calendar calendar = Calendar.getInstance();
        // 3秒加一单位时间
        calendar.add(field, intervalSeconds / 3 + offset);

        SimpleDateFormat df;
        if (field == Calendar.MONTH) {
            df = new SimpleDateFormat("M月");
        } else {
            df = new SimpleDateFormat("M/d");
        }
        return df.format(calendar.getTime());
    }

    /**
     * 生成随机字符串
     *
     * @param length   长度
     * @param allowAll 是否允许所有字符，false时允许大小写字母和数字
     * @return 随机字符串
     */
    public static String randomString(Integer length, boolean allowAll) {
        StringBuilder sb = new StringBuilder();
        if (allowAll) {
            for (int i = 0; i < length; i++) {
                char c;
                // 当允许所有字符时，从ASCII表第一个有效字符'!'(33)到最后一个'~'(126)
                c = (char) ('!' + RAND.nextInt(94));
                sb.append(c);
            }
            return String.valueOf(sb);
        }
        return randomString(length, true, true, true);
    }

    /**
     * 生成随机字符串
     *
     * @param length      长度
     * @param allowNumber 有无数字
     * @param allowUpper  有无大写字母
     * @param allowLower  有无小写字母
     * @return 随机字符串
     */
    public static String randomString(Integer length, boolean allowNumber, boolean allowUpper, boolean allowLower) {
        // 确定字符池大小
        int poolSize = 0;
        poolSize += allowNumber ? 10 : 0;
        poolSize += allowUpper ? 26 : 0;
        poolSize += allowLower ? 26 : 0;
        if (poolSize == 0) {
            return "PoolSize0";
        }

        // 确定字符池
        StringBuilder stringBuilder = new StringBuilder();
        String pool = (allowNumber ? NUMBER_CHAR : null) +
                (allowUpper ? LOWER_CHAR : null) +
                (allowLower ? UPPER_CHAR : null);

        for (int i = 0; i < length; i++) {
            int index = RAND.nextInt(poolSize);
            stringBuilder.append(pool.charAt(index));
        }
        return String.valueOf(stringBuilder);
    }

    /**
     * 生成随机ip地址
     *
     * @return ip地址
     */
    public static String randomIp() {
        StringBuilder sb = new StringBuilder();
        int first = 10 + RAND.nextInt(183);
        int second = RAND.nextInt(256);
        int third = RAND.nextInt(256);
        int fourth = RAND.nextInt(256);
        sb.append(first).append(".").append(second).append(".").append(third).append(".").append(fourth);
        return String.valueOf(sb);
    }

}
