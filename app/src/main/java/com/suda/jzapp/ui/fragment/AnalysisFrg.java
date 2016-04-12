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

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.suda.jzapp.R;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.ChartRecordDo;
import com.suda.jzapp.ui.activity.MainActivity;
import com.suda.jzapp.ui.activity.record.RecordPieAnalysisActivity;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.TextUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.ArrayList;
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
        pieLabelTv = (TextView) view.findViewById(R.id.pie_label);
        mRecordManager = new RecordManager(getActivity());
        ((MainActivity) getActivity()).setReloadAnalysisCallBack(this);
        changeTv = (TextView) view.findViewById(R.id.changeTv);
        initPieChart();
        return view;
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

    }

    private void refreshPie() {
        mRecordManager.getOutOrInRecordThisMonth(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                mTypePieChart.clear();
                xVals1.clear();
                yVals1.clear();

                ArrayList<Integer> colors = new ArrayList<Integer>();
                List<ChartRecordDo> list = (List<ChartRecordDo>) msg.obj;
                allOutOrInMoney = 0;
                int i = 0;
                for (ChartRecordDo chartRecordDo : list) {
                    allOutOrInMoney += Math.abs(TextUtil.gwtFormatNum(chartRecordDo.getRecordMoney()));
                    yVals1.add(new Entry(Math.abs(new Double(TextUtil.gwtFormatNum(chartRecordDo.getRecordMoney())).floatValue()), i));
                    xVals1.add(chartRecordDo.getRecordDesc());
                    colors.add(IconTypeUtil.getTypeIconOrColor(chartRecordDo.getIconId(),false));
                    i++;
                }

                PieDataSet dataSet = new PieDataSet(yVals1, "Election Results");
                dataSet.setSliceSpace(2f);
                dataSet.setSelectionShift(4f);
                dataSet.setColors(colors);
                PieData data = new PieData(xVals1, dataSet);
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

    private RecordManager mRecordManager;

    boolean pieOut = true;
    private double allOutOrInMoney = 0;
    private TextView changeTv, pieLabelTv;
    List<Entry> yVals1 = new ArrayList<Entry>();
    List<String> xVals1 = new ArrayList<String>();


}
