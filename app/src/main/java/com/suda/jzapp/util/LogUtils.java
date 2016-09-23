package com.suda.jzapp.util;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.suda.jzapp.BuildConfig;

//Logcat统一管理类
public class LogUtils {

    private LogUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (isDebug)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (isDebug)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (isDebug)
            Log.e(TAG, msg);
    }

    public static void v(String msg) {
        if (isDebug)
            Log.v(TAG, msg);
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (isDebug)
            Log.i(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (isDebug)
            Log.d(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (isDebug)
            Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (isDebug)
            Log.v(tag, msg);
    }

    public static void getAvEx(final AVException avException, final Context context) {
        if (avException != null) {
            if (isDebug) {
                Log.e("AVException@@@", "ExceptionCode:" + avException.getCode() + ",ExceptionDetail:" + avException.toString());
                try {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,
                                    "ExceptionCode:" + avException.getCode() + ",ExceptionDetail:" + avException.toString()
                                    , Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {

                }
            }
        }
    }


    public static boolean isDebug = !BuildConfig.RELEASE;
    private static final String TAG = "JZ_APP";

}