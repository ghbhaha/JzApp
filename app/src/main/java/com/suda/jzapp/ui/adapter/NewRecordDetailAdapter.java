package com.suda.jzapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suda.jzapp.R;
import com.suda.jzapp.dao.local.record.RecordLocalDAO;
import com.suda.jzapp.manager.domain.RecordDetailDO;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by suda on 2016/8/4.
 */
public class NewRecordDetailAdapter extends RecyclerView.Adapter<NewRecordDetailAdapter.ViewHolder> {

    private Context mContext;
    private List<RecordDetailDO> recordDetailDOs;

    private RecordLocalDAO recordLocalDAO;

    public NewRecordDetailAdapter(Context context, List<RecordDetailDO> list) {
        recordDetailDOs = list;
        mContext = context;
        recordLocalDAO = new RecordLocalDAO();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_account_record, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //设置主题
        int color = mContext.getResources().getColor(ThemeUtil.getTheme(mContext).getMainColorID());
        holder.recordDescTv.setTextColor(color);
        holder.recordMoneyTv.setTextColor(color);

        final RecordDetailDO recordDetailDO = recordDetailDOs.get(position);

        holder.recordRemarkTv.setVisibility(TextUtils.isEmpty(recordDetailDO.getRemark()) ? View.GONE : View.VISIBLE);
        holder.recordDescTv.setText(recordDetailDO.getRecordDesc());
        holder.recordRemarkTv.setText(recordDetailDO.getRemark());
        holder.recordMoneyTv.setText(
                (recordDetailDO.getRecordMoney() > 0 ? "+" : "") +
                        String.format(mContext.getResources().getString(R.string.record_money_format), recordDetailDO.getRecordMoney()));
        holder.recordIcon.setImageResource(IconTypeUtil.getTypeIcon(recordDetailDO.getIconId()));

        boolean isFirst = false;
        if (getPositionForSection(recordDetailDO.getRecordDate().getTime()) == position) {
            isFirst = true;
        }
        holder.recordDateTv.setVisibility(isFirst ? View.VISIBLE : View.GONE);
        DateFormat format = new SimpleDateFormat("MM月dd日");
        holder.recordDateTv.setText(format.format(recordDetailDO.getRecordDate()));
    }


    private int getPositionForSection(long sectionIndex) {
        if (recordDetailDOs != null && recordDetailDOs.size() > 0) {
            for (int i = 0; i < recordDetailDOs.size(); i++) {
                if (recordDetailDOs.get(i).getRecordDate().getTime() == sectionIndex)
                    return i;
            }
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return recordDetailDOs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView recordIcon;
        public TextView recordDescTv, recordRemarkTv, recordMoneyTv, recordDateTv;

        public ViewHolder(View itemView) {
            super(itemView);
            recordDateTv = (TextView) itemView.findViewById(R.id.record_date);
            recordDescTv = (TextView) itemView.findViewById(R.id.record_desc);
            recordRemarkTv = (TextView) itemView.findViewById(R.id.record_remark);
            recordMoneyTv = (TextView) itemView.findViewById(R.id.record_money);
            recordIcon = (ImageView) itemView.findViewById(R.id.record_icon);
        }

    }


}
