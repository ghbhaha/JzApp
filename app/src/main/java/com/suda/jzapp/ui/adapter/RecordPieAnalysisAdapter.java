package com.suda.jzapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suda.jzapp.R;
import com.suda.jzapp.manager.domain.ChartRecordDo;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.TextUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.List;

/**
 * Created by ghbha on 2016/4/11.
 */
public class RecordPieAnalysisAdapter extends BaseAdapter {

    private Context mContext;
    private List<ChartRecordDo> chartRecordDos;
    private LayoutInflater mInflater;


    public RecordPieAnalysisAdapter(Context context, List<ChartRecordDo> list) {
        chartRecordDos = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return chartRecordDos.size();
    }

    @Override
    public ChartRecordDo getItem(int position) {
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
            convertView = mInflater.inflate(R.layout.item_record_type_status, null);
            holder.recordIcon = (ImageView) convertView.findViewById(R.id.record_icon);
            holder.recordDescTv = (TextView) convertView.findViewById(R.id.record_desc);
            holder.recordPerCentTv = (TextView) convertView.findViewById(R.id.record_per);
            holder.recordMoneyTv = (TextView) convertView.findViewById(R.id.record_money);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChartRecordDo chartRecordDo = chartRecordDos.get(position);

        holder.recordIcon.setImageResource(IconTypeUtil.getTypeIcon(chartRecordDo.getIconId()));
        holder.recordDescTv.setText(chartRecordDo.getRecordDesc());
        holder.recordPerCentTv.setText(TextUtil.getFormatMoney(chartRecordDo.getPer()) + "%");
        holder.recordMoneyTv.setText(TextUtil.getFormatMoney(chartRecordDo.getRecordMoney()) + "");
        holder.recordMoneyTv.setTextColor(mContext.getResources().getColor(ThemeUtil.getTheme(mContext).getMainColorID()));
        return convertView;
    }

    public class ViewHolder {
        public ImageView recordIcon;
        public TextView recordDescTv, recordMoneyTv, recordPerCentTv;
    }
}
