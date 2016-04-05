package com.suda.jzapp.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.bean.RecordDetailDO;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.ui.activity.MainActivity;
import com.suda.jzapp.ui.activity.record.CreateOrEditRecordActivity;
import com.suda.jzapp.ui.adapter.RecordAdapter;
import com.suda.jzapp.util.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghbha on 2016/2/15.
 */
public class RecordFrg extends Fragment implements MainActivity.ReloadRecordCallBack {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        recordManager = new RecordManager(getActivity());

        View view = inflater.inflate(R.layout.record_frg_layout, container, false);
        backGround = view.findViewById(R.id.background);
        mAddRecordBt = (FloatingActionButton) view.findViewById(R.id.add_new_record);
        mAddRecordBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateOrEditRecordActivity.class);
                getActivity().startActivityForResult(intent,MainActivity.REQUEST_RECORD);
            }
        });

        recordLv = (ListView) view.findViewById(R.id.record_lv);
        recordDetailDOs = new ArrayList<>();
        mRecordAdapter = new RecordAdapter(getActivity(), recordDetailDOs);
        recordLv.setAdapter(mRecordAdapter);
        recordManager.getRecordByPageIndex(1, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constant.MSG_SUCCESS) {
                    recordDetailDOs.clear();
                    recordDetailDOs.addAll((List<RecordDetailDO>) msg.obj);
                    mRecordAdapter.notifyDataSetChanged();
                }
            }
        });

        ((MainActivity) getActivity()).setReloadRecordCallBack(this);
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

    @Override
    public void reload() {
        recordManager.getRecordByPageIndex(1, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constant.MSG_SUCCESS) {
                    recordDetailDOs.clear();
                    recordDetailDOs.addAll((List<RecordDetailDO>) msg.obj);
                    mRecordAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private ListView recordLv;
    private View backGround;
    private int mainColor;
    private int mainDarkColor;
    private FloatingActionButton mAddRecordBt;
    private RecordManager recordManager;
    private RecordAdapter mRecordAdapter;
    private List<RecordDetailDO> recordDetailDOs;
}
