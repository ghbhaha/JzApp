package com.suda.jzapp;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.suda.jzapp.manager.domain.ThemeDO;
import com.suda.jzapp.util.DensityUtils;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.SPUtils;
import com.suda.jzapp.util.ScreenUtils;
import com.suda.jzapp.util.StatusBarCompat;
import com.suda.jzapp.util.ThemeUtil;
import com.umeng.analytics.MobclickAgent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Suda on 2015/10/10.
 */
public abstract class BaseActivity extends AppCompatActivity {


    protected abstract void initWidget();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainColor = this.getResources().getColor(ThemeUtil.getTheme(this).getMainColorID());
        mainDarkColor = this.getResources().getColor(ThemeUtil.getTheme(this).getMainDarkColorID());
        textColor = this.getResources().getColor(ThemeUtil.getTheme(this).getTextColorID());
    }


    /**
     * FloatingActionButton
     * CoordinatorLayout
     * 存在边距问题，采取此方案
     *
     * @param viewId
     */
    protected void setMyContentView2(int viewId) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setContentView(viewId);
            headView = findViewById(R.id.headView);
            headView.getLayoutParams().height = ScreenUtils.getStatusHeight(this) + DensityUtils.dp2px(this, 56);
        } else {
            setContentView(viewId);
        }
    }

    @Deprecated
    protected void setMyContentView(int viewId) {
        setMyContentView(true, viewId);
    }

    protected void setMyContentView(boolean paddingTop, int viewId) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            headView = new View(this);
            headView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getStatusHeight(this)));
            linearLayout.addView(headView);
            View contentView = View.inflate(this, viewId, null);
            if (paddingTop) {
                int paddingLeftOrg = contentView.getPaddingLeft();
                int paddingTopOrg = contentView.getPaddingTop();
                int paddingRightOrg = contentView.getPaddingRight();
                int paddingBottomOrg = contentView.getPaddingBottom();
                contentView.setPadding(paddingLeftOrg, DensityUtils.dp2px(this, 56) + paddingTopOrg, paddingRightOrg, paddingBottomOrg);
                linearLayout.addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                linearLayout.addView(contentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
            setContentView(linearLayout);
        } else {
            setContentView(viewId);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);
        }
        super.onResume();
        MobclickAgent.onResume(this);
        mainColor = this.getResources().getColor(ThemeUtil.getTheme(this).getMainColorID());
        mainDarkColor = this.getResources().getColor(ThemeUtil.getTheme(this).getMainDarkColorID());
        textColor = this.getResources().getColor(ThemeUtil.getTheme(this).getTextColorID());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(mainColor));
            if ((boolean) SPUtils.get(this, true, getResources().getString(R.string.immersive_status_bar), false))
                StatusBarCompat.compat(this, mainColor);
            else
                StatusBarCompat.compat(this, mainDarkColor);
        }
        if (headView != null)
            headView.setBackgroundColor(mainColor);
    }

    protected int getColor(Context context, int id) {
        return context.getResources().getColor(id);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (needUpdate) {
                    setResult(RESULT_OK);
                }
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected static String fmDate(Date date) {
        DateFormat format = new SimpleDateFormat("MM-dd");
        return format.format(date);
    }

    protected void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected int getTypeIconId(int iconType) {
        return IconTypeUtil.getTypeIcon(iconType);
    }

    protected ThemeDO getMainTheme() {
        return ThemeUtil.getTheme(this);
    }

    protected int mainColor;
    protected int mainDarkColor;
    protected int textColor;
    protected boolean needUpdate = false;
    protected View headView;
}
