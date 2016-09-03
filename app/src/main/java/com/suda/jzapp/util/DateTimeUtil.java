package com.suda.jzapp.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ghbha on 2016/4/10.
 */
public class DateTimeUtil {
    public static boolean isThisYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        calendar.setTime(date);
        return year == calendar.get(Calendar.YEAR);
    }

    public static boolean isToday(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.getTime();
        return calendar.getTime().equals(date);
    }

    public static String fmCQLDate(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.'000Z'");
        return format.format(date);
    }
}
