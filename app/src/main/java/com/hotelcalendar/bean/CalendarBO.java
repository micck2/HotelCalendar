package com.hotelcalendar.bean;

/**
 * 日期实体类
 *
 * @author lilin on 2018/1/18 8:04
 */

public class CalendarBO {
    public int year;
    public int month;
    public int day;
    /**
     * 是否为当前月份的日期
     */
    public boolean isCurrentMonth;

    public CalendarBO (int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarBO that = (CalendarBO) o;

        if (year != that.year) return false;
        if (month != that.month) return false;
        return day == that.day;
    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        result = 31 * result + day;
        return result;
    }

    /**
     * 判断结束时间是否大于开始时间
     * @param startDate
     * @return
     */
    public boolean compare(CalendarBO startDate) {
        boolean result = false;
        if (year >= startDate.year) {
            if (month >= startDate.month) {
                if (day > startDate.day) {
                    result = true;
                } else {
                    result = false;
                }
            } else {
                result = false;
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return ""+year+month+day;
    }
}
