package com.suda.jzapp.dao.cloud.avos;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.suda.jzapp.dao.cloud.avos.pojo.AVAccount;
import com.suda.jzapp.dao.cloud.avos.pojo.MyAVUser;
import com.suda.jzapp.dao.greendao.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Suda on 2015/10/9.
 */
public class AccountBus {

    public static void syncAccountInCloud(final Context context, final List<Account> mList) {
        final List<Long> accountIdList = new ArrayList<>();
        final Map<Long, AVAccount> accountMap = new HashMap<>();

        AVQuery<AVAccount> query = new AVQuery<AVAccount>();
        query.whereEqualTo(AVAccount.USER_ID, MyAVUser.getCurrentUserId());
        query.findInBackground(new FindCallback<AVAccount>() {
            @Override
            public void done(List<AVAccount> list, AVException e) {

                for (AVAccount avAccount : list) {
                    accountIdList.add(avAccount.getAccountId());
                    accountMap.put(avAccount.getAccountId(), avAccount);
                }

                for (Account account : mList) {
                    if (!accountIdList.contains(account.getAccountID())) {
                        createAccountInCloud(account, context);
                    } else if (accountIdList.contains(account.getAccountID()) && !account.getSyncStatus()) {
                        updateAccountInCloud(accountMap.get(account.getAccountID()), account, context);
                    }
                }

            }
        });
    }

    public static void getAllAccountFromCloud(final Context context) {
        AVQuery<AVAccount> query = new AVQuery<AVAccount>();
        query.whereEqualTo(AVAccount.USER_ID, MyAVUser.getCurrentUserId());
        query.findInBackground(new FindCallback<AVAccount>() {
            @Override
            public void done(List<AVAccount> list, AVException e) {
                for (AVAccount avAccount : list) {
                    Account account = avAccount2Account(avAccount);
                    account.setSyncStatus(true);
                    // MyDbOpenHelper.getDaoSession(context).getAccountDao().insert(account);
                }

            }
        });
    }

    private static Account avAccount2Account(AVAccount avAccount) {
        Account account = new Account();
        account.setAccountID(avAccount.getAccountId());
        account.setAccountRemark(avAccount.getAccountRemark());
        account.setAccountColor(avAccount.getAccountColor());
        account.setAccountMoney(avAccount.getAccountMoney());
        account.setAccountName(avAccount.getAccountName());
        account.setAccountTypeID(avAccount.getAccountTypeId());
        return account;
    }

    private static void updateAccountInCloud(AVAccount avAccount, final Account account, final Context context) {
        avAccount.setAccountColor(account.getAccountColor());
        avAccount.setAccountMoney(account.getAccountMoney());
        avAccount.setAccountName(account.getAccountName());
        avAccount.setAccountRemark(account.getAccountRemark());
        avAccount.setAccountTypeId(account.getAccountTypeID());
        avAccount.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                account.setSyncStatus(true);
                //  MyDbOpenHelper.getDaoSession(context).getAccountDao().update(account);
            }
        });
    }

    private static void createAccountInCloud(final Account account, final Context context) {
        AVAccount avAccount = new AVAccount();
        avAccount.setAccountId(account.getAccountID());
        avAccount.setUserId(MyAVUser.getCurrentUserId());
        avAccount.setAccountColor(account.getAccountColor());
        avAccount.setAccountMoney(account.getAccountMoney());
        avAccount.setAccountName(account.getAccountName());
        avAccount.setAccountRemark(account.getAccountRemark());
        avAccount.setAccountTypeId(account.getAccountTypeID());
        avAccount.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                account.setSyncStatus(true);
                // MyDbOpenHelper.getDaoSession(context).getAccountDao().update(account);
            }
        });
    }

}
