package com.suda.jzapp.ui.activity.account;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.AccountManager;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.AccountDetailDO;
import com.suda.jzapp.manager.domain.RecordDetailDO;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.adapter.AccountRecordAdapter;
import com.suda.jzapp.util.DateTimeUtil;
import com.suda.jzapp.util.IconTypeUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class AccountTransactionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_transaction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mCurAccountId = getIntent().getLongExtra(IntentConstant.ACCOUNT_ID, 0);
        recordManager = new RecordManager(this);
        accountManager = new AccountManager(this);
        initWidget();
    }

    @Override
    protected void initWidget() {
        mRecordLv = (ListView) findViewById(R.id.records);

        recordDetailDOs = new ArrayList<>();
        mAccountRecordAdapter = new AccountRecordAdapter(this, recordDetailDOs);



        View head = View.inflate(this, R.layout.account_head, null);

        inMoneyTv = (TextView) head.findViewById(R.id.in_money_tv);
        outMoneyTv = (TextView) head.findViewById(R.id.out_money_tv);
        dateTv = (TextView) head.findViewById(R.id.date_tv);

        mRecordLv.addHeaderView(head);

        mRecordLv.setAdapter(mAccountRecordAdapter);

        AccountDetailDO accountDetailDO = accountManager.getAccountByID(mCurAccountId);

        ((TextView) head.findViewById(R.id.account_name)).setText(accountDetailDO.getAccountName());
        ((ImageView) head.findViewById(R.id.account_type_icon)).setImageResource(IconTypeUtil.getAccountIcon(accountDetailDO.getAccountTypeID()));

        refresh(changeMonth);
    }

    private void refresh(int delMon) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, delMon);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long start = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        long end = calendar.getTimeInMillis();
        calendar.add(Calendar.DATE, -1);
        if (DateTimeUtil.isThisYear(calendar.getTime())){
            dateTv.setText(format1.format(new Date(start)) + "~" + format2.format(calendar.getTime()));
        }else {
            dateTv.setText(format3.format(new Date(start)) + "~" + format2.format(calendar.getTime()));
        }
        recordManager.getRecordsByMonthAndAccount(mCurAccountId, start, end, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Map<String, Object> map = (Map<String, Object>) msg.obj;
                recordDetailDOs.clear();
                recordDetailDOs.addAll((List<RecordDetailDO>) map.get("recordDetailDos"));
                mAccountRecordAdapter.notifyDataSetChanged();
                inMoneyTv.setText(String.format(getResources().getString(R.string.inMoney), (double) map.get("inCount")));
                outMoneyTv.setText(String.format(getResources().getString(R.string.outMoney), (double) map.get("outCount")));
            }
        });
    }

    public void changeDate(View view) {
        String tag = (String) view.getTag();
        if ("add".equals(tag)) {
            changeMonth++;
        } else {
            changeMonth--;
        }
        refresh(changeMonth);

    }


    private long mCurAccountId;
    private ListView mRecordLv;
    private AccountRecordAdapter mAccountRecordAdapter;
    private List<RecordDetailDO> recordDetailDOs;
    private RecordManager recordManager;
    private AccountManager accountManager;
    private TextView inMoneyTv, outMoneyTv, dateTv;

    DateFormat format1 = new SimpleDateFormat("MM月dd日");
    DateFormat format2 = new SimpleDateFormat("dd日");
    DateFormat format3 = new SimpleDateFormat("yyyy年MM月dd日");

    int changeMonth = 0;

}
