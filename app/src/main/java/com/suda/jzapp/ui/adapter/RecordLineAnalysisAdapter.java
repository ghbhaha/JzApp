package com.suda.jzapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.suda.jzapp.R;
import com.suda.jzapp.manager.domain.LineChartDo;
import com.suda.jzapp.util.TextUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.List;

/**
 * Created by ghbha on 2016/4/12.
 */
public class RecordLineAnalysisAdapter extends BaseAdapter {

    private Context mContext;
    private List<LineChartDo> chartRecordDos;
    private LayoutInflater mInflater;

    private int themeColor = 0;

    public RecordLineAnalysisAdapter(Context context, List<LineChartDo> list) {
        chartRecordDos = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        themeColor = mContext.getResources().getColor(ThemeUtil.getTheme(context).getMainColorID());
    }


    @Override
    public int getCount() {
        return chartRecordDos.size();
    }

    @Override
    public LineChartDo getItem(int position) {
        return chartRecordDos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_record_year_status, null);

            holder.monthTv = (TextView) convertView.findViewById(R.id.monthTv);
            holder.inTv = (TextView) convertView.findViewById(R.id.inTv);
            holder.outTv = (TextView) convertView.findViewById(R.id.outTv);
            holder.leftTv = (TextView) convertView.findViewById(R.id.leftTv);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LineChartDo lineChartDo = chartRecordDos.get(chartRecordDos.size() - position - 1);

        int colorGrey = mContext.getResources().getColor(R.color.gray_white);
        holder.monthTv.setTextColor(themeColor);
        holder.inTv.setTextColor(lineChartDo.getAllIn() == 0 ? colorGrey : themeColor);
        holder.outTv.setTextColor(lineChartDo.getAllOut() == 0 ? colorGrey : themeColor);

        holder.monthTv.setText(lineChartDo.getMonth() + "月");
        holder.inTv.setText(TextUtil.getFormatMoney(lineChartDo.getAllIn()) + "");
        holder.outTv.setText(TextUtil.getFormatMoney(lineChartDo.getAllOut()) + "");
        holder.leftTv.setText(TextUtil.getFormatMoney(lineChartDo.getAllLeft()) + "");

        return convertView;
    }

    public class ViewHolder {
        public TextView monthTv, inTv, outTv, leftTv;
    }

}
