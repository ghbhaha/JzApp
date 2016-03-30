package com.suda.jzapp.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.suda.jzapp.R;
import com.suda.jzapp.ui.fragment.AccountFrg;
import com.suda.jzapp.ui.fragment.AnalysisFrg;
import com.suda.jzapp.ui.fragment.RecordFrg;

/**
 * Created by ghbha on 2016/2/15.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    private final String[] TITLES = {"1","2","3"};

    public MyFragmentPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.mContext = context;
        String[] title  = mContext.getResources().getStringArray(
                R.array.tab_title);
        TITLES[0] = title[0];
        TITLES[1] = title[1];
        TITLES[2] = title[2];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0 :
                return new AccountFrg();
            case 1 :
                return new RecordFrg();
            case 2 :
                return new AnalysisFrg();
            default :
                return null;
        }

    }
}
