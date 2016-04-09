package com.suda.jzapp.ui.activity.user;

import android.content.Intent;
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
import com.suda.jzapp.manager.AccountManager;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.util.SnackBarUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getAppStyleId(this));
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userManager = new UserManager(this);
        accountManager = new AccountManager(this);
        recordManager = new RecordManager(this);

        forgetGesture = getIntent().getBooleanExtra(IntentConstant.FORGET_GESTURE, false);
        orgUser = userManager.getCurUserName();

        initWidget();
    }

    @Override
    protected void initWidget() {
        mTitUserId = (TextInputEditText) findViewById(R.id.userid);
        mTitPassWord = (TextInputEditText) findViewById(R.id.pass);
        mTilUserId = (TextInputLayout) findViewById(R.id.til_userID);
        mTilPassWord = (TextInputLayout) findViewById(R.id.til_pass);

        loginBt = (Button) findViewById(R.id.login_bt);

        loginBt.setBackgroundColor(getColor(this, getMainTheme().getMainColorID()));

        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

        mTitUserId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTilUserId.setErrorEnabled(false);
            }
        });

        mTitPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mTilPassWord.setErrorEnabled(false);
            }
        });
    }

    private void doLogin() {
        boolean isEmail = false;
        final String user = mTitUserId.getText().toString();
        String password = mTitPassWord.getText().toString();
        if (TextUtils.isEmpty(user)) {
            mTilUserId.setError("请输入用户名或邮箱");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mTilPassWord.setError("请输入密码");
            return;
        }

        hideKeyboard();

        isEmail = isNameAddressFormat(user);
        userManager.login(isEmail ? null : user, password, isEmail ? user : null, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constant.MSG_ERROR) {
                    SnackBarUtil.showSnackInfo(mTilUserId, LoginActivity.this, msg.obj.toString());
                } else {
                    if (forgetGesture && user.equals(orgUser)) {
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        if (forgetGesture) {
                            userManager.logOut(false);
                        }
                        SnackBarUtil.showSnackInfo(mTilUserId, LoginActivity.this, "登录成功");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SnackBarUtil.showSnackInfo(mTilUserId, LoginActivity.this, "正在同步数据");
                                accountManager.initAccountData(new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        recordManager.initRecordTypeData(new Handler() {
                                            @Override
                                            public void handleMessage(Message msg) {
                                                super.handleMessage(msg);
                                                recordManager.initRecordData(new Handler() {
                                                    @Override
                                                    public void handleMessage(Message msg) {
                                                        super.handleMessage(msg);
                                                        new Handler().postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                setResult(RESULT_OK);
                                                                finish();
                                                            }
                                                        }, 600);
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }, 600);
                    }
                }
            }
        });

    }

    private boolean isNameAddressFormat(String email) {
        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void getPassBack(View view) {

    }


    private TextInputEditText mTitUserId, mTitPassWord;
    private TextInputLayout mTilUserId, mTilPassWord;
    private Button loginBt;
    private UserManager userManager;
    private AccountManager accountManager;
    private RecordManager recordManager;
    private boolean forgetGesture = false;
    private String orgUser;

}
