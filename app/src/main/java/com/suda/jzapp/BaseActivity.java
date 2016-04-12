package com.suda.jzapp;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.suda.jzapp.manager.domain.ThemeDO;
import com.suda.jzapp.util.IconTypeUtil;
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


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        mainColor = this.getResources().getColor(ThemeUtil.getTheme(this).getMainColorID());
        mainDarkColor = this.getResources().getColor(ThemeUtil.getTheme(this).getMainDarkColorID());
        textColor = this.getResources().getColor(ThemeUtil.getTheme(this).getTextColorID());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(mainColor));
            StatusBarCompat.compat(this, mainDarkColor);
        }
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
}
