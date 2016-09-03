package com.suda.jzapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.manager.SyncManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.AlarmUtil;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.NetworkUtil;
import com.suda.jzapp.util.SPUtils;

/**
 * Created by Suda on 2015/10/9.
 */
public class BackupService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if ((boolean) SPUtils.get(this, true, Constant.SP_ALARM_EVERY_DAY, false))
            AlarmUtil.createAlarm(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (canSync()) {
            LogUtils.e(TAG, "START_BACK_UP");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.e(TAG, "START_BACK_UP_1");
                    syncManager.forceBackup();
                    stopSelf();
                }
            }).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private boolean canSync() {
        return MyAVUser.getCurrentUser() != null &&
                ((boolean) SPUtils.get(this, true, Constant.SP_SYNC_ONLY_WIFI, false) ? NetworkUtil.checkWifi(this) : NetworkUtil.checkNetwork(this));
    }

    private SyncManager syncManager = new SyncManager(this);
    private static final String TAG = "SYNC_SERVICE";
}
