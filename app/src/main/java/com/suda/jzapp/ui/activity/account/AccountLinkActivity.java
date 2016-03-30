package com.suda.jzapp.ui.activity.account;

import android.os.Bundle;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;

public class AccountLinkActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_link);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initWidget() {

    }
}
