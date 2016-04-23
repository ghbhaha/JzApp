package com.suda.jzapp.ui.activity.record;

import android.os.Bundle;
import android.widget.ListView;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.RecordDetailDO;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.adapter.AccountRecordAdapter;

import java.util.List;

public class RecordTypeDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyContentView(R.layout.activity_record_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recordMonth = getIntent().getIntExtra(IntentConstant.RECORD_MONTH, 0);
        recordYear = getIntent().getIntExtra(IntentConstant.RECORD_YEAR, 2016);
        recordTypeID = getIntent().getLongExtra(IntentConstant.RECORD_TYPE_ID, 0);
        recordDesc = getIntent().getStringExtra(IntentConstant.RECORD_DESC);
        getSupportActionBar().setTitle("'" + recordDesc + "'" + "明细");
        recordManager = new RecordManager(this);

        initWidget();

    }

    @Override
    protected void initWidget() {
        mRecordLv = (ListView) findViewById(R.id.records);

        recordDetails = recordManager.getRecordsByRecordTypeIDAndMonth(recordTypeID, recordYear, recordMonth);
        mAccountRecordAdapter = new AccountRecordAdapter(this, recordDetails);
        mRecordLv.setAdapter(mAccountRecordAdapter);
    }

    private AccountRecordAdapter mAccountRecordAdapter;
    private List<RecordDetailDO> recordDetails;
    private ListView mRecordLv;
    private int recordYear;
    private int recordMonth;
    private long recordTypeID;
    private String recordDesc;
    private RecordManager recordManager;
}
