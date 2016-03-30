package com.suda.jzapp.ui.activity.system;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void initWidget() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abount);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutActivity.this.onBackPressed();
                    }
                }
        );

    }

    private Toolbar mToolbar;
}
