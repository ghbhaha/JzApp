package com.suda.jzapp.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.suda.jzapp.R;
import com.suda.jzapp.ui.activity.record.CreateOrEditRecordActivity;
import com.suda.jzapp.util.ThemeUtil;

/**
 * Created by ghbha on 2016/2/15.
 */
public class RecordFrg extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.record_frg_layout, container, false);
        backGround = view.findViewById(R.id.background);
        mAddRecordBt = (FloatingActionButton)view.findViewById(R.id.add_new_record);
        mAddRecordBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateOrEditRecordActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainColor = getResources().getColor(ThemeUtil.getTheme(getActivity()).getMainColorID());
        mainDarkColor = getResources().getColor(ThemeUtil.getTheme(getActivity()).getMainDarkColorID());
        mAddRecordBt.setColorNormal(mainColor);
        mAddRecordBt.setColorPressed(mainDarkColor);
        backGround.setBackground(new ColorDrawable(mainColor));
    }

    private View backGround;
    private int mainColor;
    private int mainDarkColor;
    private FloatingActionButton mAddRecordBt;
}
