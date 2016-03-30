package com.suda.jzapp.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.AccountType;
import com.suda.jzapp.util.IconTypeUtil;

import java.util.List;

/**
 * Created by ghbha on 2016/2/16.
 */
public class AccountTypeAdapter extends BaseAdapter {
    private Activity context;
    private LayoutInflater mInflater;
    private List<AccountType> accountTypeList;

    public AccountTypeAdapter(List<AccountType> accountTypeList, Activity context) {
        this.accountTypeList = accountTypeList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return accountTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return accountTypeList.get(position);
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
            convertView = mInflater.inflate(R.layout.account_type_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.account_type_icon);
            holder.title = (TextView) convertView.findViewById(R.id.account_type_desc);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AccountType accountType = accountTypeList.get(position);

        holder.icon.setBackgroundResource(IconTypeUtil.getAccountIcon(accountType.getAccountIcon()));
        holder.title.setText(accountType.getAccountDesc());

        return convertView;
    }

    public class ViewHolder {
        public TextView title;
        public ImageView icon;
    }
}
