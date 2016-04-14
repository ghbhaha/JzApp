package com.suda.jzapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.UserLink;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.manager.domain.MsgDo;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.MsgConstant;
import com.suda.jzapp.misc.MyMessageHandler;
import com.suda.jzapp.ui.activity.system.CaptureActivityAnyOrientation;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.SnackBarUtil;

import java.util.ArrayList;
import java.util.List;

public class UserLinkActivity extends BaseActivity implements MyMessageHandler.MsgCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_link);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWidget();
        userManager = new UserManager(this);
        myMessageHandler = new MyMessageHandler(this);
        myMessageHandler.setMsgCallBack(this);
        AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class, myMessageHandler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                LogUtils.d("MainActivity", "Cancelled scan");
            } else {
                LogUtils.d("MainActivity", "Scanned");
                sendLinkRequest(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        linkBt.setBackgroundTintList(getResources().getColorStateList(getMainTheme().getMainColorID()));
    }

    @Override
    protected void onDestroy() {
        userManager.closeIMClient();
        super.onDestroy();
    }

    @Override
    protected void initWidget() {
        linkBt = (FloatingActionButton) findViewById(R.id.fab);
    }

    public void link(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        integrator.setPrompt("请扫描需要关联账户的二维码");
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    public void show(MsgDo msgDo) {
        if (msgDo.getMsgType() == MsgConstant.MSG_TYPE_LINK_ACCEPT) {
            SnackBarUtil.showSnackInfo(linkBt, this, msgDo.getSendUser() + "接受关联");
        }
        if (msgDo.getMsgType() == MsgConstant.MSG_TYPE_LINK_CANCEL) {
            SnackBarUtil.showSnackInfo(linkBt, this, msgDo.getSendUser() + "拒绝关联");
        }
    }

    private void sendLinkRequest(String code) {

        if (TextUtils.isEmpty(code))
            return;

        if (code.contains(Constant.QR_MARK)) {
            final String userName = code.replace(Constant.QR_MARK, "");
            userManager.getMe(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    final User user = (User) msg.obj;
                    if (userName.equals(user.getUserName())) {
                        SnackBarUtil.showSnackInfo(linkBt, UserLinkActivity.this, "自己不可与自己关联哦");
                        // Toast.makeText(UserLinkActivity.this, "自己不可与自己关联哦", Toast.LENGTH_LONG).show();
                        return;
                    }

                    AVQuery<UserLink> query = AVObject.getQuery(UserLink.class);
                    List<String> users = new ArrayList<String>();
                    users.add(userName);
                    users.add(user.getUserName());
                    query.whereContainsAll(UserLink.MEMBER, users);
                    query.findInBackground(new FindCallback<UserLink>() {
                        @Override
                        public void done(List<UserLink> list, AVException e) {
                            if (e == null) {
                                if (list.size() > 0) {
                                    SnackBarUtil.showSnackInfo(linkBt, UserLinkActivity.this, "已经关联");
                                    return;
                                }
                                userManager.sendMsg(userName, MsgConstant.MSG_TYPE_LINK_REQUEST, userName, user.getHeadImage(), new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        SnackBarUtil.showSnackInfo(linkBt, UserLinkActivity.this, "请求成功");
                                        //Toast.makeText(UserLinkActivity.this, "请求成功", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }
                    });


                }
            });
        }
    }


    FloatingActionButton linkBt;
    private UserManager userManager;
    private MyMessageHandler myMessageHandler;
}
