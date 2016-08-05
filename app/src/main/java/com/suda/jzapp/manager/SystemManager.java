package com.suda.jzapp.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.suda.jzapp.dao.cloud.avos.pojo.system.AVUpdateCheck;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.AppUtil;
import com.suda.jzapp.util.ThreadPoolUtil;

import java.util.List;

/**
 * Created by suda on 2016/8/5.
 */
public class SystemManager extends BaseManager {
    public SystemManager(Context context) {
        super(context);
    }

    public void getUpdateInfo(final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                PackageInfo packInfo = AppUtil.getAppInfo(_context);
                if (packInfo == null) {
                    sendEmptyMessage(handler, Constant.MSG_ERROR);
                    return;
                }
                AVQuery<AVUpdateCheck> avUpdateCheckAVQuery = AVQuery.getQuery(AVUpdateCheck.class);
                avUpdateCheckAVQuery.whereGreaterThan(AVUpdateCheck.VERSION_CODE, packInfo.versionCode);
                try {
                    List<AVUpdateCheck> list = avUpdateCheckAVQuery.find();
                    if (list != null && list.size() > 0) {
                        sendMessage(handler, list.get(0));
                    } else {
                        sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                    }
                } catch (AVException e) {
                    e.printStackTrace();
                    sendEmptyMessage(handler, Constant.MSG_ERROR);
                }
            }
        });
    }

}
