package com.suda.jzapp.service;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;

import com.suda.jzapp.R;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.MonthReport;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.activity.system.GestureLockActivity;
import com.suda.jzapp.util.MoneyUtil;

/**
 * Created by suda on 2016/8/16.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    //定义我们要发送的事件
    public static final String WIDGET_BROADCAST = "WIDGET_BROADCAST";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Intent intent = new Intent();
        intent.setAction(MyWidgetProvider.WIDGET_BROADCAST);
        context.sendBroadcast(intent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Intent intent = new Intent();
        intent.setAction(MyWidgetProvider.WIDGET_BROADCAST);
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(WIDGET_BROADCAST)) {
            final RecordManager recordManager = new RecordManager(context);
            recordManager.getThisMonthReport(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    MonthReport monthReport = (MonthReport) msg.obj;
                    RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                    rv.setTextViewText(R.id.record_date_count, recordManager.getWidgetRecordDayCount());
                    rv.setTextViewText(R.id.money_out, "月支出:" + MoneyUtil.getFormatMoneyStr(context, Math.abs(monthReport.getOutMoney())));
                    rv.setTextViewText(R.id.money_in, "月收入:" + MoneyUtil.getFormatMoneyStr(context, monthReport.getInMoney()));

                    Intent intent = new Intent(context, GestureLockActivity.class);
                    intent.putExtra(IntentConstant.WIDGET_GO_ID, GestureLockActivity.GO_ADD_RECORD);
                    intent.setAction(String.valueOf(System.currentTimeMillis()));
                    PendingIntent pending = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.add_new_record, pending);

                    Intent intent2 = new Intent(context, GestureLockActivity.class);
                    intent2.putExtra(IntentConstant.RECORD_OUT_IN, true);
                    intent2.putExtra(IntentConstant.WIDGET_GO_ID, GestureLockActivity.GO_ANALYSIS);
                    intent2.setAction(String.valueOf(System.currentTimeMillis()));
                    PendingIntent pending2 = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.money_out, pending2);

                    Intent intent3 = new Intent(context, GestureLockActivity.class);
//                    intent3.putExtra(IntentConstant.RECORD_OUT_IN, true);
                    intent3.putExtra(IntentConstant.WIDGET_GO_ID, GestureLockActivity.GO_ANALYSIS);
                    intent3.setAction(String.valueOf(System.currentTimeMillis()));
                    PendingIntent pending3 = PendingIntent.getActivity(context, 0, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.money_in, pending3);

                    Intent intent4 = new Intent(context, GestureLockActivity.class);
                    intent4.putExtra(IntentConstant.WIDGET_GO_ID, GestureLockActivity.GO_MAIN);
                    intent4.setAction(String.valueOf(System.currentTimeMillis()));
                    PendingIntent pending4 = PendingIntent.getActivity(context, 0, intent4, PendingIntent.FLAG_UPDATE_CURRENT);
                    rv.setOnClickPendingIntent(R.id.record_date_count, pending4);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    ComponentName componentName = new ComponentName(context, MyWidgetProvider.class);
                    appWidgetManager.updateAppWidget(componentName, rv);
                }
            });
        }
        super.onReceive(context, intent);
    }
}
