package com.suda.jzapp.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;

import com.suda.jzapp.R;

/**
 * Created by suda on 2016/8/10.
 */
public class LauncherIconUtil {

    public static void changeLauncherIcon(Activity ctx, int iconType) {
        PackageManager pm = ctx.getPackageManager();

        pm.setComponentEnabledSetting(new ComponentName("com.suda.jzapp",
                        "com.suda.jzapp.MainActivity-icon1"),
                iconType == 1 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        pm.setComponentEnabledSetting(new ComponentName("com.suda.jzapp",
                        "com.suda.jzapp.MainActivity-icon2"),
                iconType == 2 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

    }

    public static int getLauncherIcon(Context context) {
        if ((boolean) SPUtils.get(context, true, context.getResources().getString(R.string.key_icon), true))
            return R.mipmap.icon1;
        else
            return R.mipmap.icon2;

    }

}
