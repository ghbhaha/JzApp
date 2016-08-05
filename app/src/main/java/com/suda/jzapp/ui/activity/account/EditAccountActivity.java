package com.suda.jzapp.ui.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.AccountType;
import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.manager.AccountManager;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.adapter.AccountTypeAdapter;
import com.suda.jzapp.util.TextUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditAccountActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getAppStyleId(this));
        setMyContentView(R.layout.activity_edit_account_prop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        accountManager = new AccountManager(this);
        recordManager = new RecordManager(this);
        initParam();
        initWidget();
    }

    @Override
    protected void initWidget() {
        mEtProp = (EditText) findViewById(R.id.prop_et);
        mBtProp = (Button) findViewById(R.id.prop_bt);
        mBtProp.setTextColor(getColor(this, getMainTheme().getMainColorID()));
        mLvAccountType = (ListView) findViewById(R.id.account_type);

        findViewById(mEditType == PROP_TYPE_ACCOUNT_TYPE ? R.id.account_other_param : R.id.account_type_param).setVisibility(View.GONE);

        switch (mEditType) {
            case PROP_TYPE_ACCOUNT_NAME:
                getSupportActionBar().setTitle(getResources().getString(R.string.edit_account_name));
                mEtProp.setText(mParam);
                break;
            case PROP_TYPE_ACCOUNT_MONEY:
                getSupportActionBar().setTitle(getResources().getString(R.string.edit_account_money));
                mEtProp.setText(String.format(getResources().getString(R.string.record_money_format), mMoney));
                mEtProp.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;
            case PROP_TYPE_ACCOUNT_REMARK:
                getSupportActionBar().setTitle(getResources().getString(R.string.edit_account_remark));
                mEtProp.setText(mParam);
                break;
            case PROP_TYPE_ACCOUNT_TYPE:
                getSupportActionBar().setTitle(getResources().getString(R.string.edit_account_type));
                initListView();
                break;
            default:
                break;
        }

        mEtProp.setSelection(mEtProp.getText().toString().length());


        mBtProp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProp(mEtProp.getText().toString());
            }
        });

        mEtProp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEditType != PROP_TYPE_ACCOUNT_MONEY)
                    return;
                String tmp = s.toString();
                if (tmp.contains(".")) {
                    String[] strs = tmp.split("\\.");
                    if (strs.length > 1 && strs[1].length() > 2) {
                        mEtProp.setText(tmp.substring(0, tmp.length() - 1));
                        mEtProp.setSelection(tmp.length() - 1);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void initListView() {
        accountManager.getAllAccountType(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == Constant.MSG_ERROR)
                    return;

                accountTypes.addAll((List<AccountType>) msg.obj);
                mAccountTypeAdapter = new AccountTypeAdapter(accountTypes, EditAccountActivity.this);
                mLvAccountType.setAdapter(mAccountTypeAdapter);

            }
        });

        mLvAccountType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra(IntentConstant.EDIT_ACCOUNT_TYPE, accountTypes.get(position).getAccountTypeID());
                if (mAccountID > 0) {
                    accountManager.updateAccountTypeID(mAccountID, accountTypes.get(position).getAccountTypeID(), null);
                }
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void saveProp(String param) {

        final Intent intent = new Intent();

        //账户号>0 实时保存
        switch (mEditType) {
            case PROP_TYPE_ACCOUNT_NAME:
                intent.putExtra(IntentConstant.EDIT_ACCOUNT_NAME, param);
                if (mAccountID > 0) {
                    accountManager.updateAccountName(mAccountID, param, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
                //
                break;
            case PROP_TYPE_ACCOUNT_MONEY:
                final double money = Double.parseDouble(param);
                intent.putExtra(IntentConstant.EDIT_ACCOUNT_MONEY, money);
                if (mAccountID > 0 && money - mMoney != 0) {
                    accountManager.updateAccountMoney(mAccountID, money - mMoney, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            Record record = new Record();
                            record.setIsDel(false);
                            record.setRemark(money - mMoney > 0 ? "平账收入" : "平账支出");
                            record.setRecordId(System.currentTimeMillis());
                            record.setAccountID(mAccountID);
                            record.setRecordType(Constant.RecordType.CHANGE.getId());
                            record.setRecordTypeID(Constant.CHANGE_TYPE);
                            record.setRecordMoney(TextUtil.gwtFormatNum(money - mMoney));
                            record.setRecordDate(new Date(System.currentTimeMillis()));
                            recordManager.createNewRecord(record, null);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case PROP_TYPE_ACCOUNT_REMARK:
                intent.putExtra(IntentConstant.EDIT_ACCOUNT_REMARK, param);
                if (mAccountID > 0) {
                    accountManager.updateAccountRemark(mAccountID, param, new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            default:
                break;
        }


    }


    private void initParam() {
        mEditType = getIntent().getIntExtra(IntentConstant.EDIT_TYPE, -1);
        if (mEditType == PROP_TYPE_ACCOUNT_MONEY) {
            mMoney = getIntent().getDoubleExtra(IntentConstant.EDIT_ACCOUNT_MONEY, 0);
        } else if (mEditType == PROP_TYPE_ACCOUNT_NAME) {
            mParam = getIntent().getStringExtra(IntentConstant.EDIT_ACCOUNT_NAME);
        } else if (mEditType == PROP_TYPE_ACCOUNT_REMARK) {
            mParam = getIntent().getStringExtra(IntentConstant.EDIT_ACCOUNT_REMARK);
        }

        mAccountID = getIntent().getLongExtra(IntentConstant.ACCOUNT_ID, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private EditText mEtProp;
    private Button mBtProp;
    private ListView mLvAccountType;

    private String mParam;
    private double mMoney;
    private int mEditType = -1;
    private long mAccountID = 0;
    private AccountManager accountManager;
    private AccountTypeAdapter mAccountTypeAdapter;
    private RecordManager recordManager;
    private List<AccountType> accountTypes = new ArrayList<>();

    ///////////////////////////////////////////////////////////
    public static final int PROP_TYPE_ACCOUNT_NAME = 0;
    public static final int PROP_TYPE_ACCOUNT_MONEY = 1;
    public static final int PROP_TYPE_ACCOUNT_REMARK = 2;
    public static final int PROP_TYPE_ACCOUNT_TYPE = 3;
    public static final int PROP_TYPE_ACCOUNT_COLOR = 4;

}
