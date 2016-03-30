package com.suda.jzapp.dao.local.account;

import android.content.Context;

import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.dao.greendao.AccountDao;
import com.suda.jzapp.dao.greendao.AccountType;
import com.suda.jzapp.dao.greendao.AccountTypeDao;
import com.suda.jzapp.dao.local.BaseLocalDao;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.TextUtil;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by ghbha on 2016/2/15.
 */
public class AccountLocalDao extends BaseLocalDao {

    public Account getAccountByID(long accountID, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        return getSingleData(accountDao.queryBuilder().
                where(AccountDao.Properties.AccountID.eq(accountID))
                .build()
                .list());
    }

    public void updateAccountName(long accountID, String accountName, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        Account account = getAccountByID(accountID, context);
        account.setAccountName(accountName);
        account.setSyncStatus(false);
        accountDao.update(account);
    }

    public void updateAccountMoney(long accountID, double money, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        Account account = getAccountByID(accountID, context);
        account.setAccountMoney(account.getAccountMoney() + money);
        account.setSyncStatus(false);
        accountDao.update(account);
    }

    public void updateAccountRemark(long accountID, String remark, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        Account account = getAccountByID(accountID, context);
        account.setAccountRemark(remark);
        account.setSyncStatus(false);
        accountDao.update(account);
    }

    public void updateAccountTypeID(long accountID, int typeID, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        Account account = getAccountByID(accountID, context);
        account.setAccountTypeID(typeID);
        account.setSyncStatus(false);
        accountDao.update(account);
    }

    public List<Account> getAllAccount(Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        return accountDao.queryBuilder().
                where(AccountDao.Properties.IsDel.eq(false))
                .orderDesc(AccountDao.Properties.AccountID)
                .build()
                .list();
    }

    public AccountType getAccountTypeByID(long id, Context context) {
        AccountTypeDao accountTypeDao = getDaoSession(context).getAccountTypeDao();
        return getSingleData(accountTypeDao.queryBuilder().
                where(AccountTypeDao.Properties.AccountTypeID.eq(id))
                .build()
                .list());
    }

    public void createNewAccount(Account account, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();

        account.setAccountMoney(Double.parseDouble(TextUtil.getFormatMoney((account.getAccountMoney()))));
        accountDao.insert(account);
    }

    public void deleteAccount(long accountID, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        Account account = getAccountByID(accountID, context);
        account.setIsDel(true);
        account.setSyncStatus(false);
        accountDao.update(account);
    }

    public List<AccountType> getAllAccountType(Context context) {
        AccountTypeDao accountTypeDao = getDaoSession(context).getAccountTypeDao();
        return accountTypeDao.queryBuilder().build().list();
    }

}
