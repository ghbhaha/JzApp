package com.suda.jzapp.ui.activity.record;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.LineChartDo;
import com.suda.jzapp.ui.adapter.RecordLineAnalysisAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RecordLineChartActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_line_chart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recordManager = new RecordManager(this);
        lineChartDos = new ArrayList<>();
        initWidget();
    }

    @Override
    protected void initWidget() {
        listView = (ListView) findViewById(R.id.records);
        View head = View.inflate(this, R.layout.item_line_record, null);
        listView.addHeaderView(head);
        mLineChart = (LineChart) head.findViewById(R.id.type_line_chart);
        mYearTv = (TextView) head.findViewById(R.id.date_tv);
        recordLineAnalysisAdapter = new RecordLineAnalysisAdapter(this, lineChartDos);
        listView.setAdapter(recordLineAnalysisAdapter);
        initLineChart();
    }


    private void initLineChart() {
        mLineChart.setDescription("");
        mLineChart.setDragEnabled(false);// 是否可以拖拽
        mLineChart.setScaleEnabled(false);// 是否可以缩放
        mLineChart.setTouchEnabled(false);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setDrawBorders(false);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getAxisRight().setEnabled(false);
        for (int j = 0; j < 12; j++) {
            xLineVals1.add(j + 1 + "月");
        }
        refreshLineChart(0);
    }

    private void refreshLineChart(int delYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, delYear);
        lineChartDos.clear();
        mYearTv.setText(calendar.get(Calendar.YEAR) + "年");
        final LineData data = new LineData(xLineVals1);
        recordManager.getYearRecordDetail(calendar.get(Calendar.YEAR), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                List<LineChartDo> list = (List<LineChartDo>) msg.obj;
                //添加支出
                data.addDataSet(getLineDateSet(list, "支出", getResources().getColor(R.color.accent_red), 0));
                //添加收入
                data.addDataSet(getLineDateSet(list, "收入", getResources().getColor(R.color.accent_green), 1));
                //添加结余
                data.addDataSet(getLineDateSet(list, "结余", getResources().getColor(R.color.accent_blue), 2));
                mLineChart.setData(data);
                mLineChart.animateXY(500, 500);
                lineChartDos.addAll(list);
                recordLineAnalysisAdapter.notifyDataSetChanged();
            }
        });

    }

    private LineDataSet getLineDateSet(List<LineChartDo> list, String label, int color, int type) {
        int j = 0;
        List<Entry> yLineVals = new ArrayList<Entry>();
        for (LineChartDo lineChartDo : list) {
            if (type == 0) {
                yLineVals.add(new Entry(new Double(Math.abs(lineChartDo.getAllOut())).floatValue(), j));
            } else if (type == 1) {
                yLineVals.add(new Entry(new Double(lineChartDo.getAllIn()).floatValue(), j));
            } else if (type == 2) {
                yLineVals.add(new Entry(new Double(lineChartDo.getAllLeft()).floatValue(), j));
            }
            j++;
        }
        LineDataSet dataSet = new LineDataSet(yLineVals, label);
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setCircleColorHole(color);
        dataSet.setLineWidth(2f);
        dataSet.setDrawValues(false);
        return dataSet;
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

        refreshLineChart(changeMonth);
    }


    private RecordLineAnalysisAdapter recordLineAnalysisAdapter;
    private List<LineChartDo> lineChartDos;
    private ListView listView;
    private RecordManager recordManager;
    private LineChart mLineChart;
    List<String> xLineVals1 = new ArrayList<String>();
    private int changeMonth = 0;
    private TextView mYearTv;
}
