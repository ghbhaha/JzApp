package com.suda.jzapp.ui.activity.record;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.ChartRecordDo;
import com.suda.jzapp.ui.adapter.RecordPieAnalysisAdapter;
import com.suda.jzapp.util.DateTimeUtil;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.TextUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecordPieAnalysisActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyContentView(R.layout.activity_record_pie_analysis_acticity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecordManager = new RecordManager(this);
        initWidget();
    }


    @Override
    protected void initWidget() {
        mRecordLv = (ListView) findViewById(R.id.records);
        chartRecordDoList = new ArrayList<>();
        recordPieAnalysisAdapter = new RecordPieAnalysisAdapter(this, chartRecordDoList);
        View head = View.inflate(this, R.layout.item_pie_record, null);
        mTypePieChart = (PieChart) head.findViewById(R.id.type_pie_chart);
        changeTv = (TextView) head.findViewById(R.id.changeTv);
        dateTv = (TextView) head.findViewById(R.id.date_tv);
        initPieChart();
        mRecordLv.addHeaderView(head);
        mRecordLv.setAdapter(recordPieAnalysisAdapter);

    }

    private void initPieChart() {


        mTypePieChart.setExtraOffsets(5, 10, 5, 5);

        mTypePieChart.setDrawSlicesUnderHole(false);
        mTypePieChart.setDrawSliceText(false);

        //mTypePieChart.setTransparentCircleColor(Color.WHITE);
        // mTypePieChart.setTransparentCircleAlpha(110);

        mTypePieChart.setDescription("");

        // mTypePieChart.setHoleRadius(58f);
        //mTypePieChart.setTransparentCircleRadius(61f);
        mTypePieChart.getLegend().setEnabled(false);

        mTypePieChart.setDrawCenterText(true);


        mTypePieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mTypePieChart.setRotationEnabled(true);
        mTypePieChart.setHighlightPerTapEnabled(true);

        mTypePieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                mTypePieChart.setCenterText(xVals1.get(e.getXIndex()) + "\n" + yVals1.get(e.getXIndex()).getVal());
            }

            @Override
            public void onNothingSelected() {
                mTypePieChart.setCenterText((pieOut ? "总支出\n" : "总收入\n") + TextUtil.getFormatMoney(allOutOrInMoney));
            }
        });


        refreshPie(changeMonth);
        changeTv.setText(Html.fromHtml(pieOut ? getString(R.string.out_text) : getString(R.string.in_text)));
        changeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieOut = !pieOut;

                changeTv.setText(Html.fromHtml(pieOut ? getString(R.string.out_text) : getString(R.string.in_text)));

                refreshPie(changeMonth);
            }
        });


    }

    private void refreshPie(int delMon) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, delMon);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        long start = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        if (DateTimeUtil.isThisYear(calendar.getTime())) {
            dateTv.setText(format1.format(new Date(start)) + "~" + format2.format(calendar.getTime()));
        } else {
            dateTv.setText(format3.format(new Date(start)) + "~" + format2.format(calendar.getTime()));
        }

        mRecordManager.getOutOrInRecordByMonth(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                chartRecordDoList.clear();
                chartRecordDoList.addAll((List<ChartRecordDo>) msg.obj);
                recordPieAnalysisAdapter.notifyDataSetChanged();
                mTypePieChart.clear();
                xVals1.clear();
                yVals1.clear();
                List<ChartRecordDo> list = (List<ChartRecordDo>) msg.obj;
                ArrayList<Integer> colors = new ArrayList<Integer>();
                //mTypePieChart.setVisibility(list.size() > 0 ? View.VISIBLE : View.GONE);
                allOutOrInMoney = 0;
                int i = 0;
                double min = Double.MAX_VALUE;
                for (ChartRecordDo chartRecordDo : list) {
                    if (Math.abs(TextUtil.gwtFormatNum(chartRecordDo.getRecordMoney())) < min)
                        min = Math.abs(TextUtil.gwtFormatNum(chartRecordDo.getRecordMoney()));
                    allOutOrInMoney += Math.abs(TextUtil.gwtFormatNum(chartRecordDo.getRecordMoney()));
                    yVals1.add(new Entry(Math.abs(new Double(TextUtil.gwtFormatNum(chartRecordDo.getRecordMoney())).floatValue()), i));
                    xVals1.add(chartRecordDo.getRecordDesc());
                    colors.add(IconTypeUtil.getTypeIconOrColor(chartRecordDo.getIconId(), false));
                    i++;
                }
                PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
                if (min / allOutOrInMoney > 0.001) {
                    dataSet.setSliceSpace(1f);
                }
                dataSet.setSelectionShift(2f);

                dataSet.setColors(colors);
                PieData data = new PieData(xVals1, dataSet);
                data.setDrawValues(false);
                //data.setValueTypeface(tf);
                mTypePieChart.setData(data);
                if (list.size() == 0) {
                    mTypePieChart.setCenterText("还没有" + (pieOut ? "支出" : "收入") + "记录哦~");
                    mTypePieChart.setCenterTextColor(Color.RED);
                } else {
                    mTypePieChart.setCenterTextColor(mainColor);
                    mTypePieChart.setCenterText((pieOut ? "总支出\n" : "总收入\n") + TextUtil.getFormatMoney(allOutOrInMoney));
                }

                mTypePieChart.animateXY(500, 500);  //设置动画

            }
        }, pieOut, year, month);
    }

    public void changeDate(View view) {
        String tag = (String) view.getTag();
        if ("add".equals(tag)) {
            changeMonth++;
        } else {
            changeMonth--;
        }
        if (changeMonth > 0) {
            changeMonth--;
            return;
        }
        refreshPie(changeMonth);
    }


    private PieChart mTypePieChart;

    private ListView mRecordLv;
    private List<ChartRecordDo> chartRecordDoList;
    private RecordPieAnalysisAdapter recordPieAnalysisAdapter;
    private RecordManager mRecordManager;
    private boolean pieOut = true;
    private double allOutOrInMoney = 0;
    private TextView changeTv;
    List<Entry> yVals1 = new ArrayList<Entry>();
    List<String> xVals1 = new ArrayList<String>();
    int changeMonth = 0;
    private TextView dateTv;

    DateFormat format1 = new SimpleDateFormat("MM月dd日");
    DateFormat format2 = new SimpleDateFormat("dd日");
    DateFormat format3 = new SimpleDateFormat("yyyy年MM月dd日");

}
