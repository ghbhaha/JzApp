package com.suda.jzapp.ui.activity.record;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.ChartRecordDo;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.adapter.RecordDetailFrgAdapter;
import com.suda.jzapp.ui.fragment.RecordDetailFrg;

import java.util.ArrayList;
import java.util.List;

public class NewRecordTypeDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_record_type_detail);
        mRecordManager = new RecordManager(this);
        initWidget();
        initParam();
    }

    private void initParam() {
        pieOut = getIntent().getBooleanExtra(IntentConstant.RECORD_OUT_IN, false);
        year = getIntent().getIntExtra(IntentConstant.RECORD_YEAR, 2016);
        month = getIntent().getIntExtra(IntentConstant.RECORD_MONTH, 1);
        recordTypeID = getIntent().getLongExtra(IntentConstant.RECORD_TYPE_ID, 0);

        chartRecordDoList = new ArrayList<>();

        mRecordManager.getOutOrInRecordByMonth(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                chartRecordDoList.clear();
                chartRecordDoList.addAll((List<ChartRecordDo>) msg.obj);
                initTabLayout();
                mToolbar.setTitle(year + "." + (month + 1) + (pieOut ? "支出" : "收入") + "明细");
            }
        }, pieOut, year, month);
    }

    private void initTabLayout() {
        int i = 0;
        int position = 0;
        for (ChartRecordDo chartRecordDo : chartRecordDoList) {
            if (recordTypeID == chartRecordDo.getRecordTypeID()) {
                position = i;
            }
            viewPagerAdapter.addFragment(RecordDetailFrg.newInstance(year, month, chartRecordDo.getRecordTypeID()), chartRecordDo.getRecordDesc());
            i++;
        }
        if (i < 5) {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        mViewPage.setAdapter(viewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPage);
        mViewPage.setCurrentItem(position);
    }

    @Override
    protected void initWidget() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(getResources().getString(R.string.app_name));
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mTabLayout.setBackgroundResource(getMainTheme().getMainColorID());
        mTabLayout.setSelectedTabIndicatorColor(getColor(this, R.color.white));
        mTabLayout.setTabTextColors(getColor(this, getMainTheme().getMainDarkColorID()), getColor(this, R.color.white));

        mViewPage = (ViewPager) findViewById(R.id.viewPager);

        viewPagerAdapter = new RecordDetailFrgAdapter(getSupportFragmentManager());
    }


    RecordDetailFrgAdapter viewPagerAdapter;
    private List<ChartRecordDo> chartRecordDoList;
    private RecordManager mRecordManager;
    private TabLayout mTabLayout;
    private Toolbar mToolbar;
    private ViewPager mViewPage;
    private boolean pieOut = true;
    private int year = 2016;
    private int month = 1;
    private long recordTypeID = 0;
}
