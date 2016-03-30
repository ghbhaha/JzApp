package com.suda.jzapp.util;

import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by ghbha on 2016/2/15.
 */
public class ThreadPoolUtil {
    public static synchronized ExecutorService getThreadPoolService(){
        if(threadPool == null)
            threadPool = Executors.newFixedThreadPool(MAX_THREAD_NUM);

        return threadPool;
    }

    public static synchronized ScheduledExecutorService getScheduledThreadPoolService(){
        if(scheduledThreadPool == null)
            scheduledThreadPool = Executors.newScheduledThreadPool(MAX_THREAD_NUM);

        return scheduledThreadPool;
    }

    public static boolean isInMainThread(){
        return Looper.myLooper() == Looper.getMainLooper();
    }

    private static volatile ExecutorService threadPool;
    private static volatile ScheduledExecutorService scheduledThreadPool;
    private static final int MAX_THREAD_NUM = Runtime.getRuntime().availableProcessors()*4;
}
