package com.suda.jzapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suda.jzapp.R;
import com.suda.jzapp.ui.activity.account.CreateOrEditAccountActivity;
import com.suda.jzapp.dao.bean.AccountDetailDO;
import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.List;

/**
 * Created by ghbha on 2016/2/16.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {


    private Context context;
    private List<AccountDetailDO> accounts;


    public AccountAdapter(Context context, List<AccountDetailDO> accounts) {
        this.accounts = accounts;
        this.context = context;
    }

    public void refreshData(List<Account> accounts) {
        accounts.clear();
        accounts.addAll(accounts);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.account_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AccountDetailDO account = accounts.get(position);
        holder.mIgAccountTypeIcon.setImageResource(IconTypeUtil.getAccountIcon(account.getAccountTypeID()));

        holder.mTvAccountName.setText(account.getAccountName());
        holder.mTvAccountMoney.setText(String.format(context.getResources().getString(R.string.money_format), account.getAccountMoney()));

            holder.mTvAccountTypeTitle.setText(account.getAccountDesc());

            holder.mTvTodayCost.setText(String.format(context.getResources().getString(R.string.money_format), account.getTodayCost()));

            holder.mVEditAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CreateOrEditAccountActivity.class);
                    intent.putExtra(IntentConstant.ACCOUNT_ID, account.getAccountID());
                    context.startActivity(intent);
                }
            });

    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvAccountTypeTitle;
        private TextView mTvAccountName;
        private TextView mTvAccountMoney;
        private TextView mTvTodayCost;
        private ImageView mIgAccountTypeIcon;
        private View mVEditAccount;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvAccountName = (TextView) itemView.findViewById(R.id.name);
            mTvAccountMoney = (TextView) itemView.findViewById(R.id.yu_e);
            mTvTodayCost = (TextView) itemView.findViewById(R.id.xiao_fei);
            mVEditAccount = itemView.findViewById(R.id.edit_account);
            mTvAccountTypeTitle = (TextView) itemView.findViewById(R.id.account_type_title);
            mIgAccountTypeIcon = (ImageView) itemView.findViewById(R.id.account_type_icon);
        }

    }
}
