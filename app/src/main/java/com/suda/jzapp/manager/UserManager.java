package com.suda.jzapp.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.manager.BaseManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.ExceptionInfoUtil;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.TextUtil;

import java.util.List;

/**
 * Created by ghbha on 2016/4/6.
 */
public class UserManager extends BaseManager {
    public UserManager(Context context) {
        super(context);
    }

    public void register(String userName, String password, String email, final Handler handler) {
        final MyAVUser user = new MyAVUser();
        user.setUsername(userName);
        user.setPassword(password);
        user.setEmail(email);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                Message message = new Message();
                if (e == null) {
                    message.what = Constant.MSG_SUCCESS;
                } else {
                    message.what = Constant.MSG_ERROR;
                    message.obj = ExceptionInfoUtil.getError(e.getCode());
                    LogUtils.getAvEx(e, _context);
                }
                handler.sendMessage(message);
            }
        });
    }

    public void login(String userName, final String password, String email, final Handler handler) {
        //邮箱登录
        if (!TextUtils.isEmpty(email)) {
            AVQuery<MyAVUser> query = AVObject.getQuery(MyAVUser.class);
            query.findInBackground(new FindCallback<MyAVUser>() {
                @Override
                public void done(List<MyAVUser> list, AVException e) {
                    final Message message = new Message();
                    if (e == null) {
                        if (list.size() > 0) {
                            MyAVUser myAVUser = list.get(0);
                            myAVUser.logInInBackground(myAVUser.getUsername(), password, new LogInCallback<AVUser>() {
                                @Override
                                public void done(AVUser avUser, AVException e) {
                                    if (e == null) {
                                        message.what = Constant.MSG_SUCCESS;
                                    } else {
                                        message.what = Constant.MSG_ERROR;
                                        message.obj = ExceptionInfoUtil.getError(e.getCode());
                                        LogUtils.getAvEx(e, _context);
                                    }
                                    handler.sendMessage(message);
                                }
                            });
                        } else {
                            message.what = Constant.MSG_ERROR;
                            message.obj = "未注册";
                            handler.sendMessage(message);
                        }
                    } else {
                        message.what = Constant.MSG_ERROR;
                        message.obj = ExceptionInfoUtil.getError(e.getCode());
                        handler.sendMessage(message);
                        LogUtils.getAvEx(e, _context);
                    }
                }
            });
            return;
        }

        //用户名登录
        AVUser.logInInBackground(userName, password, new LogInCallback<MyAVUser>() {
            @Override
            public void done(MyAVUser avUser, AVException e) {
                Message message = new Message();
                if (e == null) {
                    message.what = Constant.MSG_SUCCESS;
                } else {
                    message.what = Constant.MSG_ERROR;
                    message.obj = ExceptionInfoUtil.getError(e.getCode());
                    LogUtils.getAvEx(e, _context);
                }
                handler.sendMessage(message);
            }
        }, MyAVUser.class);
    }
}
