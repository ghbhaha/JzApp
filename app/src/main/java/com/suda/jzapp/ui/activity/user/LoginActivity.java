package com.suda.jzapp.ui.activity.user;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.util.ThemeUtil;
import com.suda.jzapp.view.LoadingButton;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWidget();
    }

    @Override
    protected void initWidget() {
        mTitUserId = (TextInputEditText) findViewById(R.id.userid);
        mPassWord = (TextInputEditText) findViewById(R.id.pass);


    }


    private TextInputEditText mTitUserId;
    private TextInputEditText mPassWord;


}
