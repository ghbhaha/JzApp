package com.suda.jzapp.ui.activity.user;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.util.LogUtils;

public class RegisterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWidget();
    }


    @Override
    protected void initWidget() {
        mTieUserID = (TextInputEditText) findViewById(R.id.userid);
        mTieEmail = (TextInputEditText) findViewById(R.id.email);
        mTiePass = (TextInputEditText) findViewById(R.id.pass);
        mTiePass2 = (TextInputEditText) findViewById(R.id.pass2);
        mRegisterBt = (Button) findViewById(R.id.register_bt);

        mRegisterBt.setBackgroundColor(getColor(this, getMainTheme().getMainColorID()));

        mRegisterBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {

    }


    private TextInputEditText mTieUserID, mTieEmail, mTiePass, mTiePass2;
    private Button mRegisterBt;
}
