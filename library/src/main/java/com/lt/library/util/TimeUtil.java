package com.lt.library.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @作者: LinTan
 * @日期: 2019/5/1 20:57
 * @版本: 1.0
 * @描述: //TimeUtil
 * 1.0: Initial Commit
 */

public class TimeUtil {
    public static final int HOUR_MODE_12 = 12;
    public static final int HOUR_MODE_24 = 24;
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT_12_HOUR = "hh:mm:ss";//hh是12小时制
    private static final String TIME_FORMAT_24_HOUR = "HH:mm:ss";//HH是24小时制
    private static Calendar calendar = Calendar.getInstance();

    private TimeUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String getDate() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        return simpleDateFormat.format(date);
    }//获取日期

    public static String getDate(int offsetDay) {
        String stringDate = null;
        try {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
            String currentDate = simpleDateFormat.format(date);
            calendar.setTime(simpleDateFormat.parse(currentDate));
            calendar.add(Calendar.DAY_OF_YEAR, offsetDay);
            stringDate = simpleDateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringDate;
    }//获取偏移后的日期。eg: offsetDay为1, 即得到明天的日期, offsetDay为-1, 即得到昨天的日期)

    public static String getTime(@HourMode int hourMode) {
        Date date = new Date();
        String timeFormat = null;
        switch (hourMode) {
            case HOUR_MODE_12:
                timeFormat = TIME_FORMAT_12_HOUR;
                break;
            case HOUR_MODE_24:
                timeFormat = TIME_FORMAT_24_HOUR;
                break;
            default:
                break;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat, Locale.getDefault());
        return simpleDateFormat.format(date);
    }//获取时间

    public static String getDayOfWeek(int offsetDay) {
        int index = getCurrentDayOfWeek() + offsetDay;
        if (index < 0) {
            index = 7 - index * -1 % 7;
        }//规范索引的范围
        String[] strings = {"日", "一", "二", "三", "四", "五", "六"};
        return strings[index];//注意, Java中一周的第一天是周日; eg: [周日, 周一, 周二, 周三, 周四, 周五, 周六]
    }//获取偏移后的星期。eg: offsetDay为1, 即得到明天的星期, offsetDay为-1, 即得到昨天的星期

    public static int getCurrentYear() {
        return calendar.get(Calendar.YEAR);
    }//获取当前的年数

    public static int getCurrentMonth() {
        int indexTemp = calendar.get(Calendar.MONTH);
        return indexTemp + 1;
    }//获取今年的月数

    public static int getCurrentDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }//获取本月的天数

    public static int getCurrentDayOfWeek() {
        int indexTemp = calendar.get(Calendar.DAY_OF_WEEK);//注意, calendar.get返回的是数组中元素的索引值, 而不是元素值
        return indexTemp - 1;
    }//获取本周的天数

    public static int getCurrentHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }//获取今天的小时数

    public static int getCurrentMinute() {
        return calendar.get(Calendar.MINUTE);
    }//获取该小时的分数

    public static int getCurrentSecond() {
        return calendar.get(Calendar.SECOND);
    }//获取该分钟的秒数

    @IntDef({HOUR_MODE_12, HOUR_MODE_24})//限定取值范围
    @Retention(RetentionPolicy.SOURCE)//限制注解仅存在源码中, Class中不会包括
    public @interface HourMode {
    }
}
