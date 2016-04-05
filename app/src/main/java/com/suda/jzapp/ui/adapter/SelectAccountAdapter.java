package com.suda.jzapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suda.jzapp.R;
import com.suda.jzapp.dao.bean.AccountDetailDO;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.List;

/**
 * Created by ghbha on 2016/2/28.
 */
public class SelectAccountAdapter extends BaseAdapter {


    private Context context;
    private List<AccountDetailDO> accounts;
    private LayoutInflater mInflater;
    int colorID;
    private long accountID;

    public SelectAccountAdapter(Context context, List<AccountDetailDO> accounts, long accountID) {
        this.accounts = accounts;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        colorID = ThemeUtil.getTheme(context).getMainColorID();
        this.accountID = accountID;
    }


    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public AccountDetailDO getItem(int position) {
        return accounts.get(position);
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
            convertView = mInflater.inflate(R.layout.account_item_2, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.account_type_icon);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.money = (TextView) convertView.findViewById(R.id.yu_e);
            holder.selectMark = (TextView) convertView.findViewById(R.id.select_mark);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AccountDetailDO accountDetailDO = accounts.get(position);

        holder.selectMark.setVisibility(accountID == accountDetailDO.getAccountID() ? View.VISIBLE : View.GONE);

        holder.icon.setImageResource(IconTypeUtil.getAccountIcon(accountDetailDO.getAccountTypeID()));
        holder.name.setText(accountDetailDO.getAccountName());
        holder.money.setText(String.format(context.getResources().getString(R.string.money_format_2), accountDetailDO.getAccountMoney()));


        holder.name.setTextColor(context.getResources().getColor(colorID));
        // holder.money.setTextColor(context.getResources().getColor(colorID));
        holder.name.setTextColor(context.getResources().getColor(colorID));
        holder.selectMark.setTextColor(context.getResources().getColor(colorID));
        return convertView;
    }


    public class ViewHolder {
        public TextView name;
        public TextView money;
        public TextView selectMark;
        public ImageView icon;
    }
}
