package com.suda.jzapp.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suda.jzapp.R;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.RecordDetailDO;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.adapter.NewRecordDetailAdapter;

import java.util.List;

/**
 * Created by suda on 2016/8/4.
 */
public class RecordDetailFrg extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recordManager = new RecordManager(getActivity());
        if (getArguments() != null) {
            recordTypeId = getArguments().getLong(IntentConstant.RECORD_TYPE_ID);
            year = getArguments().getInt(IntentConstant.RECORD_YEAR);
            month = getArguments().getInt(IntentConstant.RECORD_MONTH);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        recordDetails = recordManager.getRecordsByRecordTypeIDAndMonth(recordTypeId, year, month);
        mAccountRecordAdapter = new NewRecordDetailAdapter(getActivity(), recordDetails);
        mRecordLv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecordLv.setAdapter(mAccountRecordAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_record_detail, container, false);
        mRecordLv = (RecyclerView) view.findViewById(R.id.records);
        return view;
    }

    public static RecordDetailFrg newInstance(int year, int month, long recordTypeId) {
        RecordDetailFrg tabFragment = new RecordDetailFrg();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentConstant.RECORD_YEAR, year);
        bundle.putInt(IntentConstant.RECORD_MONTH, month);
        bundle.putLong(IntentConstant.RECORD_TYPE_ID, recordTypeId);
        tabFragment.setArguments(bundle);
        return tabFragment;
    }


    private int year = 2016;
    private int month = 1;
    private long recordTypeId = 0;
    private RecyclerView mRecordLv;
    private NewRecordDetailAdapter mAccountRecordAdapter;
    private List<RecordDetailDO> recordDetails;
    private RecordManager recordManager;

}
