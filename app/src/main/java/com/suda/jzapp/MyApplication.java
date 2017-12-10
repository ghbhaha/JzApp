package com.suda.jzapp;

import android.support.multidex.MultiDexApplication;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.suda.jzapp.dao.local.account.AccountLocalDao;
import com.suda.jzapp.dao.local.conf.ConfigLocalDao;
import com.suda.jzapp.util.LoadAVOSLib;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.SPUtils;

/**
 * Created by Suda on 2015/9/16.
 */
public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        //接入讯飞
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "="+BuildConfig.XUNFEI_APPID);
        //注册AvOs模块
        AVOSCloud.initialize(this, BuildConfig.LEANCOULD_APPID, BuildConfig.LEANCOULD_APPKEY);
        LoadAVOSLib.LoadLib();
        AVAnalytics.enableCrashReport(this, true);
        //AVIMClient.setOfflineMessagePush(true);
        initData();
        LogUtils.isDebug = BuildConfig.DEBUG;
    }


    private void initData() {
        if ((boolean) SPUtils.get(this, "firstIn", true)) {
            SPUtils.put(this, "firstIn", false);
            ConfigLocalDao dao = new ConfigLocalDao();
            AccountLocalDao accountLocalDao = new AccountLocalDao();
            accountLocalDao.initBudget(this);
            dao.initAccountTypeDb(this);
            dao.initRecordType(this);
            dao.createDefaultAccount(this);
        }
    }
}
