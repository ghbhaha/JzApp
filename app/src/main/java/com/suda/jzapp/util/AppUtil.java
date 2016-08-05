package com.suda.jzapp.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by suda on 2016/8/5.
 */
public class AppUtil {

    public static PackageInfo getAppInfo(Context context) {
        // 获取packagemanager的实例
        try {
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo;
        } catch (Exception e) {

        }
        return null;
    }
}
