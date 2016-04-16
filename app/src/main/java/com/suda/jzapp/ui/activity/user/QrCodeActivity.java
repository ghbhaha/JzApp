package com.suda.jzapp.ui.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.manager.domain.MsgDo;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.MsgConstant;
import com.suda.jzapp.misc.MyMessageHandler;
import com.suda.jzapp.util.QRCodeUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class QrCodeActivity extends BaseActivity implements MyMessageHandler.MsgCallBack {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userManager = new UserManager(this);
        initWidget();
        myMessageHandler = new MyMessageHandler(this);
        myMessageHandler.setMsgCallBack(this);
        AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class, myMessageHandler);
        userManager.openChatClient(new UserManager.CallBack() {
            @Override
            public void done(Object o) {

            }
        });
    }

    @Override
    public void show(MsgDo msgDo) {
        Glide.with(QrCodeActivity.this).
                load(msgDo.getMsgExtra()).error(R.mipmap.suda)
                .into(otherHead);
        otherName.setText(msgDo.getSendUser());
        YoYo.with(Techniques.FadeOut).playOn(imageQrCode);
        YoYo.with(Techniques.ZoomIn).playOn(linkLayout);
        //imageQrCode.setVisibility(View.GONE);
        linkLayout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initWidget() {
        imageQrCode = (ImageView) findViewById(R.id.qr_code_image);
        linkLayout = findViewById(R.id.link_layout);

        myHead = (CircleImageView) findViewById(R.id.my_head);
        otherHead = (CircleImageView) findViewById(R.id.other_head);
        myName = (TextView) findViewById(R.id.my_name);
        otherName = (TextView) findViewById(R.id.other_name);

        userManager.getMe(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                final User user = (User) msg.obj;
                if (user == null)
                    return;
                userManager.queryUserLinkByUser(user.getUserName(), new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        String code = "";
                        if (msg.obj == null) {
                            code = Constant.QR_MARK + user.getUserName();
                        } else {
                            code = Constant.QR_MARK_HAVE_LINK + user.getUserName();
                        }
                        imageQrCode.setImageBitmap(QRCodeUtil.createQRImage(code, imageQrCode.getLayoutParams().width, imageQrCode.getLayoutParams().width));
                    }
                });
                Glide.with(QrCodeActivity.this).
                        load(user.getHeadImage()).error(R.mipmap.suda)
                        .into(myHead);
                myName.setText(user.getUserName());
            }
        });

    }

    public void acceptLink(View view) {
        List<String> list = new ArrayList<>();
        list.add(myName.getText().toString());
        list.add(otherName.getText().toString());
        userManager.setUserLink(list, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                YoYo.with(Techniques.FadeOut).playOn(linkLayout);
                YoYo.with(Techniques.ZoomIn).playOn(imageQrCode);
                userManager.sendMsg(otherName.getText().toString(), MsgConstant.MSG_TYPE_LINK_ACCEPT, "", "", new Handler());
            }
        });
    }

    public void cancelLink(View view) {
        YoYo.with(Techniques.FadeOut).playOn(linkLayout);
        YoYo.with(Techniques.ZoomIn).playOn(imageQrCode);
        userManager.sendMsg(otherName.getText().toString(), MsgConstant.MSG_TYPE_LINK_CANCEL, "", "", new Handler());
    }

    @Override
    protected void onDestroy() {
        AVIMMessageManager.unregisterMessageHandler(AVIMTextMessage.class, myMessageHandler);
        super.onDestroy();
    }

    private View linkLayout;
    private ImageView imageQrCode;
    private UserManager userManager;
    MyMessageHandler myMessageHandler;

    private CircleImageView myHead, otherHead;
    private TextView myName, otherName;
}
