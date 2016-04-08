package com.suda.jzapp.ui.activity.system;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.activity.MainActivity;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.MD5Util;
import com.suda.jzapp.util.SPUtils;
import com.suda.jzapp.util.TextUtil;
import com.suda.jzapp.view.GestureLockViewGroup;

import de.hdodenhof.circleimageview.CircleImageView;

public class GestureLockActivity extends BaseActivity {

    @Override
    protected void initWidget() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_lock);
        mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.id_gestureLockViewGroup);
        mForget = (TextView) findViewById(R.id.forget_secret);
        isSetting = getIntent().getBooleanExtra(IntentConstant.SETTING_MODE, false);
        mHeadImg = (CircleImageView) findViewById(R.id.profile_image);

        new UserManager(this).getMe(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constant.MSG_SUCCESS) {
                    User user = (User) msg.obj;
                    Glide.with(GestureLockActivity.this).
                            load(user.getHeadImage())
                            .placeholder(R.mipmap.suda).into(mHeadImg);
                }
            }
        });

        mForget.setText(Html.fromHtml("<u>" + "忘记手势密码" + "</u>"));
        mForget.setVisibility(isSetting ? View.INVISIBLE : View.VISIBLE);

        mForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (!isSetting) {
            secret = (String) SPUtils.get(this, Constant.SP_GESTURE, "");
            mGestureLockViewGroup.setAnswer(secret);
        }

        if (TextUtils.isEmpty(secret) && !isSetting) {
            enterMain();
        }

        mTvTip = (TextView) findViewById(R.id.setting_tips);

        mTvTip.setText("请绘制解锁图案");


        mGestureLockViewGroup.setIsSetting(isSetting);
        mGestureLockViewGroup.setUnMatchExceedBoundary(mTryTime);
        mGestureLockViewGroup
                .setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {

                    @Override
                    public void onUnmatchedExceedBoundary(int errorCode) {
                        if (errorCode == GestureLockViewGroup.CODE_IN_VALID) {
                            mTvTip.setText("手势密码需要四位，请重新输入");
                        }
                        if (errorCode == GestureLockViewGroup.CODE_NOT_SAME) {
                            mTvTip.setText("您已输错5次，即将退出账号");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new UserManager(GestureLockActivity.this).logOut();
                                    enterMain();
                                }
                            }, 300);
                        }
                    }

                    @Override
                    public void onGestureEvent(boolean matched) {
                        if (!matched) {
                            mTryTime--;
                            mTvTip.setText("手势不匹配，请重新输入\n还可尝试" + mTryTime + "次");
                        } else
                            enterMain();
                    }

                    @Override
                    public void onBlockSelected(int cId) {
                    }

                    @Override
                    public void showSelect(String value) {
                        if (isSetting) {
                            if (TextUtils.isEmpty(secret)) {
                                secret = value;
                                mTvTip.setText("请再次绘制解锁图案");
                            } else if (!value.equals(secret)) {
                                secret = value;
                                mTvTip.setText("与上次绘制不一致，请重新输入");
                            } else if (value.equals(secret)) {
                                mTvTip.setText("录入成功");
                                SPUtils.put(GestureLockActivity.this, Constant.SP_GESTURE, secret);
                                finish();
                            }
                        }
                    }
                });
    }

    private void enterMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    private boolean isSetting = false;
    private GestureLockViewGroup mGestureLockViewGroup;
    private CircleImageView mHeadImg;
    private TextView mTvTip, mForget;
    private String secret = "";
    private int mTryTime = 5;
}
