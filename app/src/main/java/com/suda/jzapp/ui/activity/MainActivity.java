package com.suda.jzapp.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.suda.jzapp.service.SyncService;
import com.suda.jzapp.ui.activity.account.MonthReportActivity;
import com.suda.jzapp.ui.activity.system.AboutActivity;
import com.suda.jzapp.ui.activity.system.EditThemeActivity;
import com.suda.jzapp.ui.activity.system.SettingsActivity;
import com.suda.jzapp.ui.activity.user.LoginActivity;
import com.suda.jzapp.ui.activity.user.UserActivity;
import com.suda.jzapp.ui.activity.user.UserLinkActivity;
import com.suda.jzapp.ui.adapter.MyFragmentPagerAdapter;
import com.suda.jzapp.ui.adapter.OptMenuAdapter;
import com.suda.jzapp.util.SnackBarUtil;
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

        startService(new Intent(this, SyncService.class));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

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
                        Glide.with(MainActivity.this).
                                load(user.getHeadImage()).error(R.mipmap.suda)
                                .into(headImg);
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
        // mPagerSlidingTabStrip.setFadeEnabled(false);
        mPagerSlidingTabStrip.setZoomMax(0);
        mPagerSlidingTabStrip.setTextSize(14);
        mViewPager.setCurrentItem(1);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
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
        mPagerSlidingTabStrip.setTextColor(getColor(this, getMainTheme().getMainDarkColorID()));

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPagerSlidingTabStrip.setTextColor(getColor(this, getMainTheme().getMainDarkColorID()));
        mLayoutBackGround.setBackgroundResource(ThemeUtil.getTheme(this).getMainColorID());
        mPagerSlidingTabStrip.setBackgroundColor(getColor(this, ThemeUtil.getTheme(this).getMainColorID()));
        if (MyAVUser.getCurrentUser() != null) {
            userManager.getMe(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == Constant.MSG_SUCCESS) {
                        User user = (User) msg.obj;
                        Glide.with(MainActivity.this).
                                load(user.getHeadImage()).error(R.mipmap.suda)
                                .into(headImg);
                    }
                }
            });
        } else {
            Glide.with(MainActivity.this).
                    load(R.mipmap.ic_launcher)
                    .into(headImg);
        }
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

        optDOs.add(new OptDO(UserLinkActivity.class, 0, R.drawable.ic_drawer_friends, "关联账户"));
        optDOs.add(new OptDO(MonthReportActivity.class, 1, R.drawable.ic_drawer_guide, "月报"));
        optDOs.add(new OptDO(SettingsActivity.class, 2, R.drawable.ic_drawer_settings, "设置"));
        optDOs.add(new OptDO(EditThemeActivity.class, 0, R.drawable.ic_color_lens_black_24dp, "主题切换"));
        optDOs.add(new OptDO(AboutActivity.class, 3, R.drawable.ic_drawer_about, "关于"));
        optDOs.add(new OptDO(null, 4, R.drawable.ic_drawer_exit, "退出"));
        optMenuAdapter = new OptMenuAdapter(optDOs, this);
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
                SnackBarUtil.showSnackInfo(mPagerSlidingTabStrip, this, "再按一次退出");
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
                reloadAnalysisCallBack.reload(true);
            }
            if (requestCode == REQUEST_EDIT_THEME) {
                reloadRecordCallBack.reload(false);
                reloadAccountCallBack.reload(false);
                optMenuAdapter.notifyDataSetChanged();
                reloadAnalysisCallBack.reload(false);
            }
            if (requestCode == REQUEST_LOGIN) {
                reloadRecordCallBack.reload(true);
                reloadAccountCallBack.reload(true);
                reloadAnalysisCallBack.reload(true);
                userNameTv.setText(TextUtils.isEmpty(userManager.getCurUserName()) ?
                        "登陆/注册" : userManager.getCurUserName());
            }
            if (requestCode == REQUEST_ACCOUNT_FLOW) {
                reloadRecordCallBack.reload(true);
                reloadAccountCallBack.reload(true);
                reloadAnalysisCallBack.reload(true);
            }
        }
    }

    public void login(View view) {
        if (MyAVUser.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        } else {
            Intent intent = new Intent(this, UserActivity.class);
            startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    public void setReloadAccountCallBack(ReloadCallBack reloadAccountCallBack) {
        this.reloadAccountCallBack = reloadAccountCallBack;
    }

    public void setReloadRecordCallBack(ReloadCallBack reloadRecordCallBack) {
        this.reloadRecordCallBack = reloadRecordCallBack;
    }

    public void setReloadAnalysisCallBack(ReloadCallBack reloadAnalysisCallBack) {
        this.reloadAnalysisCallBack = reloadAnalysisCallBack;
    }

    public ReloadCallBack getReloadAccountCallBack() {
        return reloadAccountCallBack;
    }

    public ReloadCallBack getReloadAnalysisCallBack() {
        return reloadAnalysisCallBack;
    }

    public ReloadCallBack getReloadRecordCallBack() {
        return reloadRecordCallBack;
    }

    protected void getPermission(final String permission) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 0);
        }
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

    private ReloadCallBack reloadAccountCallBack;
    private ReloadCallBack reloadRecordCallBack;
    private ReloadCallBack reloadAnalysisCallBack;
    private OptMenuAdapter optMenuAdapter;


    public interface ReloadCallBack {
        void reload(boolean needUpdateData);
    }


    public final static int REQUEST_ACCOUNT = 100;
    public final static int REQUEST_RECORD = 101;
    public final static int REQUEST_EDIT_THEME = 102;
    public final static int REQUEST_LOGIN = 103;
    public final static int REQUEST_ACCOUNT_FLOW = 104;
}
