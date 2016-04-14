package com.suda.jzapp.ui.activity.record;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.RecordType;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.adapter.RecordTypeIconAdapter;
import com.suda.jzapp.util.SnackBarUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

public class CreateNewRecordTypeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getAppStyleId(this));
        setContentView(R.layout.activity_create_new_record_type);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recordManager = new RecordManager(this);
        mRecordType = getIntent().getIntExtra(IntentConstant.RECORD_TYPE, 0);
        initWidget();
    }


    @Override
    protected void initWidget() {
        mEtRecordName = (EditText) findViewById(R.id.record_name);
        mIvRecordIcon = (ImageView) findViewById(R.id.record_icon);

        mAddFab = (FloatingActionButton) findViewById(R.id.fab);
        //mAddFab.setBackgroundTintList(getResources().getColorStateList(getMainTheme().getMainColorID()));
        mAddFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewRecord(view);
            }
        });

        mIonsGV = (GridView) findViewById(R.id.record_icon_gv);
        mIconsList = new ArrayList<>();
        for (int i = 0; i < Constant.RecordTypeConstant.ICON_TYPE_COUNT; i++) {
            mIconsList.add(i);
        }

        mCurrentIcon = 0;
        mIvRecordIcon.setImageResource(getTypeIconId(mCurrentIcon));

        mIonsGV.setAdapter(new RecordTypeIconAdapter(this, mIconsList));

        mIonsGV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentIcon = mIconsList.get(position);
                mIvRecordIcon.setImageResource(getTypeIconId(mCurrentIcon));
            }
        });
    }

    private void addNewRecord(View view) {
        String recordName = mEtRecordName.getText().toString();
        if (TextUtils.isEmpty(recordName)) {
            SnackBarUtil.showSnackInfo(view, this, getString(R.string.please_enter_record_type_name));
            return;
        }

        RecordType recordType = new RecordType();
        recordType.setRecordType(mRecordType);
        recordType.setRecordTypeID(System.currentTimeMillis());
        recordType.setRecordIcon(mCurrentIcon);
        recordType.setRecordDesc(recordName);
        recordType.setIsDel(false);
        recordManager.createNewRecordType(recordType, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                setResult(RESULT_OK);
                finish();
            }
        });
    }


    private FloatingActionButton mAddFab;
    private GridView mIonsGV;
    private EditText mEtRecordName;
    private ImageView mIvRecordIcon;
    private List<Integer> mIconsList;
    private int mCurrentIcon;
    private RecordManager recordManager;
    private int mRecordType;

}
