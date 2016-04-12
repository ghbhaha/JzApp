package com.suda.jzapp.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.ChartRecordDo;
import com.suda.jzapp.manager.domain.LineChartDo;
import com.suda.jzapp.ui.activity.MainActivity;
import com.suda.jzapp.ui.activity.record.RecordLineChartActivity;
import com.suda.jzapp.ui.activity.record.RecordPieAnalysisActivity;
import com.suda.jzapp.ui.adapter.RecordLineAnalysisAdapter;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.TextUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ghbha on 2016/2/15.
 */
public class AnalysisFrg extends Fragment implements MainActivity.ReloadCallBack {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.analysis_frg_layout, container, false);
        backGround = view.findViewById(R.id.background);
        mTypePieChart = (PieChart) view.findViewById(R.id.type_pie_chart);
        mLineChart = (LineChart) view.findViewById(R.id.line_chart);
        pieLabelTv = (TextView) view.findViewById(R.id.pie_label);
        lineLabelTv = (TextView) view.findViewById(R.id.line_label);
        mRecordManager = new RecordManager(getActivity());
        ((MainActivity) getActivity()).setReloadAnalysisCallBack(this);
        changeTv = (TextView) view.findViewById(R.id.changeTv);
        initPieChart();
        initLineChart();
        return view;
    }

    private void initLineChart() {
        mLineChart.setDescription("");
        mLineChart.setDragEnabled(true);// 是否可以拖拽
        mLineChart.setScaleEnabled(false);// 是否可以缩放
        mLineChart.setTouchEnabled(true);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setDrawBorders(false);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getAxisRight().setEnabled(false);


        for (int j = 0; j < 12; j++) {
            xLineVals1.add(j + 1 + "月");
        }

        refreshLineChart();
    }

    private void refreshLineChart() {
        final LineData data = new LineData(xLineVals1);
        mRecordManager.getYearRecordDetail(2016, new Handler() {
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
            }
        });
    }

    private LineDataSet getLineDateSet(List<LineChartDo> list, String label, int color, int type) {
        int j = 0;
        List<Entry> yLineVals = new ArrayList<Entry>();
        for (LineChartDo lineChartDo : list) {
            if (type == 0) {
                yLineVals.add(new Entry(new Double(lineChartDo.getAllOut()).floatValue(), j));
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
                mTypePieChart.setCenterText(xPieVals1.get(e.getXIndex()) + "\n" + yPieVals1.get(e.getXIndex()).getVal());
            }

            @Override
            public void onNothingSelected() {
                mTypePieChart.setCenterText((pieOut ? "总支出\n" : "总收入\n") + TextUtil.getFormatMoney(allOutOrInMoney));
            }
        });


        refreshPie();
        changeTv.setText(Html.fromHtml(pieOut ? getString(R.string.out_text) : getString(R.string.in_text)));
        changeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pieOut = !pieOut;

                changeTv.setText(Html.fromHtml(pieOut ? getString(R.string.out_text) : getString(R.string.in_text)));

                refreshPie();
            }
        });

        pieLabelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordPieAnalysisActivity.class);
                startActivity(intent);
            }
        });

        lineLabelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecordLineChartActivity.class);
                startActivity(intent);
            }
        });

    }

    private void refreshPie() {
        mRecordManager.getOutOrInRecordThisMonth(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mTypePieChart.clear();
                xPieVals1.clear();
                yPieVals1.clear();

                ArrayList<Integer> colors = new ArrayList<Integer>();
                List<ChartRecordDo> list = (List<ChartRecordDo>) msg.obj;
                allOutOrInMoney = 0;
                int i = 0;
                for (ChartRecordDo chartRecordDo : list) {
                    allOutOrInMoney += Math.abs(TextUtil.gwtFormatNum(chartRecordDo.getRecordMoney()));
                    yPieVals1.add(new Entry(Math.abs(new Double(TextUtil.gwtFormatNum(chartRecordDo.getRecordMoney())).floatValue()), i));
                    xPieVals1.add(chartRecordDo.getRecordDesc());
                    colors.add(IconTypeUtil.getTypeIconOrColor(chartRecordDo.getIconId(), false));
                    i++;
                }

                PieDataSet dataSet = new PieDataSet(yPieVals1, "Election Results");
                dataSet.setSliceSpace(2f);
                dataSet.setSelectionShift(4f);
                dataSet.setColors(colors);
                PieData data = new PieData(xPieVals1, dataSet);
                data.setDrawValues(false);
                //data.setValueTypeface(tf);
                mTypePieChart.setData(data);
                if (list.size() == 0) {
                    mTypePieChart.setCenterText("本月还没有" + (pieOut ? "支出" : "收入") + "记录哦~");
                    mTypePieChart.setCenterTextColor(Color.RED);
                } else {
                    mTypePieChart.setCenterTextColor(mainColor);
                    mTypePieChart.setCenterText((pieOut ? "总支出\n" : "总收入\n") + TextUtil.getFormatMoney(allOutOrInMoney));
                }

                mTypePieChart.animateXY(500, 500);  //设置动画

            }
        }, pieOut);
    }

    @Override
    public void reload(boolean needUpdateData) {
        refreshPie();
        refreshLineChart();
    }


    @Override
    public void onResume() {
        super.onResume();
        mainColor = getResources().getColor(ThemeUtil.getTheme(getActivity()).getMainColorID());
        mainDarkColor = getResources().getColor(ThemeUtil.getTheme(getActivity()).getMainDarkColorID());

        backGround.setBackground(new ColorDrawable(mainColor));
    }


    private View backGround;
    private int mainColor;
    private int mainDarkColor;
    private PieChart mTypePieChart;
    private LineChart mLineChart;

    private RecordManager mRecordManager;

    boolean pieOut = true;
    private double allOutOrInMoney = 0;
    private TextView changeTv, pieLabelTv,lineLabelTv;
    List<Entry> yPieVals1 = new ArrayList<Entry>();
    List<String> xPieVals1 = new ArrayList<String>();
    List<String> xLineVals1 = new ArrayList<String>();

}
