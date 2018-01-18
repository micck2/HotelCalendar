package com.hotelcalendar.util;

import android.util.Log;

import com.hotelcalendar.bean.CalendarBO;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 日期工具类
 *
 * @author lilin on 2018/1/18 8:10
 */

public class CalendarUtils {
    /**
     * 初始化某一天的日期对象
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static CalendarBO initCalendarBO(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(year, month - 1, day);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
        return new CalendarBO(year, month, day);
    }

    /**
     * 获取某月份的天数
     * @param year
     * @param month
     * @return
     */
    public static int initDaysOfSomeMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(year,month - 1,1);
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取某天对应的星期
     * @param year
     * @param month
     * @param day
     * @return
     */
    private static int initWeekDayOnSomeDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取当前月份的日期列表
     * @param year
     * @param month
     * @return
     */
    public static List<CalendarBO> initDaysListOfMonth(int year, int month) {
        List<CalendarBO> calendarList = new ArrayList<>();
        int daysOfMonth = initDaysOfSomeMonth(year, month);
        //找到当前月第一天的星期，计算出前面空缺的上个月的日期个数，填充到当月日期列表中
        int weekDayOfFirstDay = initWeekDayOnSomeDate(year,month,1);
        int preMonthDays = weekDayOfFirstDay - 1;

        for (int i = preMonthDays; i > 0; i--) {
            CalendarBO calendarBO = initCalendarBO(year, month, 1 - i);
            calendarBO.isCurrentMonth = false;
            calendarList.add(calendarBO);
        }

        for (int i = 0; i < daysOfMonth; i++) {
            CalendarBO calendarBO = initCalendarBO(year,month,i + 1);
            calendarBO.isCurrentMonth = true;
            calendarList.add(calendarBO);
        }

        return calendarList;
    }

    /**
     * 格式化标题展示
     * @param year
     * @param month
     * @return
     */
    public static String formatYearAndMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        return year + "年" + month + "月";
    }

    /**
     * 获取系统当前年月日
     *
     * @return
     */
    public static int[] getNowDayFromSystem() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return new int[]{cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DATE)};
    }

    /**
     * 判断是否为系统当天
     * @param calendarBO
     * @return
     */
    public static boolean isToday(CalendarBO calendarBO) {
        int[] nowDay = getNowDayFromSystem();
        return calendarBO.year == nowDay[0] && calendarBO.month == nowDay[1] && calendarBO.day == nowDay[2];
    }
}
