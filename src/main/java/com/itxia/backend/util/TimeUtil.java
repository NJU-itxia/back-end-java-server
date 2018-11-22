package com.itxia.backend.util;

import org.joda.time.DateTime;

import java.sql.Timestamp;

/**
 * @author Yzh
 * 处理一些时间相关的问题的工具类
 * <p>
 * 未测试
 */
public class TimeUtil {

    /**
     * 返回当前的年份
     *
     * @return 年份的数值
     */
    public static int currentYearNum() {
        return new DateTime().year().get();
    }

    /**
     * 返回这一周是这一年的第几周
     *
     * @return 一个数值
     */
    public static int lastWeekNum() {
        return new DateTime().minusWeeks(1).weekOfWeekyear().get();
    }

    /**
     * 返回上一周的开始时间
     *
     * @return 一个Timestamp类的开始时间
     */
    public static Timestamp lastWeekStartTime() {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.dayOfWeek().withMinimumValue().minusWeeks(1).millisOfDay().withMinimumValue();
        return new Timestamp(dateTime.getMillis());
    }

    /**
     * 返回上一周的结束时间
     *
     * @return 一个Timestamp类的结束时间
     */
    public static Timestamp lastWeekEndTime() {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.dayOfWeek().withMaximumValue().minusWeeks(1).millisOfDay().withMaximumValue();
        return new Timestamp(dateTime.getMillis());
    }

    /**
     * 返回某一周的开始时间
     *
     * @param weekNum 周数
     * @return 时间
     */
    public static Timestamp someWeekStartTime(int weekNum) {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.dayOfWeek().withMinimumValue().withWeekOfWeekyear(weekNum).millisOfDay().withMinimumValue();
        return new Timestamp(dateTime.getMillis());
    }

    /**
     * 返回某一周的结束时间
     *
     * @param weekNum 周数
     * @return 时间
     */
    public static Timestamp someWeekEndTime(int weekNum) {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.dayOfWeek().withMaximumValue().withWeekOfWeekyear(weekNum).millisOfDay().withMaximumValue();
        return new Timestamp(dateTime.getMillis());
    }
}
