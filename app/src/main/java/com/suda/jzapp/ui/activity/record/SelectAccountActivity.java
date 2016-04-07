package com.suda.jzapp.ui.activity.record;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.AccountManager;
import com.suda.jzapp.manager.domain.AccountDetailDO;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.adapter.SelectAccountAdapter;
import com.suda.jzapp.util.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghbha on 2016/2/28.
 */
public class SelectAccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);
        accountID = getIntent().getLongExtra(IntentConstant.ACCOUNT_ID,0l);
        initWidget();
        accountManager = new AccountManager(this);
        refreshData();
    }

    @Override
    protected void initWidget() {
        mRyAccount = (ListView) findViewById(R.id.account);
        ((TextView) findViewById(R.id.account_select)).setTextColor(textColor);


        mRyAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long selectAccountId = accounts.get(position).getAccountID();

                Intent intent = new Intent();
                intent.putExtra(IntentConstant.ACCOUNT_ID, selectAccountId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void refreshData() {
        accountManager.getAllAccount(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constant.MSG_SUCCESS) {
                    accounts.clear();
                    accounts.addAll((List<AccountDetailDO>) msg.obj);
                    if (mAccountAdapter == null) {
                        mAccountAdapter = new SelectAccountAdapter(SelectAccountActivity.this, accounts,accountID);
                        mRyAccount.setAdapter(mAccountAdapter);
                    } else {
                        mAccountAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        StatusBarCompat.compat(this, Color.TRANSPARENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    private SelectAccountAdapter mAccountAdapter;

    private ListView mRyAccount;

    private AccountManager accountManager;
    List<AccountDetailDO> accounts = new ArrayList<>();
    private long accountID;

}
