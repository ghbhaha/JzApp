package com.suda.jzapp.ui.activity.system;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.AppUtil;
import com.suda.jzapp.util.LauncherIconUtil;
import com.suda.jzapp.util.SPUtils;

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
        needUpdate = true;
        // ((TextView) findViewById(R.id.introduce)).setText(Html.fromHtml(getString(R.string.introduce)));
    }

    @Override
    protected void initWidget() {
        ((TextView) findViewById(R.id.tv_version)).setText("Version " + AppUtil.getAppInfo(this).versionName);
        findViewById(R.id.background).setBackgroundColor(getColor(this, getMainTheme().getMainColorID()));
        ((CircleImageView) findViewById(R.id.appIcon)).setImageResource(LauncherIconUtil.getLauncherIcon(this));
        mTipDonate = findViewById(R.id.tip_donate);
        resetTipRound();
    }

    public void donate(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri.parse("https://qr.alipay.com/apqiqql0hgh5pmv54d"));
            startActivity(intent);
        } catch (Exception e) {
            intent.setData(Uri.parse("http://d.alipay.com"));
            startActivity(intent);
        }
        SPUtils.put(this, Constant.SP_TIP_DONATE, false);
        resetTipRound();
    }

    private void resetTipRound() {
        boolean showEditBudgetTip = (boolean) SPUtils.get(this, Constant.SP_TIP_DONATE, true);
        mTipDonate.setVisibility(showEditBudgetTip ? View.VISIBLE : View.GONE);
        if (showEditBudgetTip) {
            AnimatorSet mAnimatorSet = new AnimatorSet();
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mTipDonate, "alpha", 1, 0);
            objectAnimator.setRepeatMode(Animation.RESTART);
            objectAnimator.setRepeatCount(Integer.MAX_VALUE);
            objectAnimator.setDuration(1000);
            mAnimatorSet.playTogether(objectAnimator);
            mAnimatorSet.start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            setResult(RESULT_OK);
        return super.onKeyDown(keyCode, event);
    }

    private View mTipDonate;

    private Toolbar mToolbar;
}
