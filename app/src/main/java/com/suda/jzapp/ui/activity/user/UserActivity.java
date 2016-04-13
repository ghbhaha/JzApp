package com.suda.jzapp.ui.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.manager.UserManager;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends BaseActivity {

    @Override
    protected void initWidget() {
        mHeadIcon = (CircleImageView) findViewById(R.id.profile_image);
        mTvUserName = (TextView) findViewById(R.id.userid);
        userManager.getMe(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                User user = (User) msg.obj;
                Glide.with(UserActivity.this).
                        load(user.getHeadImage())
                        .placeholder(R.mipmap.suda).into(mHeadIcon);
                mTvUserName.setText(user.getUserName());
            }
        });
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
    private TextView mTvUserName;

}
