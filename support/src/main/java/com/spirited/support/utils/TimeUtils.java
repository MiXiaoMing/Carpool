package com.spirited.support.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

    //时间格式化
    public static String dateFormat(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
                + "（" + TimeUtils.getWeekOfDate(date) + "）";
    }

    //根据日期获取星期几
    public static String getWeekOfDate(Date date) {
        String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static int getMinutes(String data) {
        String hour = data.substring(0, data.indexOf(":"));
        String minutes = data.substring(data.indexOf(":") + 1);
        return Integer.parseInt(hour) * 60 + Integer.parseInt(minutes);
    }

    public static String addTime(String data, int add) {
        int totalMinutes = getMinutes(data) + add;
        int hour = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        if (hour < 10) {
            return "0" + hour + ":" + minutes;
        } else {
            return hour + ":" + minutes;
        }
    }
}
