package com.suda.jzapp.ui.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.AccountManager;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.AccountDetailDO;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.KeyBoardUtils;
import com.suda.jzapp.util.SnackBarUtil;
import com.suda.jzapp.util.TextUtil;
import com.suda.jzapp.util.ThemeUtil;

public class AccountsTransferActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getAppStyleId(this));
        setMyContentView(R.layout.activity_accounts_transfer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        accountManager = new AccountManager(this);
        recordManager = new RecordManager(this);
        outAccountID = getIntent().getLongExtra(IntentConstant.ACCOUNT_ID, 0);
        inAccountID = 0;
        initWidget();

    }

    @Override
    protected void initWidget() {
        mImgOutAccountIcon = (ImageView) findViewById(R.id.out_account_icon);
        mImgInAccountIcon = (ImageView) findViewById(R.id.in_account_icon);
        mTvoOutAccountName = (TextView) findViewById(R.id.out_account_name);
        mTvInAccountName = (TextView) findViewById(R.id.in_account_name);
        mTvOutAccountLeftMoney = (TextView) findViewById(R.id.out_account_money);
        mTvInAccountLeftMoney = (TextView) findViewById(R.id.in_account_money);
        eEtOutMoney = (EditText) findViewById(R.id.out_money);
        eEtInMoney = (EditText) findViewById(R.id.in_money);

        plusView = findViewById(R.id.plus);
        plusView.setVisibility(View.GONE);
        eEtInMoney.setVisibility(View.GONE);

        inView = findViewById(R.id.in_layout);
        outView = findViewById(R.id.out_layout);
        circleProgressBar = (CircleProgressBar) findViewById(R.id.progressBar);
        circleProgressBar.setVisibility(View.INVISIBLE);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.submit);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        eEtOutMoney.setText("0.00");
        eEtInMoney.setText("0.00");
        eEtOutMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        eEtInMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        AccountDetailDO accountDetailDO = accountManager.getAccountByID(outAccountID);
        mImgOutAccountIcon.setImageResource(IconTypeUtil.getAccountIcon(accountDetailDO.getAccountIcon()));
        if (!TextUtils.isEmpty(accountDetailDO.getAccountColor())) {
            mImgOutAccountIcon.setColorFilter(Integer.parseInt(accountDetailDO.getAccountColor()));
        }

        mTvoOutAccountName.setText(accountDetailDO.getAccountName());
        mTvOutAccountLeftMoney.setText(TextUtil.getFormatMoney(accountDetailDO.getAccountMoney()) + "");

        mImgInAccountIcon.setImageResource(0);
        mTvInAccountName.setText("");
        mTvInAccountLeftMoney.setText("");

        outView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(eEtOutMoney, AccountsTransferActivity.this);
                KeyBoardUtils.closeKeybord(eEtInMoney, AccountsTransferActivity.this);
                selectItem = 0;
                Intent intent = new Intent(AccountsTransferActivity.this, SelectAccountActivity.class);
                if (outAccountID > 0)
                    intent.putExtra(IntentConstant.ACCOUNT_ID, outAccountID);
                startActivityForResult(intent, 0);
                overridePendingTransition(R.anim.up_in,0);
            }
        });

        inView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(eEtOutMoney, AccountsTransferActivity.this);
                KeyBoardUtils.closeKeybord(eEtInMoney, AccountsTransferActivity.this);
                selectItem = 1;
                Intent intent = new Intent(AccountsTransferActivity.this, SelectAccountActivity.class);
                if (inAccountID > 0)
                    intent.putExtra(IntentConstant.ACCOUNT_ID, inAccountID);
                startActivityForResult(intent, 0);
                overridePendingTransition(R.anim.up_in,0);
            }
        });

        eEtOutMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tmp = s.toString();
                if (tmp.contains(".")) {
                    String[] strs = tmp.split("\\.");
                    if (strs.length > 1 && strs[1].length() > 2) {
                        eEtOutMoney.setText(tmp.substring(0, tmp.length() - 1));
                        eEtOutMoney.setSelection(tmp.length() - 1);
                    }
                }

                if (!TextUtils.isEmpty(tmp)) {
                    double money = Double.parseDouble(tmp);
                    if (money > Double.parseDouble(mTvOutAccountLeftMoney.getText().toString())) {
                        KeyBoardUtils.closeKeybord(eEtOutMoney, AccountsTransferActivity.this);
                        SnackBarUtil.showSnackInfo(floatingActionButton, AccountsTransferActivity.this, getResources().getString(R.string.not_have_enough_money));
                        eEtOutMoney.setText(mTvOutAccountLeftMoney.getText());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!eEtOutMoney.isFocused())
                    return;
                eEtInMoney.setText(eEtOutMoney.getText());
            }
        });

        eEtInMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String tmp = s.toString();
                if (tmp.contains(".")) {
                    String[] strs = tmp.split("\\.");
                    if (strs.length > 1 && strs[1].length() > 2) {
                        eEtInMoney.setText(tmp.substring(0, tmp.length() - 1));
                        eEtInMoney.setSelection(tmp.length() - 1);
                    }
                }
                if (!TextUtils.isEmpty(tmp)) {
                    double money = Double.parseDouble(tmp);
                    if (money > Double.parseDouble(mTvOutAccountLeftMoney.getText().toString())) {
                        KeyBoardUtils.closeKeybord(eEtInMoney, AccountsTransferActivity.this);
                        SnackBarUtil.showSnackInfo(floatingActionButton, AccountsTransferActivity.this, getResources().getString(R.string.not_have_enough_money));
                        eEtInMoney.setText(mTvOutAccountLeftMoney.getText());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (eEtOutMoney.isFocused())
                    return;
                eEtOutMoney.setText(eEtInMoney.getText());
            }
        });
    }

    private void submit() {
        if (inAccountID == 0) {
            SnackBarUtil.showSnackInfo(floatingActionButton, this, getResources().getString(R.string.select_in_account));
            return;
        }

        if (TextUtils.isEmpty(eEtInMoney.getText())) {
            SnackBarUtil.showSnackInfo(floatingActionButton, this, getResources().getString(R.string.enter_account_trans_money));
            return;
        }
        if (Double.parseDouble(eEtInMoney.getText().toString()) == 0) {
            SnackBarUtil.showSnackInfo(floatingActionButton, this, getResources().getString(R.string.enter_account_trans_money));
            return;
        }
        circleProgressBar.setVisibility(View.VISIBLE);
        circleProgressBar.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light);
        recordManager.moneyTransFer(outAccountID, inAccountID, TextUtil.gwtFormatNum(Double.parseDouble(eEtInMoney.getText().toString())), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                setResult(RESULT_OK);
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            long accountId = data.getLongExtra(IntentConstant.ACCOUNT_ID, 0);
            AccountDetailDO accountDetailDO = accountManager.getAccountByID(accountId);
            if (selectItem == 0) {
                if (inAccountID == accountId) {
                    SnackBarUtil.showSnackInfo(floatingActionButton, this, getResources().getString(R.string.not_select_same_account));
                    return;
                }
                outAccountID = accountId;
                mImgOutAccountIcon.setImageResource(IconTypeUtil.getAccountIcon(accountDetailDO.getAccountIcon()));
                if (!TextUtils.isEmpty(accountDetailDO.getAccountColor())) {
                    mImgOutAccountIcon.setColorFilter(Integer.parseInt(accountDetailDO.getAccountColor()));
                } else {
                    mImgOutAccountIcon.setColorFilter(0);
                }

                mTvoOutAccountName.setText(accountDetailDO.getAccountName());
                mTvOutAccountLeftMoney.setText(TextUtil.getFormatMoney(accountDetailDO.getAccountMoney()) + "");
            } else {
                if (outAccountID == accountId) {
                    SnackBarUtil.showSnackInfo(floatingActionButton, this, getResources().getString(R.string.not_select_same_account));
                    return;
                }
                inAccountID = accountId;
                plusView.setVisibility(View.VISIBLE);
                eEtInMoney.setVisibility(View.VISIBLE);

                mImgInAccountIcon.setImageResource(IconTypeUtil.getAccountIcon(accountDetailDO.getAccountIcon()));
                if (!TextUtils.isEmpty(accountDetailDO.getAccountColor())) {
                    mImgInAccountIcon.setColorFilter(Integer.parseInt(accountDetailDO.getAccountColor()));
                } else {
                    mImgInAccountIcon.setColorFilter(0);
                }

                mTvInAccountName.setText(accountDetailDO.getAccountName());
                mTvInAccountLeftMoney.setText(TextUtil.getFormatMoney(accountDetailDO.getAccountMoney()) + "");
            }
        }

    }

    private ImageView mImgOutAccountIcon, mImgInAccountIcon;
    private TextView mTvoOutAccountName, mTvInAccountName, mTvOutAccountLeftMoney, mTvInAccountLeftMoney;
    private EditText eEtOutMoney, eEtInMoney;
    private View inView, outView, plusView;
    private FloatingActionButton floatingActionButton;
    private CircleProgressBar circleProgressBar;

    private long outAccountID, inAccountID;

    private AccountManager accountManager;
    private RecordManager recordManager;

    private int selectItem = 0;

    boolean editOut = true;
}
