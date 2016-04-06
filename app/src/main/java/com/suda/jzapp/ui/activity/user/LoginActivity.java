package com.suda.jzapp.ui.activity.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.misc.Constant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userManager = new UserManager(this);
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
    }

    private void doLogin() {
        boolean isEmail = false;
        String user = mTitUserId.getText().toString();
        String password = mTitPassWord.getText().toString();
        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(password)) {
            return;
        }
        isEmail = isNameAddressFormat(user);
        userManager.login(isEmail ? null : user, password, isEmail ? user : null, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constant.MSG_ERROR) {
                    Snackbar.make(mTilUserId, msg.obj.toString(), Snackbar.LENGTH_SHORT)
                            .setAction("Action", null)
                            .show();
                } else {
                    Snackbar.make(mTilUserId, "登录成功", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null)
                            .show();
                }
            }
        });

    }

    private boolean isNameAddressFormat(String email) {
        boolean isExist = false;

        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Matcher m = p.matcher(email);
        boolean b = m.matches();
        if (b) {
            System.out.println("有效邮件地址");
            isExist = true;
        } else {
            System.out.println("无效邮件地址");
        }
        return isExist;
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

}
