package com.suda.jzapp.ui.activity.user;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.util.ExceptionInfoUtil;
import com.suda.jzapp.util.KeyBoardUtils;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.SnackBarUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserGetPassBackActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getAppStyleId(this));
        setMyContentView(R.layout.activity_user_get_pass_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWidget();
    }

    @Override
    protected void initWidget() {
        mEtEmail = (TextInputEditText) findViewById(R.id.email);
        mBtSubmit = (Button) findViewById(R.id.submit);
        mBtSubmit.setTextColor(getColor(this, getMainTheme().getMainColorID()));
        mBtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String email = mEtEmail.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    SnackBarUtil.showSnackInfo(v, UserGetPassBackActivity.this, "邮箱不能为空");
                    return;
                }
                if (!isNameAddressFormat(email)) {
                    SnackBarUtil.showSnackInfo(v, UserGetPassBackActivity.this, "请填入合法邮箱");
                    return;
                }


                KeyBoardUtils.closeKeybord(mEtEmail, UserGetPassBackActivity.this);
                MyAVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                    @Override
                    public void done(AVException e) {
                        LogUtils.getAvEx(e, UserGetPassBackActivity.this);
                        if (e == null) {
                            SnackBarUtil.showSnackInfo(v, UserGetPassBackActivity.this, "密码重置已发送到邮箱，请注意查收");
                        } else {
                            SnackBarUtil.showSnackInfo(v, UserGetPassBackActivity.this, ExceptionInfoUtil.getError(e.getCode()));
                        }
                    }
                });
            }
        });
    }

    private boolean isNameAddressFormat(String email) {
        Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private TextInputEditText mEtEmail;
    private Button mBtSubmit;
}
