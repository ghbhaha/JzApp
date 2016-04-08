package com.suda.jzapp.ui.activity.user;

import android.os.Bundle;
import android.view.View;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.UserManager;

public class UserActivity extends BaseActivity {

    @Override
    protected void initWidget() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userManager = new UserManager(this);
    }

    public void logOut(View view) {
        userManager.logOut();
        setResult(RESULT_OK );
        this.finish();
    }

    private UserManager userManager;
}
