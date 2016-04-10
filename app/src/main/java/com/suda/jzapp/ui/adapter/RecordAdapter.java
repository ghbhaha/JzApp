package com.suda.jzapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.local.record.RecordLocalDAO;
import com.suda.jzapp.manager.AccountManager;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.RecordDetailDO;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.activity.MainActivity;
import com.suda.jzapp.ui.activity.record.CreateOrEditRecordActivity;
import com.suda.jzapp.util.DateTimeUtil;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.ThemeUtil;
import com.suda.jzapp.view.MyRoundColorView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by ghbha on 2016/4/5.
 */
public class RecordAdapter extends BaseAdapter {

    private Context mContext;
    private List<RecordDetailDO> recordDetailDOs;
    private LayoutInflater mInflater;
    private List<View> optViews;
    private int lastSelOpt = -1;
    private RecordLocalDAO recordLocalDAO;
    private RecordManager recordManager;
    private AccountManager accountManager;
    private Map<Date, RecordDetailDO> recordDetailDOMap;

    public RecordAdapter(Context context, List<RecordDetailDO> list) {
        recordDetailDOs = list;
        mContext = context;
        mInflater = LayoutInflater.from(context);
        optViews = new ArrayList<>();
        recordLocalDAO = new RecordLocalDAO();
        recordManager = new RecordManager(context);
        accountManager = new AccountManager(context);
        recordDetailDOMap = new HashMap<>();

    }

    @Override
    public int getCount() {
        return recordDetailDOs.size();
    }

    @Override
    public Object getItem(int position) {
        return recordDetailDOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.record_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.record_icon);
            holder.inLy = convertView.findViewById(R.id.shouru_ly);
            holder.outLY = convertView.findViewById(R.id.zhichu_ly);
            holder.outTv = (TextView) convertView.findViewById(R.id.out_tv);
            holder.inTv = (TextView) convertView.findViewById(R.id.in_tv);
            holder.outRemarkTv = (TextView) convertView.findViewById(R.id.out_remark_tv);
            holder.inRemarkTv = (TextView) convertView.findViewById(R.id.in_remark_tv);
            holder.recordDateTv = (TextView) convertView.findViewById(R.id.record_date);
            holder.delV = convertView.findViewById(R.id.icon_del);
            holder.editV = convertView.findViewById(R.id.icon_edit);
            holder.lineV = convertView.findViewById(R.id.record_line);
            holder.myRoundColorView = (MyRoundColorView) convertView.findViewById(R.id.myRound);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final RecordDetailDO recordDetailDO = recordDetailDOs.get(position);

        if (recordDetailDO.isDayFirstDay()) {
            recordDetailDOMap.put(recordDetailDO.getRecordDate(), recordDetailDO);
        }

        //设置颜色
        int color = mContext.getResources().getColor(ThemeUtil.getTheme(mContext).getMainColorID());
        holder.lineV.setBackgroundColor(color);
        holder.myRoundColorView.setMyRoundColor(color);
        holder.inTv.setTextColor(color);
        holder.outTv.setTextColor(color);
        holder.inRemarkTv.setTextColor(color);
        holder.outRemarkTv.setTextColor(color);

        holder.inLy.setVisibility(recordDetailDO.getRecordMoney() > 0 || (recordDetailDO.isDayFirstDay() && recordDetailDO.getTodayAllInMoney() > 0) || (recordDetailDO.isMonthFirstDay())
                ? View.VISIBLE : View.INVISIBLE);
        holder.outLY.setVisibility(recordDetailDO.getRecordMoney() < 0 || (recordDetailDO.isDayFirstDay() && recordDetailDO.getTodayAllOutMoney() < 0)
                ? View.VISIBLE : View.INVISIBLE);

        holder.icon.setVisibility(recordDetailDO.isDayFirstDay() || recordDetailDO.isMonthFirstDay() ? View.INVISIBLE : View.VISIBLE);
        holder.recordDateTv.setVisibility(recordDetailDO.isDayFirstDay() || recordDetailDO.isMonthFirstDay() ? View.VISIBLE : View.INVISIBLE);

        resetOptBt();

        if (recordDetailDO.getRecordMoney() < 0) {
            holder.outTv.setText(recordDetailDO.getRecordDesc() + " " +
                    String.format(mContext.getResources().getString(R.string.record_money_format), Math.abs(recordDetailDO.getRecordMoney())));
            holder.outRemarkTv.setText(recordDetailDO.getRemark());
        }

        if (recordDetailDO.getRecordMoney() > 0) {
            holder.inTv.setText(recordDetailDO.getRecordDesc() + " " +
                    String.format(mContext.getResources().getString(R.string.record_money_format), Math.abs(recordDetailDO.getRecordMoney())));
            holder.inRemarkTv.setText("");
        }

        if (recordDetailDO.isMonthFirstDay()) {
            DateFormat format = new SimpleDateFormat("yyyy年");
            holder.inTv.setText(DateTimeUtil.isThisYear(recordDetailDO.getRecordDate()) ? "" : format.format(recordDetailDO.getRecordDate()));
        } else {
            if (recordDetailDO.getTodayAllInMoney() >= 0 && recordDetailDO.isDayFirstDay()) {
                holder.inTv.setText(String.format(mContext.getResources().getString(R.string.record_money_format), Math.abs(recordDetailDO.getTodayAllInMoney()))
                        + " 收入");
                holder.outRemarkTv.setText("");
            }
        }

        if (recordDetailDO.getTodayAllOutMoney() <= 0 && recordDetailDO.isDayFirstDay()) {
            holder.outTv.setText("支出 " + String.format(mContext.getResources().getString(R.string.record_money_format), Math.abs(recordDetailDO.getTodayAllOutMoney())));
            holder.inRemarkTv.setText("");
        }

        if (recordDetailDO.isMonthFirstDay()) {
            DateFormat format = new SimpleDateFormat("MM月");
            holder.recordDateTv.setText(format.format(recordDetailDO.getRecordDate()));
        } else {
            if (DateTimeUtil.isToday(recordDetailDO.getRecordDate())) {
                holder.recordDateTv.setText("今日");
            } else {
                DateFormat format = new SimpleDateFormat("dd日");
                holder.recordDateTv.setText(format.format(recordDetailDO.getRecordDate()));
            }
        }

        holder.icon.setImageResource(IconTypeUtil.getTypeIcon(recordDetailDO.getIconId()));

        final ViewHolder finalHolder = holder;

        if (!recordDetailDO.isDayFirstDay() && !recordDetailDO.isMonthFirstDay()) {
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetOptBt();
                    if (lastSelOpt != position) {
                        optViews.add(finalHolder.delV);
                        optViews.add(finalHolder.editV);
                        finalHolder.delV.setVisibility(View.VISIBLE);
                        finalHolder.editV.setVisibility(View.VISIBLE);
                        YoYo.with(Techniques.SlideInUp).duration(200).playOn(finalHolder.delV);
                        YoYo.with(Techniques.SlideInDown).duration(200).playOn(finalHolder.editV);
                        lastSelOpt = position;
                    } else {
                        lastSelOpt = -1;
                    }
                }
            });
        }

        holder.delV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog materialDialog = new MaterialDialog(mContext);
                materialDialog.setTitle("删除记录？")
                        .setMessage("")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Record record = recordLocalDAO.getRecordById(mContext, recordDetailDO.getRecordID());
                                record.setIsDel(true);
                                recordManager.updateOldRecord(record, null);
                                accountManager.updateAccountMoney(record.getAccountID(), -record.getRecordMoney(), new
                                        Handler() {
                                            @Override
                                            public void handleMessage(Message msg) {
                                                super.handleMessage(msg);
                                                ((MainActivity) mContext).getReloadAccountCallBack().reload(true);
                                            }
                                        });
                                RecordDetailDO todayRecordDetailDO = recordDetailDOMap.get(recordDetailDO.getRecordDate());
                                if (recordDetailDO.getRecordMoney() < 0) {
                                    todayRecordDetailDO.setTodayAllOutMoney(todayRecordDetailDO.getTodayAllOutMoney() - recordDetailDO.getRecordMoney());
                                } else {
                                    todayRecordDetailDO.setTodayAllInMoney(todayRecordDetailDO.getTodayAllInMoney() - recordDetailDO.getRecordMoney());
                                }

                                if (recordDetailDOs.get(position - 1).isDayFirstDay() &&
                                        (position == recordDetailDOs.size() - 1 || recordDetailDOs.get(position + 1).isDayFirstDay() || recordDetailDOs.get(position + 1).isMonthFirstDay())) {
                                    if (recordDetailDOs.get(position - 2).isMonthFirstDay()
                                            && (position == recordDetailDOs.size() - 1 || recordDetailDOs.get(position + 1).isMonthFirstDay())) {
                                        //本月最后一条数据
                                        recordDetailDOs.remove(position - 2);
                                        recordDetailDOs.remove(position - 2);
                                        recordDetailDOs.remove(position - 2);
                                    } else {
                                        //今日最后一条数据
                                        recordDetailDOs.remove(position - 1);
                                        recordDetailDOs.remove(position - 1);
                                    }
                                } else {
                                    recordDetailDOs.remove(position);
                                }

                                notifyDataSetChanged();
                                materialDialog.dismiss();

                            }
                        })
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                materialDialog.dismiss();
                            }
                        }).show();
            }
        });

        holder.editV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CreateOrEditRecordActivity.class);
                intent.putExtra(IntentConstant.OLD_RECORD_DETAIL, recordDetailDO);
                resetOptBt();
                lastSelOpt = -1;
                ((MainActivity) mContext).startActivityForResult(intent, MainActivity.REQUEST_RECORD);
            }
        });

        return convertView;
    }

    private void resetOptBt() {
        for (View view : optViews) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    public class ViewHolder {
        public View outLY, inLy;
        public TextView outTv, inTv, outRemarkTv, inRemarkTv, recordDateTv;
        public ImageView icon;
        public View delV, editV;
        public View lineV;
        public MyRoundColorView myRoundColorView;
    }
}
