package com.suda.jzapp.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.receiver.AlarmReceiver;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ghbha on 2016/4/15.
 */
public class AlarmUtil {


    public static void createAlarm(Context context) {

        long alarmTime = SPUtils.gets(context, Constant.SP_ALARM_TIME, 0l);

        Calendar calendarOld = Calendar.getInstance();
        calendarOld.setTimeInMillis(alarmTime);

        long curTime = System.currentTimeMillis();

        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        // 过10s 执行这个闹铃
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(curTime);
        //calendar.add(Calendar.SECOND, 10);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, calendarOld.get(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendarOld.get(Calendar.MINUTE));
        if (calendar.getTimeInMillis() < curTime) {
            calendar.add(Calendar.DATE, 1);
        }

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LogUtils.d("ALARM", "下次提醒时间" + format.format(calendar.getTime()));

        // 进行闹铃注册
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(sender);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
    }
}
