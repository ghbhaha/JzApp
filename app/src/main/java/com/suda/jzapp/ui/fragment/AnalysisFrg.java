package com.suda.jzapp.ui.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suda.jzapp.R;
import com.suda.jzapp.util.ThemeUtil;

/**
 * Created by ghbha on 2016/2/15.
 */
public class AnalysisFrg extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.analysis_frg_layout, container, false);
        backGround = view.findViewById(R.id.background);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainColor = getResources().getColor(ThemeUtil.getTheme(getActivity()).getMainColorID());
        mainDarkColor = getResources().getColor(ThemeUtil.getTheme(getActivity()).getMainDarkColorID());

        backGround.setBackground(new ColorDrawable(mainColor));
    }


    private View backGround;
    private int mainColor;
    private int mainDarkColor;
}
