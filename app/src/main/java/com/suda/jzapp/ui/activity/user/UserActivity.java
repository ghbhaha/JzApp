package com.suda.jzapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.util.QRCodeUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends BaseActivity {

    @Override
    protected void initWidget() {
        mHeadIcon = (CircleImageView) findViewById(R.id.profile_image);
        mTvUserName = (TextView) findViewById(R.id.userid);
        mTvUserCode = (TextView) findViewById(R.id.userCode);
        mTvEmail = (TextView) findViewById(R.id.email);
        imageViewQrCode = (ImageView) findViewById(R.id.qr_code_image);

        userManager.getMe(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                User user = (User) msg.obj;
                if (user == null)
                    return;
                Glide.with(UserActivity.this).
                        load(user.getHeadImage())
                        .placeholder(R.mipmap.suda).into(mHeadIcon);
                mTvUserName.setText(user.getUserName());
                mTvUserCode.setText("您是第" + user.getUserCode() + "位用户");
                mTvEmail.setText(MyAVUser.getCurrentUser().getEmail());
                imageViewQrCode.setImageBitmap(QRCodeUtil.createQRImage(user.getUserId(), imageViewQrCode.getLayoutParams().width, imageViewQrCode.getLayoutParams().width));
            }
        });
    }

    public void showQcCode(View view) {
        Intent intent = new Intent(this, QrCodeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        userManager = new UserManager(this);
        initWidget();
    }

    public void logOut(View view) {
        userManager.logOut();
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.background).setBackgroundColor(mainColor);
    }

    private UserManager userManager;
    private CircleImageView mHeadIcon;
    private TextView mTvUserName, mTvUserCode, mTvEmail;
    private ImageView imageViewQrCode;

}
