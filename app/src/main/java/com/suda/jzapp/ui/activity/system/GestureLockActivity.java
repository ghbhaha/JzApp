package com.suda.jzapp.ui.activity.system;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.activity.MainActivity;
import com.suda.jzapp.ui.activity.record.CreateOrEditRecordActivity;
import com.suda.jzapp.ui.activity.record.RecordPieAnalysisActivity;
import com.suda.jzapp.ui.activity.user.LoginActivity;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.SPUtils;
import com.suda.jzapp.util.StatusBarCompat;
import com.suda.jzapp.view.gesturelockview.GestureLockViewGroup;

import de.hdodenhof.circleimageview.CircleImageView;

public class GestureLockActivity extends BaseActivity {

    @Override
    protected void initWidget() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyContentView(R.layout.activity_gesture_lock);
        StatusBarCompat.setStatusBarDarkMode(true, this);

        mGestureLockViewGroup = (GestureLockViewGroup) findViewById(R.id.id_gestureLockViewGroup);
        mForget = (TextView) findViewById(R.id.forget_secret);
        isSetting = getIntent().getBooleanExtra(IntentConstant.SETTING_MODE, false);
        mHeadImg = (CircleImageView) findViewById(R.id.profile_image);


        mTvTip = (TextView) findViewById(R.id.setting_tips);
        mTvTip.setText("请绘制解锁图案");

        new UserManager(this).getMe(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constant.MSG_SUCCESS) {
                    User user = (User) msg.obj;
                    Glide.with(GestureLockActivity.this.getApplicationContext()).
                            load(user.getHeadImage())
                            .error(R.mipmap.suda).into(mHeadImg);
                }
            }
        });

        mForget.setText(Html.fromHtml("<u>" + "忘记手势密码" + "</u>"));
        mForget.setVisibility(isSetting ? View.INVISIBLE : View.VISIBLE);

        mForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestureLockActivity.this, LoginActivity.class);
                intent.putExtra(IntentConstant.FORGET_GESTURE, true);
                startActivityForResult(intent, REQUEST_CODE_FORGET_GESTURE);
            }
        });

        if (!isSetting) {
            secret = (String) SPUtils.get(this, Constant.SP_GESTURE, "");
            mGestureLockViewGroup.setAnswer(secret);
            if (!TextUtils.isEmpty(secret)) {
                manager = FingerprintManagerCompat.from(this);
                if (manager.isHardwareDetected()) {
                    manager.authenticate(null, 0, mCancellationSignal, new MyCallBack(), null);
                    mTvTip.setText("使用指纹或者图案解锁");
                }
            }
        }

        if (TextUtils.isEmpty(secret) && !isSetting) {
            enterMain();
        }
        mGestureLockViewGroup.setIsSetting(isSetting);
        mGestureLockViewGroup.setUnMatchExceedBoundary(mTryTime);
        mGestureLockViewGroup
                .setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {

                    @Override
                    public void onUnmatchedExceedBoundary(int errorCode) {
                        if (errorCode == GestureLockViewGroup.CODE_IN_VALID) {
                            YoYo.with(Techniques.Shake).duration(500).playOn(mTvTip);
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
                            YoYo.with(Techniques.Shake).duration(500).playOn(mTvTip);
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
                            } else {
                                if (!value.equals(secret)) {
                                    YoYo.with(Techniques.Shake).duration(500).playOn(mTvTip);
                                    mTvTip.setText("与上次绘制不一致，请重新输入");
                                } else if (value.equals(secret)) {
                                    mTvTip.setText("录入成功");
                                    SPUtils.put(GestureLockActivity.this, Constant.SP_GESTURE, secret);
                                    finish();
                                }
                            }
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCancellationSignal.cancel();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (!isSetting)
            finish();
    }

    private void enterMain() {
        int goId = getIntent().getIntExtra(IntentConstant.WIDGET_GO_ID, GO_DEFAULT);
        Class goClass = null;
        switch (goId) {
            case GO_MAIN:
                goClass = MainActivity.class;
                break;
            case GO_ANALYSIS:
                goClass = RecordPieAnalysisActivity.class;
                break;
            case GO_ADD_RECORD:
                goClass = CreateOrEditRecordActivity.class;
                break;
            default:
                goClass = MainActivity.class;
        }
        Intent intent = new Intent(this, goClass);
        if (goId == GO_ANALYSIS) {

            Log.e("xxxxxx", getIntent().getBooleanExtra(IntentConstant.RECORD_OUT_IN, false) + "");

            intent.putExtra(IntentConstant.RECORD_OUT_IN,
                    getIntent().getBooleanExtra(IntentConstant.RECORD_OUT_IN, false));
        }
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_FORGET_GESTURE) {
                enterMain();
                SPUtils.put(this, Constant.SP_GESTURE, "");
                SPUtils.put(this, true, SettingsActivity.GESTURE_LOCK, false);
            }
        }
    }

    public class MyCallBack extends FingerprintManagerCompat.AuthenticationCallback {
        private static final String TAG = "MyCallBack";

        // 当出现错误的时候回调此函数，比如多次尝试都失败了的时候，errString是错误信息
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            YoYo.with(Techniques.Shake).duration(500).playOn(mTvTip);
            mTvTip.setText("指纹验证失败，请使用图案解锁");
            LogUtils.d(TAG, "onAuthenticationError: " + errString);
        }

        // 当指纹验证失败的时候会回调此函数，失败之后允许多次尝试，失败次数过多会停止响应一段时间然后再停止sensor的工作
        @Override
        public void onAuthenticationFailed() {
            mTvTip.setText("指纹验证失败，请重试");
            YoYo.with(Techniques.Shake).duration(500).playOn(mTvTip);
            LogUtils.d(TAG, "onAuthenticationFailed: " + "验证失败");
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            LogUtils.d(TAG, "onAuthenticationHelp: " + helpString);
        }

        // 当验证的指纹成功时会回调此函数，然后不再监听指纹sensor
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            LogUtils.d(TAG, "onAuthenticationSucceeded: " + "验证成功");
            mTvTip.setText("指纹验证成功");
            enterMain();
        }
    }

    private boolean isSetting = false;
    private GestureLockViewGroup mGestureLockViewGroup;
    private CircleImageView mHeadImg;
    private TextView mTvTip, mForget;
    private String secret = "";
    private int mTryTime = 5;
    private CancellationSignal mCancellationSignal = new CancellationSignal();

    private static final int REQUEST_CODE_FORGET_GESTURE = 1;

    private FingerprintManagerCompat manager;
    public static final int GO_MAIN = 1;
    public static final int GO_ANALYSIS = 2;
    public static final int GO_ADD_RECORD = 3;
    public static final int GO_DEFAULT = GO_MAIN;
}
