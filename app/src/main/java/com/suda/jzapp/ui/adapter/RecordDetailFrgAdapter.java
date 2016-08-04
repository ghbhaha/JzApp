package com.suda.jzapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suda on 2016/8/4.
 */
public class RecordDetailFrgAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentsTitles = new ArrayList<>();

    public RecordDetailFrgAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String fragmentTitle) {
        mFragments.add(fragment);
        mFragmentsTitles.add(fragmentTitle);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        //返回Fragment的数量
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentsTitles.get(position);
    }
}
