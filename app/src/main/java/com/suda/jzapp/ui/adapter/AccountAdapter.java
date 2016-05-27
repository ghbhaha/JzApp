package com.suda.jzapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.manager.AccountManager;
import com.suda.jzapp.manager.domain.AccountDetailDO;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.activity.MainActivity;
import com.suda.jzapp.ui.activity.account.AccountTransactionActivity;
import com.suda.jzapp.ui.activity.account.AccountsTransferActivity;
import com.suda.jzapp.ui.activity.account.CreateOrEditAccountActivity;
import com.suda.jzapp.ui.adapter.helper.ItemTouchHelperAdapter;
import com.suda.jzapp.ui.adapter.helper.OnStartDragListener;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.util.SnackBarUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by ghbha on 2016/2/16.
 */
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> implements ItemTouchHelperAdapter {


    private Context context;
    private List<AccountDetailDO> accounts;
    private OnStartDragListener mDragStartListener;
    private AccountManager accountManager;

    public AccountAdapter(Context context, List<AccountDetailDO> accounts, OnStartDragListener dragStartListener) {
        this.accounts = accounts;
        this.context = context;
        this.mDragStartListener = dragStartListener;
        accountManager = new AccountManager(context);
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


        holder.mIgAccountTypeIcon.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });

        final AccountDetailDO account = accounts.get(position);
        holder.mIgAccountTypeIcon.setImageResource(IconTypeUtil.getAccountIcon(account.getAccountTypeID()));

        if (!TextUtils.isEmpty(account.getAccountColor())) {
            holder.mIgAccountTypeIcon.setColorFilter(Integer.parseInt(account.getAccountColor()));
        } else {
            holder.mIgAccountTypeIcon.setColorFilter(0);
        }

        holder.mTvAccountName.setText(account.getAccountName());
        holder.mTvAccountMoney.setText(String.format(context.getResources().getString(R.string.money_format), account.getAccountMoney()));

        holder.mTvAccountTypeTitle.setText(account.getAccountDesc());

        holder.mTvTodayCost.setText(String.format(context.getResources().getString(R.string.money_format), account.getTodayCost()));

        holder.mVEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateOrEditAccountActivity.class);
                intent.putExtra(IntentConstant.ACCOUNT_ID, account.getAccountID());
                intent.putExtra(IntentConstant.ACCOUNT_COUNT, accounts.size());
                ((MainActivity) context).startActivityForResult(intent, MainActivity.REQUEST_ACCOUNT);
            }
        });

        holder.mVAccountFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AccountTransactionActivity.class);
                intent.putExtra(IntentConstant.ACCOUNT_ID, account.getAccountID());
                ((MainActivity) context).startActivityForResult(intent, MainActivity.REQUEST_ACCOUNT_FLOW);
            }
        });
        holder.mVAccountTransForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accounts.size() == 1) {

                    SnackBarUtil.showSnackInfo(v, context, "当前只有一个账户，无法转账");

                    return;
                }

                Intent intent = new Intent(context, AccountsTransferActivity.class);
                intent.putExtra(IntentConstant.ACCOUNT_ID, account.getAccountID());
                ((MainActivity) context).startActivityForResult(intent, MainActivity.REQUEST_ACCOUNT_TRANSFORM);
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
        private View mVAccountFlow;
        private View mVAccountTransForm;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvAccountName = (TextView) itemView.findViewById(R.id.name);
            mTvAccountMoney = (TextView) itemView.findViewById(R.id.yu_e);
            mTvTodayCost = (TextView) itemView.findViewById(R.id.xiao_fei);
            mVEditAccount = itemView.findViewById(R.id.edit_account);
            mTvAccountTypeTitle = (TextView) itemView.findViewById(R.id.account_type_title);
            mIgAccountTypeIcon = (ImageView) itemView.findViewById(R.id.account_type_icon);
            mVAccountFlow = itemView.findViewById(R.id.account_flow);
            mVAccountTransForm = itemView.findViewById(R.id.transform);
        }

    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(accounts, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        accountManager.updateAccountIndex(null, accounts);
        return false;
    }

    @Override
    public void onItemDismiss(int position) {

    }

}
