package com.suda.jzapp.ui.activity.system;

import android.os.Bundle;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void initWidget() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
