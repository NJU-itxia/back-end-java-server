package com.itxia.backend.util;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;

import static org.junit.Assert.*;

/**
 * 检查TimeUtil的功能
 * 暂时想不到什么好的办法，就先打印一下检查是否有误吧
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TimeUtilTest {

    /**
     * 输出当前年份，查看是否有误
     */
    @Test
    public void testCurrentYear() {
        int year = TimeUtil.currentYearNum();
        System.out.println(year);
        assertTrue(year > 0);
    }

    /**
     * 上一周在这一年的周数
     */
    @Test
    public void testLastWeekNum() {
        int week = TimeUtil.lastWeekNum();
        System.out.println(week);
        assertTrue(week >= 0);
    }

    /**
     * 上一周开始时的毫秒数
     */
    @Test
    public void testLastWeekStartTime() {
        Timestamp time = TimeUtil.lastWeekStartTime();
        System.out.println(time);
    }

    /**
     * 上一周结束时的毫秒数
     */
    @Test
    public void testLastWeekEndTime() {
        Timestamp time = TimeUtil.lastWeekEndTime();
        System.out.println(time);
    }

}
