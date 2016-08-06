package com.suda.jzapp.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationQuery;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.Conversation;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.cloud.avos.pojo.user.UserLink;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.dao.local.account.AccountLocalDao;
import com.suda.jzapp.dao.local.conf.ConfigLocalDao;
import com.suda.jzapp.dao.local.record.RecordLocalDAO;
import com.suda.jzapp.dao.local.record.RecordTypeLocalDao;
import com.suda.jzapp.dao.local.user.UserLocalDao;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.ui.activity.system.SettingsActivity;
import com.suda.jzapp.util.ExceptionInfoUtil;
import com.suda.jzapp.util.ImageUtil;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.MsgUtil;
import com.suda.jzapp.util.SPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ghbha on 2016/4/6.
 */
public class UserManager extends BaseManager {
    public UserManager(Context context) {
        super(context);
    }

    /**
     * 注册
     *
     * @param userName
     * @param password
     * @param email
     * @param handler
     */
    public void register(String userName, String password, String email, final Handler handler) {
        final MyAVUser user = new MyAVUser();
        Bitmap bitmap = BitmapFactory.decodeResource(_context.getResources(), R.mipmap.suda);
        AVFile avFile = new AVFile(MyAVUser.HEAD_IMAGE, ImageUtil.Bitmap2Bytes(bitmap));
        user.setHeadImage(avFile);
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
                    getAvEx(e);
                }
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 登陆
     *
     * @param userName
     * @param password
     * @param email
     * @param handler
     */
    public void login(String userName, final String password, String email, final Handler handler) {
        //邮箱登录
        if (!TextUtils.isEmpty(email)) {
            AVQuery<MyAVUser> query = AVObject.getQuery(MyAVUser.class);
            query.whereEqualTo("email", email);
            query.findInBackground(new FindCallback<MyAVUser>() {
                @Override
                public void done(List<MyAVUser> list, AVException e) {
                    final Message message = new Message();
                    if (e == null) {
                        if (list.size() > 0) {
                            final MyAVUser myAVUser = list.get(0);
                            myAVUser.logInInBackground(myAVUser.getUsername(), password, new LogInCallback<AVUser>() {
                                @Override
                                public void done(AVUser avUser, AVException e) {
                                    if (e == null) {
                                        User user = new User();
                                        user.setUserId(myAVUser.getObjectId());
                                        user.setHeadImage(getImgUrl(myAVUser.getHeadImage()));
                                        user.setUserName(myAVUser.getUsername());
                                        user.setUserCode(myAVUser.getUserCode());
                                        userLocalDao.insertUser(user, _context);
                                        message.what = Constant.MSG_SUCCESS;
                                        configLocalDao.initConfig(_context);
                                    } else {
                                        message.what = Constant.MSG_ERROR;
                                        message.obj = ExceptionInfoUtil.getError(e.getCode());
                                        getAvEx(e);
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
                        getAvEx(e);
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
                    User user = new User();
                    user.setUserId(avUser.getObjectId());
                    user.setHeadImage(getImgUrl(avUser.getHeadImage()));
                    user.setUserName(avUser.getUsername());
                    user.setUserCode(avUser.getUserCode());
                    userLocalDao.insertUser(user, _context);
                    message.what = Constant.MSG_SUCCESS;
                } else {
                    message.what = Constant.MSG_ERROR;
                    message.obj = ExceptionInfoUtil.getError(e.getCode());
                    getAvEx(e);
                }
                handler.sendMessage(message);
            }
        }, MyAVUser.class);
    }

    public void updateHeadIcon(final AVFile avFile, final Handler handler) {
        AVQuery<MyAVUser> query = AVObject.getQuery(MyAVUser.class);
        query.whereEqualTo("objectId", MyAVUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<MyAVUser>() {
            @Override
            public void done(List<MyAVUser> list, AVException e) {
                getAvEx(e);
                if (e == null) {
                    MyAVUser avUser = list.get(0);
                    avUser.setHeadImage(avFile);
                    avUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            getAvEx(e);
                            if (e == null) {
                                userLocalDao.clear(_context);
                                userHashMap.clear();
                                sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                            } else {
                                sendEmptyMessage(handler, Constant.MSG_ERROR);
                            }
                        }
                    });
                }
            }
        });
    }

    public void getMe(final Handler handler) {
        queryUser(getCurUserName(), handler);
    }

    private String getImgUrl(AVFile avFile) {
        if (avFile != null)
            return avFile.getUrl();
        else
            return "";
    }

    public String getCurUserName() {
        if (MyAVUser.getCurrentUser() != null)
            return MyAVUser.getCurrentUser().getUsername();
        else
            return null;
    }

    /**
     * 用户登出
     */
    public void logOut() {
        logOut(true);
    }

    /**
     * 用户登出
     */
    public void logOut(boolean clearAvUser) {
        if (clearAvUser)
            MyAVUser.getCurrentUser().logOut();
        recordLocalDAO.clearAllRecord(_context);
        accountLocalDao.clearAllAccount(_context);
        recordTypeLocalDao.clearAllRecordType(_context);
        configLocalDao.initConfig(_context);
        userLocalDao.clear(_context);
        SPUtils.put(_context, Constant.SP_GESTURE, "");
        SPUtils.put(_context, Constant.SP_FIRST_ADD, true);
        SPUtils.put(_context, Constant.SP_TIP_ROUND_PIE, true);
        SPUtils.put(_context, Constant.SP_TIP_ROUND_LINE, true);
        SPUtils.put(_context, true, SettingsActivity.GESTURE_LOCK, false);
        userHashMap.clear();
        configLocalDao.initRecordType(_context);
        configLocalDao.createDefaultAccount(_context);
    }

    public void setUserLink(List<String> list, final Handler handler) {
        UserLink userLink = new UserLink();
        userLink.setMember(list);
        userLink.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                getAvEx(e);
                if (e == null) {
                    sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                } else {
                    sendEmptyMessage(handler, Constant.MSG_ERROR);
                }
            }
        });
    }

    public void queryUserLinkByUser(String userName, final Handler handler) {

        if (userLink != null) {
            sendMessage(handler, userLink, true);
            return;
        }

        AVQuery<UserLink> query = AVObject.getQuery(UserLink.class);
        query.whereContains(UserLink.MEMBER, userName);
        query.findInBackground(new FindCallback<UserLink>() {
            @Override
            public void done(List<UserLink> list, AVException e) {
                getAvEx(e);
                if (e == null) {
                    if (list != null && list.size() > 0) {
                        userLink = list.get(0);
                        sendMessage(handler, userLink, true);
                    } else {
                        sendMessage(handler, null, true);
                    }
                } else {
                    sendMessage(handler, null, true);
                }
            }
        });

    }

    public void sendMsg(final String toUser, final int msgType, final String msgContent, final String extra, final Handler handler) {
        if (MyAVUser.getCurrentUser() == null) {
            handler.sendEmptyMessage(Constant.MSG_ERROR);
            return;
        }
        final String conName = MyAVUser.getCurrentUser().getUsername() + "&" + toUser;
        getConversation(MyAVUser.getCurrentUser().getUsername(), toUser, new CallBack() {
            @Override
            public void done(Object o) {
                if (o == null)
                    handler.sendEmptyMessage(Constant.MSG_ERROR);
                else {
                    if (o instanceof AVIMConversation) {
                        LogUtils.d("sendMsg1+++++++++++++++");
                        AVIMTextMessage msg = new AVIMTextMessage();
                        msg.setText(MsgUtil.getFormatMsg(msgType, msgContent, extra));
                        ((AVIMConversation) o).sendMessage(msg, new AVIMConversationCallback() {
                            @Override
                            public void done(AVIMException e) {
                                if (e == null)
                                    handler.sendEmptyMessage(Constant.MSG_SUCCESS);
                                else
                                    handler.sendEmptyMessage(Constant.MSG_ERROR);
                            }
                        });
                    } else {
                        LogUtils.d("sendMsg2+++++++++++++++");
                        ((AVIMClient) o).createConversation(Arrays.asList(toUser), conName, null, new AVIMConversationCreatedCallback() {
                            @Override
                            public void done(AVIMConversation avimConversation, AVIMException e) {
                                if (e == null) {
                                    conversationMap.put(conName, avimConversation);
                                    AVIMTextMessage msg = new AVIMTextMessage();
                                    msg.setText(MsgUtil.getFormatMsg(msgType, msgContent, extra));
                                    avimConversation.sendMessage(msg, new AVIMConversationCallback() {
                                        @Override
                                        public void done(AVIMException e) {
                                            if (e == null)
                                                handler.sendEmptyMessage(Constant.MSG_SUCCESS);
                                            else
                                                handler.sendEmptyMessage(Constant.MSG_ERROR);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void getConversation(final String fromUser, final String toUser, final CallBack callBack) {
        openChatClient(new CallBack() {
            @Override
            public void done(final Object o) {
                LogUtils.d("getConversation1+++++++++++++++");
                final String conName = fromUser + "&" + toUser;
                if (conversationMap.get(conName) != null) {
                    callBack.done(conversationMap.get(conName));
                    return;
                }
                LogUtils.d("getConversation2+++++++++++++++");
                AVIMConversationQuery query = ((AVIMClient) o).getQuery();
                List<String> users = new ArrayList<String>();
                users.add(fromUser);
                users.add(toUser);
                query.whereContainsAll(Conversation.COLUMN_MEMBERS, users);
                query.findInBackground(new AVIMConversationQueryCallback() {
                    @Override
                    public void done(List<AVIMConversation> list, AVIMException e) {
                        LogUtils.d("getConversation3+++++++++++++++");
                        if (e == null) {
                            if (list.size() > 0) {
                                callBack.done(list.get(0));
                                conversationMap.put(conName, list.get(0));
                            } else
                                callBack.done(o);
                        } else
                            callBack.done(null);
                    }
                });
            }
        });
    }

    public void openChatClient(final CallBack callBack) {
        LogUtils.d("openChatClient1+++++++++++++++");
        if (MyAVUser.getCurrentUser() == null) {
            callBack.done(null);
            return;
        }

        if (mClient != null) {
            callBack.done(mClient);
            return;
        }

        LogUtils.d("openChatClient2+++++++++++++++");
        AVIMClient user = AVIMClient.getInstance(MyAVUser.getCurrentUser().getUsername());
        user.open(new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                LogUtils.d("openChatClient3+++++++++++++++");
                getAvEx(e);
                if (e == null) {
                    mClient = avimClient;
                    callBack.done(mClient);
                } else
                    callBack.done(null);
            }
        });
    }

    public void closeIMClient() {
        if (mClient != null) {
            conversationMap.clear();
            mClient.close(null);
            mClient = null;
        }
    }


    public void queryUser(final String userName, final Handler handler) {
        final Message message = new Message();
        if (userHashMap.get(userName) != null) {
            message.what = Constant.MSG_SUCCESS;
            message.obj = userHashMap.get(userName);
            handler.sendMessage(message);
        } else {
            if (MyAVUser.getCurrentUser() == null) {
                handler.sendEmptyMessage(Constant.MSG_ERROR);
                return;
            }
            User user = userLocalDao.getUserByUserName(userName, _context);
            if (user != null) {
                userHashMap.put(userName, user);
                message.what = Constant.MSG_SUCCESS;
                message.obj = user;
                handler.sendMessage(message);
                return;
            }

            AVQuery<MyAVUser> query = AVObject.getQuery(MyAVUser.class);
            query.whereEqualTo("username", userName);
            query.findInBackground(new FindCallback<MyAVUser>() {
                @Override
                public void done(List<MyAVUser> list, AVException e) {
                    if (e == null) {
                        MyAVUser avUser = list.get(0);
                        User user = new User();
                        user.setUserId(avUser.getObjectId());
                        user.setHeadImage(getImgUrl(avUser.getHeadImage()));
                        user.setUserCode(avUser.getUserCode());
                        user.setUserName(avUser.getUsername());
                        userLocalDao.insertUser(user, _context);
                        userHashMap.put(userName, user);
                        message.what = Constant.MSG_SUCCESS;
                        message.obj = user;
                    } else {
                        message.what = Constant.MSG_ERROR;
                    }
                    handler.sendMessage(message);
                }
            });
        }

    }

    public void setUserLink(UserLink userLink) {
        UserManager.userLink = userLink;
    }

    private static AVIMClient mClient;
    private static Map<String, AVIMConversation> conversationMap = new HashMap<>();
    private static Map<String, User> userHashMap = new HashMap<>();

    public static UserLink userLink;

    private UserLocalDao userLocalDao = new UserLocalDao();
    private RecordLocalDAO recordLocalDAO = new RecordLocalDAO();
    private RecordTypeLocalDao recordTypeLocalDao = new RecordTypeLocalDao();
    private AccountLocalDao accountLocalDao = new AccountLocalDao();
    private ConfigLocalDao configLocalDao = new ConfigLocalDao();

    public interface CallBack {
        void done(Object o);
    }

}
