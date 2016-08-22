package com.suda.jzapp.service;

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
import com.suda.jzapp.util.MoneyUtil;

/**
 * Created by suda on 2016/8/16.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    //定义我们要发送的事件
    public static final String WIDGET_BROADCAST = "WIDGET_BROADCAST";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // TODO Auto-generated method stub
        super.onDeleted(context, appWidgetIds);
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
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

                    //将该界面显示到插件中
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    ComponentName componentName = new ComponentName(context, MyWidgetProvider.class);
                    appWidgetManager.updateAppWidget(componentName, rv);
                }
            });
        }
        super.onReceive(context, intent);
    }
}
