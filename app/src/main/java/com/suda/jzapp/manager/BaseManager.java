package com.suda.jzapp.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.AVException;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.LogUtils;

/**
 * Created by Suda on 2015/11/16.
 */
public abstract class BaseManager {

    public BaseManager(Context context) {
        _context = context;
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

    protected void getAvEx(AVException avEx){
        LogUtils.getAvEx(avEx,_context);
    }

    protected Context _context;
}
