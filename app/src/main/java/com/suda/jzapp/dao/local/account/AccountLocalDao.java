package com.suda.jzapp.dao.local.account;

import android.content.Context;
import android.text.TextUtils;

import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.dao.greendao.AccountDao;
import com.suda.jzapp.dao.greendao.AccountType;
import com.suda.jzapp.dao.greendao.AccountTypeDao;
import com.suda.jzapp.dao.local.BaseLocalDao;
import com.suda.jzapp.util.TextUtil;

import java.util.List;

/**
 * Created by ghbha on 2016/2/15.
 */
public class AccountLocalDao extends BaseLocalDao {

    public Account getSuitAccount(Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        return getSingleData(accountDao.queryBuilder().
                orderDesc(AccountDao.Properties.AccountMoney)
                .build()
                .list());
    }

    public void clearAllAccount(Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        accountDao.deleteAll();
    }

    public Account getAccountByID(long accountID, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        return getSingleData(accountDao.queryBuilder().
                where(AccountDao.Properties.AccountID.eq(accountID))
                .build()
                .list());
    }

    public void updateAccountName(long accountID, String accountName, boolean isSync, String objId, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        Account account = getAccountByID(accountID, context);
        account.setAccountName(accountName);
        account.setSyncStatus(isSync);
        if (!TextUtils.isEmpty(objId)) {
            account.setObjectID(objId);
        }
        accountDao.update(account);
    }

    public void updateAccountMoney(long accountID, double money, boolean isSync, String objId, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        Account account = getAccountByID(accountID, context);
        account.setAccountMoney(account.getAccountMoney() + money);
        account.setSyncStatus(isSync);
        if (!TextUtils.isEmpty(objId)) {
            account.setObjectID(objId);
        }
        accountDao.update(account);
    }

    public void updateAccountRemark(long accountID, String remark, boolean isSync, String objId, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        Account account = getAccountByID(accountID, context);
        account.setAccountRemark(remark);
        account.setSyncStatus(isSync);
        if (!TextUtils.isEmpty(objId)) {
            account.setObjectID(objId);
        }
        accountDao.update(account);
    }

    public void updateAccountTypeID(long accountID, int typeID, boolean isSync, String objId, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        Account account = getAccountByID(accountID, context);
        account.setAccountTypeID(typeID);
        account.setSyncStatus(isSync);
        if (!TextUtils.isEmpty(objId)) {
            account.setObjectID(objId);
        }
        accountDao.update(account);
    }

    public void updateAccount(Context context,Account account){
        AccountDao accountDao = getDaoSession(context).getAccountDao();
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

    public void deleteAccount(long accountID, boolean isSync, String objId, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        Account account = getAccountByID(accountID, context);
        account.setIsDel(true);
        account.setSyncStatus(isSync);
        if (!TextUtils.isEmpty(objId)) {
            account.setObjectID(objId);
        }
        accountDao.update(account);
    }

    public List<AccountType> getAllAccountType(Context context) {
        AccountTypeDao accountTypeDao = getDaoSession(context).getAccountTypeDao();
        return accountTypeDao.queryBuilder().build().list();
    }

    public List<Account> getNotSyncData(Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        return accountDao.queryBuilder().where(AccountDao.Properties.SyncStatus.eq(false))
                .list();
    }

}
