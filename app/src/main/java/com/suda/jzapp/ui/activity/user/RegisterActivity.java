package com.suda.jzapp.ui.activity.user;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.NetworkUtil;
import com.suda.jzapp.util.SnackBarUtil;
import com.suda.jzapp.util.ThemeUtil;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getAppStyleId(this));
        setMyContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userManager = new UserManager(this);
        initWidget();
    }


    @Override
    protected void initWidget() {
        mTieUserID = (TextInputEditText) findViewById(R.id.userid);
        mTieEmail = (TextInputEditText) findViewById(R.id.email);
        mTiePass = (TextInputEditText) findViewById(R.id.pass);
        mTiePass2 = (TextInputEditText) findViewById(R.id.pass2);
        mTilUserID = (TextInputLayout) findViewById(R.id.til_userID);
        mTilEmail = (TextInputLayout) findViewById(R.id.til_email);
        mTilPass = (TextInputLayout) findViewById(R.id.til_pass);
        mTilPass2 = (TextInputLayout) findViewById(R.id.til_pass2);
        mRegisterBt = (Button) findViewById(R.id.register_bt);
        mRegisterBt.setBackgroundColor(getColor(this, getMainTheme().getMainColorID()));
        mRegisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        mTieUserID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTilUserID.setErrorEnabled(false);
            }
        });

        mTieEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTilEmail.setErrorEnabled(false);
            }
        });

        mTiePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTilPass.setErrorEnabled(false);
            }
        });

        mTiePass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTilPass2.setErrorEnabled(false);
            }
        });
    }

    private void register() {
        if (!NetworkUtil.checkNetwork(this)) {
            SnackBarUtil.showSnackInfo(mTilUserID, RegisterActivity.this, "请连接网络");
            return;
        }
        String user = mTieUserID.getText().toString();
        String password = mTiePass.getText().toString();
        String password2 = mTiePass2.getText().toString();
        String email = mTieEmail.getText().toString();
        if (TextUtils.isEmpty(user)) {
            mTilUserID.setError("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            mTilEmail.setError("请输入邮箱");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mTilPass.setError("请输入密码");
            return;
        }
        if (TextUtils.isEmpty(password2)) {
            mTilPass2.setError("请输入密码");
            return;
        }

        if (!password.equals(password2)) {
            mTilPass.setError("两次密码不一致");
            mTilPass2.setError("两次密码不一致");
            return;
        }

        userManager.register(user, password, email, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constant.MSG_ERROR) {
                    SnackBarUtil.showSnackInfo(mTilUserID, RegisterActivity.this, msg.obj.toString());
                } else {
                    SnackBarUtil.showSnackInfo(mTilUserID, RegisterActivity.this, "注册成功");
                    MyAVUser.getCurrentUser().logOut();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                }
            }
        });
    }


    private TextInputEditText mTieUserID, mTieEmail, mTiePass, mTiePass2;
    private TextInputLayout mTilUserID, mTilEmail, mTilPass, mTilPass2;
    private Button mRegisterBt;
    private UserManager userManager;
}
