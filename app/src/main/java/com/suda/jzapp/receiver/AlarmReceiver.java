package com.suda.jzapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.suda.jzapp.util.AlarmUtil;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.NotificationUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ghbha on 2016/4/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LogUtils.e("ALARM", format.format(Calendar.getInstance().getTime()));
        NotificationUtil.showRemindNotification(context, "小主，记得记账哦");
        AlarmUtil.createAlarm(context);
    }
}
