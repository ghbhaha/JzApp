package com.suda.jzapp.ui.activity;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gxz.PagerSlidingTabStrip;
import com.soundcloud.android.crop.Crop;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.BuildConfig;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.SystemManager;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.manager.domain.OptDO;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.service.BackupService;
import com.suda.jzapp.service.MyWidgetProvider;
import com.suda.jzapp.ui.activity.account.MonthReportActivity;
import com.suda.jzapp.ui.activity.record.ExportRecordActivity;
import com.suda.jzapp.ui.activity.system.AboutActivity;
import com.suda.jzapp.ui.activity.system.EditThemeActivity;
import com.suda.jzapp.ui.activity.system.SettingsActivity;
import com.suda.jzapp.ui.activity.user.LoginActivity;
import com.suda.jzapp.ui.activity.user.UserActivity;
import com.suda.jzapp.ui.activity.user.UserLinkActivity;
import com.suda.jzapp.ui.adapter.MyFragmentPagerAdapter;
import com.suda.jzapp.ui.adapter.OptMenuAdapter;
import com.suda.jzapp.util.ImageUtil;
import com.suda.jzapp.util.LauncherIconUtil;
import com.suda.jzapp.util.SPUtils;
import com.suda.jzapp.util.SnackBarUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mainAct = true;
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        startService(new Intent(this, BackupService.class));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO);
        }
        showYiYan();
        updateWidget();

        userManager = new UserManager(this);
        recordManager = new RecordManager(this);
        initWidget();
    }

    private void showYiYan() {
        if ((boolean) SPUtils.get(this, true, "yi_yan", true)) {
            new SystemManager(this).getYiYan(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == Constant.MSG_SUCCESS) {
                        ((TextView) findViewById(R.id.yiyan)).setText(msg.obj.toString());
                    }
                }
            });
        }
    }

    @Override
    protected void initWidget() {
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mLvOptItems = (ListView) findViewById(R.id.opt_items);
        mLayoutBackGround = findViewById(R.id.account_background);
        headImg = (CircleImageView) findViewById(R.id.profile_image);
        mLoadingBack = findViewById(R.id.loading_back);
        mLoadingBack.setBackgroundResource(getMainTheme().getMainColorID());
        mLoadingBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (loading)
                    return true;
                else
                    return false;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        Animator animator = ViewAnimationUtils.createCircularReveal(
                                mLoadingBack,
                                mLoadingBack.getWidth() / 2,
                                mLoadingBack.getHeight() / 2,
                                mLoadingBack.getWidth(),
                                0);
                        animator.setDuration(500);
                        animator.setInterpolator(new AccelerateDecelerateInterpolator());
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                mLoadingBack.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        animator.start();
                    } catch (Exception e) {

                    }
                } else
                    YoYo.with(Techniques.SlideOutUp).playOn(mLoadingBack);
                loading = false;
            }
        }, 2000);

        String userName = userManager.getCurUserName();
        userNameTv = (TextView) findViewById(R.id.user_tv);
        userNameTv.setText(TextUtils.isEmpty(userName) ?
                "登陆/注册" : (userName + recordManager.getRecordDayCount()));
        if (!TextUtils.isEmpty(userName)) {
            userManager.getMe(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == Constant.MSG_SUCCESS) {
                        User user = (User) msg.obj;
                        Glide.with(MainActivity.this.getApplicationContext()).
                                load(user.getHeadImage()).error(R.mipmap.suda)
                                .into(headImg);
                    } else {
                        userManager.logOut(true);
                    }
                }
            });
        }

        if ((int) SPUtils.get(this, Constant.SP_NAV_IMG_TYPE, 0) == 0) {
            mLayoutBackGround.setBackgroundResource(ThemeUtil.getTheme(this).getMainColorID());
        } else {
            Bitmap bitmap = ImageUtil.getBitmapByImgName(this, Constant.NAV_IMG);
            if (bitmap != null) {
                BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
                mLayoutBackGround.setBackground(bd);
            } else
                mLayoutBackGround.setBackgroundResource(ThemeUtil.getTheme(this).getMainColorID());
        }

        mLayoutBackGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayAdapter<String> arrayAdapter
                        = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_list_item_1);
                arrayAdapter.add("默认");
                arrayAdapter.add("自定义");

                ListView listView = new ListView(MainActivity.this);
                listView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                listView.setDividerHeight(1);
                listView.setAdapter(arrayAdapter);

                final MaterialDialog alert = new MaterialDialog(MainActivity.this).setContentView(listView);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            mLayoutBackGround.setBackgroundResource(ThemeUtil.getTheme(MainActivity.this).getMainColorID());
                            SPUtils.put(MainActivity.this, Constant.SP_NAV_IMG_TYPE, 0);
                        } else {
                            Intent it = new Intent(Intent.ACTION_PICK, null);
                            it.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(it, REQUEST_SELECT_IMAGE);
                        }
                        alert.dismiss();
                    }
                });
                alert.setCanceledOnTouchOutside(true);
                alert.show();
            }
        });

        // 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitleTextAppearance(this, R.style.MenuTextStyle);
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
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
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
        mPagerSlidingTabStrip.setIndicatorHeight(2);
        // 选中的文字颜色
        mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(getColor(this, getMainTheme().getMainColorID()) & 0x3f000000);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPagerSlidingTabStrip.setTextColor(getColor(this, getMainTheme().getMainColorID()) & 0x3f000000);
        if ((int) (SPUtils.get(this, Constant.SP_NAV_IMG_TYPE, 0)) == 0) {
            mLayoutBackGround.setBackgroundResource(ThemeUtil.getTheme(this).getMainColorID());
        }
        mPagerSlidingTabStrip.setBackgroundColor(getColor(this, ThemeUtil.getTheme(this).getMainColorID()));
        if (MyAVUser.getCurrentUser() != null) {
            userManager.getMe(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if (msg.what == Constant.MSG_SUCCESS) {
                        User user = (User) msg.obj;
                        Glide.with(MainActivity.this.getApplicationContext()).
                                load(user.getHeadImage()).error(R.mipmap.suda)
                                .into(headImg);
                    }
                }
            });
        } else {
            Glide.with(MainActivity.this.getApplicationContext()).
                    load(LauncherIconUtil.getLauncherIcon(this))
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

        if (BuildConfig.USERLINK)
            optDOs.add(new OptDO(UserLinkActivity.class, 0, R.drawable.ic_drawer_friends, "关联账户"));
        optDOs.add(new OptDO(MonthReportActivity.class, 1, R.drawable.ic_drawer_guide, "月报"));
        optDOs.add(new OptDO(ExportRecordActivity.class, 2, R.drawable.ic_drawer_export, "数据导出"));
        optDOs.add(new OptDO(SettingsActivity.class, 3, R.drawable.ic_drawer_settings, "设置"));
        optDOs.add(new OptDO(EditThemeActivity.class, 4, R.drawable.ic_color_lens_black_24dp, "主题切换"));
        optDOs.add(new OptDO(null, 5, R.drawable.ic_drawer_check_update, "检查更新"));
        optDOs.add(new OptDO(AboutActivity.class, 6, R.drawable.ic_drawer_about, "关于"));
        optDOs.add(new OptDO(null, 7, R.drawable.ic_drawer_exit, "退出"));
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
                userManager.setUserLink(null);
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
                updateWidget();
                reloadRecordCallBack.reload(true);
                reloadAccountCallBack.reload(true);
                reloadAnalysisCallBack.reload(true);
                userNameTv.setText(TextUtils.isEmpty(userManager.getCurUserName()) ?
                        "登陆/注册" : (userManager.getCurUserName() + recordManager.getRecordDayCount()));
            }
            if (requestCode == REQUEST_EDIT_THEME) {
                reloadRecordCallBack.reload(false);
                reloadAccountCallBack.reload(false);
                optMenuAdapter.notifyDataSetChanged();
                reloadAnalysisCallBack.reload(false);
            }
            if (requestCode == REQUEST_LOGIN) {
                updateWidget();
                reloadRecordCallBack.reload(true);
                reloadAccountCallBack.reload(true);
                reloadAnalysisCallBack.reload(true);
                userNameTv.setText(TextUtils.isEmpty(userManager.getCurUserName()) ?
                        "登陆/注册" : (userManager.getCurUserName() + new RecordManager(MainActivity.this).getRecordDayCount()));
            }
            if (requestCode == REQUEST_ACCOUNT_FLOW) {
                updateWidget();
                reloadRecordCallBack.reload(true);
                reloadAccountCallBack.reload(true);
                reloadAnalysisCallBack.reload(true);
            }
            if (requestCode == REQUEST_ACCOUNT_TRANSFORM) {
                reloadAccountCallBack.reload(true);
            }
            if (requestCode == REQUEST_SELECT_IMAGE) {
                if (data.getData() != null)
                    cropPhoto(data.getData());
            }
            if (requestCode == REQUEST_CROP_IMAGE) {
                SPUtils.put(MainActivity.this, Constant.SP_NAV_IMG_TYPE, 1);
                Bitmap bitmap = ImageUtil.getBitmapByImgName(this, Constant.NAV_IMG);
                if (bitmap != null) {
                    BitmapDrawable bd = new BitmapDrawable(getResources(), bitmap);
                    mLayoutBackGround.setBackground(bd);
                } else
                    mLayoutBackGround.setBackgroundResource(ThemeUtil.getTheme(this).getMainColorID());
            }
            if (requestCode == REQUEST_ABOUT)
                optMenuAdapter.notifyDataSetChanged();
        }
    }

    public void cropPhoto(Uri uri) {
        Crop.of(uri, Uri.fromFile(ImageUtil.getPathByImageName(this, Constant.NAV_IMG))).asSquare().withAspect(400, 250)
                .start(this, REQUEST_CROP_IMAGE);
    }

    public void refreshAll() {
        updateWidget();
        reloadRecordCallBack.reload(true);
        reloadAccountCallBack.reload(true);
        reloadAnalysisCallBack.reload(true);
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

    private void updateWidget() {
        Intent intent = new Intent();
        intent.setAction(MyWidgetProvider.WIDGET_BROADCAST);
        sendBroadcast(intent);
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

    protected void getPermission(final String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 0);
                break;
            }
        }
    }

    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mLvOptItems;
    private View mLayoutBackGround;
    private TextView userNameTv;
    private CircleImageView headImg;
    private View mLoadingBack;

    private boolean openOrClose = false;
    private boolean canQuit = false;

    private UserManager userManager;
    private RecordManager recordManager;

    private ReloadCallBack reloadAccountCallBack;
    private ReloadCallBack reloadRecordCallBack;
    private ReloadCallBack reloadAnalysisCallBack;
    private OptMenuAdapter optMenuAdapter;

    private boolean loading = true;

    public interface ReloadCallBack {
        void reload(boolean needUpdateData);
    }

    public final static int REQUEST_ACCOUNT = 100;
    public final static int REQUEST_RECORD = 101;
    public final static int REQUEST_EDIT_THEME = 102;
    public final static int REQUEST_LOGIN = 103;
    public final static int REQUEST_ACCOUNT_FLOW = 104;
    public final static int REQUEST_ACCOUNT_TRANSFORM = 105;
    public final static int REQUEST_SELECT_IMAGE = 106;
    public final static int REQUEST_CROP_IMAGE = 107;
    public final static int REQUEST_ABOUT = 108;


}
