package com.suda.jzapp.ui.activity.system;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.util.AppUtil;
import com.suda.jzapp.util.LauncherIconUtil;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyContentView(false, R.layout.activity_abount);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AboutActivity.this.onBackPressed();
                    }
                }
        );
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initWidget();
        // ((TextView) findViewById(R.id.introduce)).setText(Html.fromHtml(getString(R.string.introduce)));
    }

    @Override
    protected void initWidget() {
        ((TextView) findViewById(R.id.tv_version)).setText("Version " + AppUtil.getAppInfo(this).versionName);
        findViewById(R.id.background).setBackgroundColor(getColor(this, getMainTheme().getMainColorID()));
        ((CircleImageView)findViewById(R.id.appIcon)).setImageResource(LauncherIconUtil.getLauncherIcon(this));
    }

    private Toolbar mToolbar;
}
