package com.suda.jzapp.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.AVException;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.NetworkUtil;
import com.suda.jzapp.util.SPUtils;

/**
 * Created by Suda on 2015/11/16.
 */
public abstract class BaseManager {

    public BaseManager(Context context) {
        _context = context;
    }

    protected void sendEmptyMessage(Handler handler, int what) {
        if (handler != null)
            handler.sendEmptyMessage(what);
    }

    protected void sendMessage(Handler handler, Object value) {
        sendMessage(handler, value, true);
    }

    protected void sendMessage(Handler handler, Object value, boolean isSuccess) {
        Message message = new Message();
        if (isSuccess && value != null) {
            message.what = Constant.MSG_SUCCESS;
            message.obj = value;
        } else {
            message.what = Constant.MSG_ERROR;
        }
        handler.sendMessage(message);
    }

    protected void getAvEx(AVException avEx) {
        LogUtils.getAvEx(avEx, _context);
    }

    protected Context _context;

    protected interface Callback {
        void doSth(boolean isSync, String objId);
    }

    protected boolean canSync() {
        return MyAVUser.getCurrentUser() != null &&
                ((boolean) SPUtils.get(_context, true, Constant.SP_SYNC_ONLY_WIFI, false) ? NetworkUtil.checkNetwork(_context) : NetworkUtil.checkNetwork(_context));
    }

    protected static final int PAGE_SIZE = 1000;
}
