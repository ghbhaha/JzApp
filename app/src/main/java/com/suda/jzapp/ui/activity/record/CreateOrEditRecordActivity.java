package com.suda.jzapp.ui.activity.record;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.mrengineer13.snackbar.SnackBar;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.greendao.RecordType;
import com.suda.jzapp.dao.local.record.RecordTypeLocalDao;
import com.suda.jzapp.manager.AccountManager;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.adapter.NewRecordTypeAdapter;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.TextUtil;
import com.suda.jzapp.util.ThemeUtil;
import com.suda.jzapp.view.drag.DragGridView;

import java.util.ArrayList;
import java.util.Date;


public class CreateOrEditRecordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_record);

        recordManager = new RecordManager(this);
        accountManager = new AccountManager(this);

        initWidget();

        recordTypes = new ArrayList<>();
        setList();

        oldRecord = getIntent().getParcelableExtra(IntentConstant.OLD_RECORD);

        newRecord = new Record();

        //newRecord.setAccountID();

        if (oldRecord == null)
            setCurRecordType(0);
        else {
            newRecord.setId(oldRecord.getId());
            newRecord.setAccountID(oldRecord.getAccountID());
            newRecord.setRecordTypeID(oldRecord.getRecordTypeID());
            newRecord.setRecordMoney(oldRecord.getRecordMoney());
            newRecord.setRemark(oldRecord.getRemark());
            newRecord.setRecordDate(oldRecord.getRecordDate());
            mOldRecordType = recordManager.getRecordTypeByID(oldRecord.getId());
            setCurRecordType(0, mOldRecordType);
        }

        recordTypeAdapter = new NewRecordTypeAdapter(this, recordTypes);

        mRecordDr.setAdapter(recordTypeAdapter);

        mRecordDr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (recordTypeAdapter.ismShake()) {
                    recordTypeAdapter.setShake(false);
                    return;
                }

                if (position == recordTypes.size() - 1) {
                    Intent intent = new Intent(CreateOrEditRecordActivity.this, CreateNewRecordTypeActivity.class);
                    intent.putExtra(IntentConstant.RECORD_TYPE, recordTypes.get(0).getRecordType());
                    startActivityForResult(intent, REQUEST_CODE_ADD_NEW_RECORD_TYPE);
                    return;
                }
                setCurRecordType(position);
            }
        });

        mRecordDr.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (!recordTypeAdapter.ismShake()) {
                    recordTypeAdapter.setShake(true);
                }
                return false;
            }
        });

        mRecordDr.setOnMoveListener(new DragGridView.onMoveListener() {
            @Override
            public void onBottom() {
                if (showPanel)
                    return;
                YoYo.with(Techniques.SlideInUp).duration(500).playOn(panel);
                showPanel = true;
            }

            @Override
            public void onTop() {
                if (!showPanel)
                    return;
                YoYo.with(Techniques.SlideOutDown).duration(500).playOn(panel);
                showPanel = false;
            }

            @Override
            public void onRight() {

            }

            @Override
            public void onLeft() {

            }
        });
    }
    
    @Override
    protected void initWidget() {
        mRecordDr = (DragGridView) findViewById(R.id.record_item);
        panelBackView = findViewById(R.id.panel_color);
        panel = findViewById(R.id.panel);
        tvMoneyCount = (TextView) findViewById(R.id.money_count);
        tvTypeTitle = (TextView) findViewById(R.id.record_title);
        typeIcon = (ImageView) findViewById(R.id.record_icon);
        btZhiChu = (Button) findViewById(R.id.zhi_chu);
        btShouRu = (Button) findViewById(R.id.shou_ru);
        mAccountTv = (TextView) findViewById(R.id.account);
        mDateTv = (TextView) findViewById(R.id.date);
    }


    private void setCurRecordType(int index) {
        setCurRecordType(index, null);
    }

    private void setCurRecordType(int index, RecordType recordType) {
        if (recordType == null) {
            mCurRecordType = recordTypes.get(index);
        } else {
            mCurRecordType = recordType;
        }
        tvTypeTitle.setText(mCurRecordType.getRecordDesc());
        typeIcon.setImageResource(IconTypeUtil.getTypeIcon(mCurRecordType.getRecordIcon()));
    }

    private void setList() {
        int type = zhiChu ? Constant.RecordType.ZUICHU.getId() : Constant.RecordType.SHOURU.getId();
        recordTypes.addAll(recordManager.getRecordTypeByType(type));
        recordTypes.add(new RecordType());
    }


    @Override
    protected void onResume() {
        super.onResume();
        mainColor = this.getResources().getColor(ThemeUtil.getTheme(this).getMainColorID());
        mainDarkColor = this.getResources().getColor(ThemeUtil.getTheme(this).getMainDarkColorID());
        panelBackView.setBackgroundDrawable(new ColorDrawable(mainColor));
        tvTypeTitle.setTextColor(mainDarkColor);
        tvMoneyCount.setTextColor(mainDarkColor);
        setBtColor();

    }

    private void setBtColor() {

        if (zhiChu) {
            btZhiChu.setTextColor(mainDarkColor);
            btShouRu.setTextColor(Color.BLACK);
        } else {
            btZhiChu.setTextColor(Color.BLACK);
            btShouRu.setTextColor(mainDarkColor);
        }


    }

    public void switchZhiChuOrShouRu(View view) {
        zhiChu = !zhiChu;
        setBtColor();
        YoYo.with(Techniques.SlideOutLeft).duration(200).playOn(mRecordDr);
        recordTypes.clear();

        setList();
        YoYo.with(Techniques.SlideInRight).delay(200).duration(200).playOn(mRecordDr);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recordTypeAdapter.notifyDataSetChanged();
                setCurRecordType(0);
            }
        }, 200);

    }

    public void selectAccount(View view) {
        Intent intent = new Intent(this, SelectAccountActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ACCOUNT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_ACCOUNT:
                    newRecord.setAccountID(data.getLongExtra(IntentConstant.ACCOUNT_ID, 0l));
                    mAccountTv.setText(accountManager.getAccountByID(newRecord.getAccountID()).getAccountName());
                    break;
                case REQUEST_CODE_ADD_NEW_RECORD_TYPE:
                    recordTypes.clear();
                    setList();
                    recordTypeAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (recordTypeAdapter.ismShake()) {
                recordTypeAdapter.setShake(false);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void saveAndExit() {
        newRecord.setRecordMoney(Double.parseDouble(tvMoneyCount.getText().toString())
                * (mCurRecordType.getRecordType() / Math.abs(mCurRecordType.getRecordType())));
        newRecord.setRemark("");
        newRecord.setRecordTypeID(mCurRecordType.getRecordTypeID());
        if (oldRecord == null) {
            newRecord.setRecordDate(new Date(System.currentTimeMillis()));
            recordManager.createNewRecord(newRecord);
            accountManager.updateAccountMoney(newRecord.getAccountID(), newRecord.getRecordMoney(), null);
        } else {
            //判断账户是否发生变化
            if (oldRecord.getAccountID() == newRecord.getAccountID()) {
                //账户 -oldRecord.getRecordMoney() + newRecord.getRecordMoney();
                double addMoney = -oldRecord.getRecordMoney() + newRecord.getRecordMoney();
                accountManager.updateAccountMoney(newRecord.getAccountID(), addMoney, null);
            } else {
                //原账户 -oldRecord.getRecordMoney(), 新账户 + newRecord.getRecordMoney();
                accountManager.updateAccountMoney(newRecord.getAccountID(), -oldRecord.getRecordMoney(), null);
                accountManager.updateAccountMoney(newRecord.getAccountID(), newRecord.getRecordMoney(), null);
            }
        }
        finish();
    }

    ////////////////////////////计算panel////////////////////////////////
    public void onClickPanel(View view) {
        Button button = (Button) view;
        String tag = button.getText().toString();
        String money = tvMoneyCount.getText().toString();
        Character s1 = money.charAt(money.length() - 1);
        Character s2 = money.charAt(money.length() - 2);
        if ("C".equals(tag)) {
            opt = Opt.CLEAR;
            tvMoneyCount.setText("0.00");
            moneyCount = 0.00;
            tempCount = 0.00;
            isDO = false;
        } else if ("del".equals(tag)) {
            opt = Opt.DEL;
            if (!"0".equals(s1.toString())) {
                StringBuilder stringBuilder = new StringBuilder(money);
                stringBuilder.replace(money.length() - 1, money.length(), 0 + "");
                tvMoneyCount.setText(stringBuilder.toString());
                tempCount = Double.parseDouble(money);
                isDO = true;
                return;
            }
            if (!"0".equals(s2.toString())) {
                StringBuilder stringBuilder = new StringBuilder(money);
                stringBuilder.replace(money.length() - 2, money.length(), 0 + "");
                stringBuilder.append("0");
                tvMoneyCount.setText(stringBuilder.toString());
                tempCount = Double.parseDouble(money);
                return;
            }
            if (!"0".equals(money.substring(0, money.length() - 3))) {
                int tmp = Integer.parseInt(money.substring(0, money.length() - 3));
                tmp = tmp / 10;
                moneyCount = tmp;
                tempCount = Double.parseDouble(money);
                tvMoneyCount.setText(tmp + ".00");
            }


        } else if ("+".equals(tag)) {
            isDO = false;
            if (tempCount == 0 && Double.parseDouble(money) > 0) {
                opt = Opt.PLUS;
                return;
            }
            tempCount = 0;
            ((Button) findViewById(R.id.ok)).setText("=");

            if (moneyCount == 0) {
                moneyCount = Double.parseDouble(money);
                opt = Opt.PLUS;
                return;
            }

            if (opt == Opt.MINUS) {
                moneyCount = moneyCount - Double.parseDouble(money);
            } else {
                moneyCount = moneyCount + Double.parseDouble(money);
            }

            opt = Opt.PLUS;
            tvMoneyCount.setText(TextUtil.getFormatMoney(moneyCount));
        } else if ("-".equals(tag)) {
            isDO = false;
            if (tempCount == 0 && Double.parseDouble(money) > 0) {
                opt = Opt.MINUS;
                return;
            }

            tempCount = 0;

            ((Button) findViewById(R.id.ok)).setText("=");

            if (moneyCount == 0) {
                moneyCount = Double.parseDouble(money);
                opt = Opt.MINUS;
                return;
            }

            if (opt == Opt.PLUS) {
                moneyCount = moneyCount + Double.parseDouble(money);
            } else {
                moneyCount = moneyCount - Double.parseDouble(money);
            }

            opt = Opt.MINUS;
            tvMoneyCount.setText(TextUtil.getFormatMoney(moneyCount));
        } else if ("OK".equals(tag)) {
            isDO = false;
            opt = Opt.OK;
            tempCount = 0;
            if (Double.parseDouble(money) <= 0) {
                new SnackBar.Builder(this)
                        .withMessage("金额不能小于或等于0")
                        .withDuration(SnackBar.SHORT_SNACK)
                        .show();
                return;
            }

            saveAndExit();

        } else if ("=".equals(tag)) {
            isDO = false;
            if (opt == Opt.EQUAL)
                return;
            if (tempCount == 0) {
                opt = Opt.EQUAL;
                ((Button) findViewById(R.id.ok)).setText("OK");
                moneyCount = 0.00;
                tempCount = Double.parseDouble(money);
                return;
            }
            if (opt == Opt.MINUS) {
                moneyCount = moneyCount - Double.parseDouble(money);
            } else {
                moneyCount = moneyCount + Double.parseDouble(money);
            }
            tvMoneyCount.setText(TextUtil.getFormatMoney(moneyCount));
            opt = Opt.EQUAL;
            moneyCount = 0.00;
            tempCount = Double.parseDouble(money);
            ((Button) findViewById(R.id.ok)).setText("OK");
        } else {
            if (".".equals(tag)) {
                isDO = true;
                return;
            }

            if ((opt == Opt.PLUS || opt == Opt.MINUS || opt == Opt.EQUAL || opt == Opt.OK) && tempCount == 0) {
                money = "0.00";
            }

            if (isDO) {

                if (!"0".equals(s1.toString())) {
                    tempCount = Double.parseDouble(money);
                    return;
                } else {
                    if ("0".equals(s2.toString())) {
                        money = money.substring(0, money.length() - 2) + tag + "0";
                    } else {
                        money = money.substring(0, money.length() - 1) + tag;
                    }
                }
                tvMoneyCount.setText(money);
                tempCount = Double.parseDouble(money);
                return;
            }

            if ("0".equals(money.substring(0, money.length() - 3))) {
                money = tag + money.substring(money.length() - 3, money.length());
            } else {
                money = money.substring(0, money.length() - 3) + tag + money.substring(money.length() - 3, money.length());
            }
            tempCount = Double.parseDouble(money);
            tvMoneyCount.setText(money);
        }

    }

    private DragGridView mRecordDr;
    private View panelBackView, panel;

    NewRecordTypeAdapter recordTypeAdapter;
    ArrayList<RecordType> recordTypes;
    RecordManager recordManager;

    private int mainColor;
    private int mainDarkColor;

    private TextView tvMoneyCount, tvTypeTitle;
    private ImageView typeIcon;
    private Button btZhiChu, btShouRu;
    private Boolean zhiChu = true;

    private RecordType mCurRecordType, mOldRecordType;
    private double moneyCount = 0.00;
    private double tempCount = 0.00;
    private Opt opt = Opt.NULL;
    private boolean isDO = false;
    private boolean showPanel = true;
    private TextView mAccountTv, mDateTv;

    private Record newRecord, oldRecord;
    private AccountManager accountManager;

    public static final int REQUEST_CODE_ACCOUNT = 1;
    public static final int REQUEST_CODE_ADD_NEW_RECORD_TYPE = 2;

    private enum Opt {
        NULL, PLUS, MINUS, DEL, CLEAR, OK, EQUAL;
    }

}
