package com.suda.jzapp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gxz.PagerSlidingTabStrip;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.manager.domain.OptDO;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.ui.activity.account.AccountLinkActivity;
import com.suda.jzapp.ui.activity.account.MonthReportActivity;
import com.suda.jzapp.ui.activity.system.AboutActivity;
import com.suda.jzapp.ui.activity.system.EditThemeActivity;
import com.suda.jzapp.ui.activity.system.SettingsActivity;
import com.suda.jzapp.ui.activity.user.LoginActivity;
import com.suda.jzapp.ui.adapter.MyFragmentPagerAdapter;
import com.suda.jzapp.ui.adapter.OptMenuAdapter;
import com.suda.jzapp.util.TextUtil;
import com.suda.jzapp.util.ThemeUtil;
import com.umeng.update.UmengUpdateAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UmengUpdateAgent.update(this);
        userManager = new UserManager(this);
        initWidget();
    }

    @Override
    protected void initWidget() {
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mLvOptItems = (ListView) findViewById(R.id.opt_items);
        mLayoutBackGround = (RelativeLayout) findViewById(R.id.account_background);
        headImg = (CircleImageView) findViewById(R.id.profile_image);

        String userName = userManager.getCurUserName();
        userNameTv = (TextView) findViewById(R.id.user_tv);
        userNameTv.setText(TextUtils.isEmpty(userName) ?
                "登陆/注册" : userName);
        if (!TextUtils.isEmpty(userName)) {
            userManager.getMe(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == Constant.MSG_SUCCESS) {
                        User user = (User) msg.obj;
                        Glide.with(MainActivity.this).load(user.getHeadImage()).into(headImg);
                    }
                }
            });
        }

        mLayoutBackGround.setBackgroundResource(ThemeUtil.getTheme(this).getMainColorID());

        // 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);

        //设置抽屉
        setDrawerLayout();

        initViewPager();

    }

    private void initViewPager() {
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), this));
        mViewPager.setOffscreenPageLimit(3);


        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip
                .setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int arg0) {
                        // colorChange(arg0);
                    }

                    @Override
                    public void onPageScrolled(int arg0, float arg1, int arg2) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                    }
                });

        // 底部游标颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.WHITE);

        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mPagerSlidingTabStrip.setBackgroundColor(getColor(this, ThemeUtil.getTheme(this).getMainColorID()));

        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources()
                        .getDisplayMetrics()));


        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources()
                        .getDisplayMetrics()));
        // 选中的文字颜色
        mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(Color.BLACK);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLayoutBackGround.setBackgroundResource(ThemeUtil.getTheme(this).getMainColorID());
        mPagerSlidingTabStrip.setBackgroundColor(getColor(this, ThemeUtil.getTheme(this).getMainColorID()));
    }

    private void setDrawerLayout() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                mToolbar, R.string.open, R.string.close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
                openOrClose = false;
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                openOrClose = true;
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //初始化菜单
        List<OptDO> optDOs = new ArrayList<>();

        optDOs.add(new OptDO(AccountLinkActivity.class, 0, R.drawable.ic_drawer_friends, "关联账户"));
        optDOs.add(new OptDO(MonthReportActivity.class, 1, R.drawable.ic_drawer_guide, "月报"));
        optDOs.add(new OptDO(SettingsActivity.class, 2, R.drawable.ic_drawer_settings, "设置"));
        optDOs.add(new OptDO(EditThemeActivity.class, 0, R.drawable.ic_drawer_friends, "主题切换"));
        optDOs.add(new OptDO(AboutActivity.class, 3, R.drawable.ic_drawer_about, "关于"));
        optDOs.add(new OptDO(null, 4, R.drawable.ic_drawer_exit, "退出"));
        OptMenuAdapter optMenuAdapter = new OptMenuAdapter(optDOs, this);
        mLvOptItems.setAdapter(optMenuAdapter);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (openOrClose) {
                mDrawerLayout.closeDrawers();
                return true;
            }

            if (canQuit) {
                this.finish();
            } else {
                Snackbar.make(mPagerSlidingTabStrip, "再按一次退出", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null)
                        .show();
            }
            canQuit = true;

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    canQuit = false;
                }
            }, 1500);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ACCOUNT) {
                reloadAccountCallBack.reload(true);
            }
            if (requestCode == REQUEST_RECORD) {
                reloadRecordCallBack.reload(true);
                reloadAccountCallBack.reload(true);
            }
            if (requestCode == REQUEST_EDIT_THEME) {
                reloadRecordCallBack.reload(false);
                reloadAccountCallBack.reload(false);
            }
            if (requestCode == REQUEST_LOGIN) {
                reloadRecordCallBack.reload(true);
                reloadAccountCallBack.reload(true);
                userNameTv.setText(TextUtils.isEmpty(userManager.getCurUserName()) ?
                        "登陆/注册" : userManager.getCurUserName());
            }
        }
    }

    public void login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    public void setReloadAccountCallBack(ReloadAccountCallBack reloadAccountCallBack) {
        this.reloadAccountCallBack = reloadAccountCallBack;
    }

    public void setReloadRecordCallBack(ReloadRecordCallBack reloadRecordCallBack) {
        this.reloadRecordCallBack = reloadRecordCallBack;
    }

    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mLvOptItems;
    private RelativeLayout mLayoutBackGround;
    private TextView userNameTv;
    private CircleImageView headImg;

    private boolean openOrClose = false;
    private boolean canQuit = false;

    private UserManager userManager;

    private ReloadAccountCallBack reloadAccountCallBack;
    private ReloadRecordCallBack reloadRecordCallBack;

    public interface ReloadAccountCallBack {
        void reload(boolean needUpdateData);
    }

    public interface ReloadRecordCallBack {
        void reload(boolean needUpdateData);
    }

    public final static int REQUEST_ACCOUNT = 100;
    public final static int REQUEST_RECORD = 101;
    public final static int REQUEST_EDIT_THEME = 102;
    public final static int REQUEST_LOGIN = 103;
}
