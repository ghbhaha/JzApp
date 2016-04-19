package com.suda.jzapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.local.record.RecordLocalDAO;
import com.suda.jzapp.manager.domain.RecordDetailDO;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.activity.account.AccountTransactionActivity;
import com.suda.jzapp.ui.activity.record.CreateOrEditRecordActivity;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by ghbha on 2016/4/9.
 */
public class AccountRecordAdapter extends BaseAdapter {

    private Context mContext;
    private List<RecordDetailDO> recordDetailDOs;
    private LayoutInflater mInflater;
    private RecordLocalDAO recordLocalDAO;

    public AccountRecordAdapter(Context context, List<RecordDetailDO> list) {
        recordDetailDOs = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        recordLocalDAO = new RecordLocalDAO();
    }

    @Override
    public int getCount() {
        return recordDetailDOs.size();
    }

    @Override
    public RecordDetailDO getItem(int position) {
        return recordDetailDOs.get(position);
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
            convertView = mInflater.inflate(R.layout.item_account_record, null);
            holder.recordDateTv = (TextView) convertView.findViewById(R.id.record_date);
            holder.recordDescTv = (TextView) convertView.findViewById(R.id.record_desc);
            holder.recordRemarkTv = (TextView) convertView.findViewById(R.id.record_remark);
            holder.recordMoneyTv = (TextView) convertView.findViewById(R.id.record_money);
            holder.recordIcon = (ImageView) convertView.findViewById(R.id.record_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
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

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //关联账户
                if (recordDetailDO.getRecordID() == -100)
                    return;

                Intent intent = new Intent(mContext, CreateOrEditRecordActivity.class);
                Record record = recordLocalDAO.getRecordById(mContext, recordDetailDO.getRecordID());
                if (record.getRecordType() == Constant.RecordType.SHOURU.getId() ||
                        record.getRecordType() == Constant.RecordType.ZUICHU.getId() ||
                        record.getRecordType() == Constant.RecordType.AA_ZHICHU.getId() ||
                        record.getRecordType() == Constant.RecordType.AA_SHOURU.getId()) {
                    intent.putExtra(IntentConstant.OLD_RECORD, record);
                    ((AccountTransactionActivity) mContext).startActivityForResult(intent, AccountTransactionActivity.REQUEST_CODE_EDIT_RECORD);
                }

            }
        });

        return convertView;
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

    public class ViewHolder {
        public ImageView recordIcon;
        public TextView recordDescTv, recordRemarkTv, recordMoneyTv, recordDateTv;
    }
}
