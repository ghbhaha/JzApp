package com.suda.jzapp.ui.activity.account;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.MonthReport;
import com.suda.jzapp.util.TextUtil;

public class MonthReportActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_report_acyivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recordManager = new RecordManager(this);
        initWidget();
    }

    @Override
    protected void initWidget() {
        mTvBudget = (TextView) findViewById(R.id.budget);
        mTvBudgetTip = (TextView) findViewById(R.id.budget_tip);
        mTvInMoney = (TextView) findViewById(R.id.in_money_tv);
        mTvInMoneyTip = (TextView) findViewById(R.id.in_money_tip);
        mTvOutMoney = (TextView) findViewById(R.id.out_money_tv);
        mTvOutMoneyTip = (TextView) findViewById(R.id.out_money_tip);


        recordManager.getThisMonthReport(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                MonthReport monthReport = (MonthReport) msg.obj;

                //目前还剩20\n看来得收紧口袋喽
                String budgetTip = "目前还剩" + (monthReport.getBudgetMoney() + monthReport.getOutMoney());
                if (Math.abs(monthReport.getOutMoney()) / monthReport.getBudgetMoney() > 0.8) {
                    budgetTip += "\n看来得勒紧裤腰带喽";
                    mTvBudget.setTextColor(Color.RED);
                    mTvBudgetTip.setTextColor(Color.RED);
                } else {
                    budgetTip += "\n预算还很充足嘛";
                }

                //其中'吃饭'消费最多，共消费40。\n看来你是一个吃货呀
                String outMoneyTip = "";
                if (!TextUtils.isEmpty(monthReport.getOutMaxType())) {
                    outMoneyTip = "其中'" + monthReport.getOutMaxType() + "'消费最多，共消费" + TextUtil.gwtFormatNum(monthReport.getOutMaxMoney());
                }

                //加油，骚年
                String inMoneyTip = "";
                if (monthReport.getBudgetMoney() > monthReport.getInMoney()) {
                    inMoneyTip = "革命尚未成功，同志仍需努力";
                } else {
                    inMoneyTip = "很不错嘛，继续加油";
                }

                mTvBudget.setText(TextUtil.gwtFormatNum(monthReport.getBudgetMoney()) + "");
                mTvInMoney.setText(TextUtil.gwtFormatNum(monthReport.getInMoney()) + "");
                mTvOutMoney.setText(TextUtil.gwtFormatNum(Math.abs(monthReport.getOutMoney())) + "");
                mTvBudgetTip.setText(budgetTip);
                mTvInMoneyTip.setText(inMoneyTip);
                mTvOutMoneyTip.setText(outMoneyTip);
            }
        });

    }


    private TextView mTvBudget, mTvBudgetTip;
    private TextView mTvInMoney, mTvInMoneyTip;
    private TextView mTvOutMoney, mTvOutMoneyTip;

    private RecordManager recordManager;

}
