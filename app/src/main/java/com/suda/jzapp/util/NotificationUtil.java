package com.suda.jzapp.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.suda.jzapp.R;
import com.suda.jzapp.ui.activity.MainActivity;

/**
 * Created by ghbha on 2016/4/14.
 */
public class NotificationUtil {
    public static void showRemindNotification(final Context context, String message) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(context)
                //.setLargeIcon(context.getResources().getN)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("记账提醒")
                .setContentText(message)
                .build();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        PendingIntent contextIntent = PendingIntent.getActivity(context, 0, intent, 0);
        notification.contentIntent = contextIntent;
        notificationManager.notify(R.mipmap.ic_launcher, notification);
    }
}
