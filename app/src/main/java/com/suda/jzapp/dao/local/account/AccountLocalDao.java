package com.suda.jzapp.dao.local.account;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.dao.greendao.AccountDao;
import com.suda.jzapp.dao.greendao.AccountType;
import com.suda.jzapp.dao.greendao.AccountTypeDao;
import com.suda.jzapp.dao.greendao.Budget;
import com.suda.jzapp.dao.greendao.BudgetDao;
import com.suda.jzapp.dao.local.BaseLocalDao;
import com.suda.jzapp.manager.domain.AccountDetailDO;
import com.suda.jzapp.manager.domain.AccountIndexDO;
import com.suda.jzapp.util.MoneyUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ghbha on 2016/2/15.
 */
public class AccountLocalDao extends BaseLocalDao {


    public void createOrUpdateAccount(Context context, Account account) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        Account accountOld = getSingleData(accountDao.queryBuilder().whereOr(AccountDao.Properties.AccountID.eq(account.getAccountID())
                , AccountDao.Properties.ObjectID.eq(account.getObjectID())).build().list());
        if (accountOld != null) {
            account.setId(accountOld.getId());
            updateAccount(context, account);
        } else {
            createNewAccount(account, context);
        }
    }

    public double getBudget(Context context) {
        BudgetDao budgetDao = getDaoSession(context).getBudgetDao();
        if (getSingleData(budgetDao.queryBuilder().list()) == null) {
            initBudget(context);
        }
        return getSingleData(budgetDao.queryBuilder().list()).getBudgetMoney();
    }

    public void updateBudget(Context context, double money) {
        BudgetDao budgetDao = getDaoSession(context).getBudgetDao();
        Budget budget = getSingleData(budgetDao.queryBuilder().list());
        if (budget != null) {
            budget.setBudgetMoney(money);
            budgetDao.update(budget);
        }
    }

    public void initBudget(Context context) {
        BudgetDao budgetDao = getDaoSession(context).getBudgetDao();
        budgetDao.deleteAll();
        Budget budget = new Budget();
        budget.setBudgetMoney(3000.00);
        Date date = new Date();
        budget.setUpdatedAt(date);
        budget.setCreatedAt(date);
        budgetDao.insert(budget);
    }

    public double getAllMoney(Context context) {
        List<Account> accounts = getAllAccount(context);
        double money = 0;
        for (Account account : accounts) {
            money += account.getAccountMoney();
        }
        return money;
    }

    public Account getSuitAccount(Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        return getSingleData(accountDao.queryBuilder().
                where(AccountDao.Properties.IsDel.eq(false))
                .orderAsc(AccountDao.Properties.Index)
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

    public void updateAccountColor(long accountID, String color, boolean isSync, String objId, Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        Account account = getAccountByID(accountID, context);
        account.setAccountColor(color);
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

    public void updateAccount(Context context, Account account) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        accountDao.update(account);
    }

    public List<Account> getAllAccount(Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        return accountDao.queryBuilder().
                where(AccountDao.Properties.IsDel.eq(false))
                .orderAsc(AccountDao.Properties.Index)
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
        account.setAccountMoney(MoneyUtil.getFormatNum((account.getAccountMoney())));
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

    public List<Account> getNotBackData(Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        return accountDao.queryBuilder().where(AccountDao.Properties.SyncStatus.eq(false))
                .list();
    }

    public void updateAccountIndex(Context context, List<AccountDetailDO> accountDetailDOs) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        int i = 0;
        for (AccountDetailDO accountDetailDO : accountDetailDOs) {
            Account account = getSingleData(accountDao.queryBuilder().where(AccountDao.Properties.AccountID.eq(accountDetailDO.getAccountID())).list());
            if (account == null)
                continue;
            account.setIndex(i);
            accountDao.update(account);
            i++;
        }
    }

    public void updateAccountIndexByAccountIndex(Context context, List<AccountIndexDO> accountIndexDOs) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        for (AccountIndexDO accountIndexDO : accountIndexDOs) {
            Account account = getSingleData(accountDao.queryBuilder().where(AccountDao.Properties.AccountID.eq(accountIndexDO.getAccountID())).list());
            if (account == null)
                continue;
            account.setIndex(accountIndexDO.getIndex());
            accountDao.update(account);
        }
    }

    public String getAccountIndexInfo(Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        List<Account> list = accountDao.queryBuilder().
                where(AccountDao.Properties.IsDel.eq(false))
                .orderAsc(AccountDao.Properties.Index)
                .build()
                .list();
        List<AccountIndexDO> list1 = new ArrayList<>();
        int i = 0;
        for (Account account : list) {
            AccountIndexDO accountIndexDO = new AccountIndexDO();
            accountIndexDO.setAccountID(account.getAccountID());
            accountIndexDO.setIndex(i);
            i++;
            list1.add(accountIndexDO);
        }

        return JSON.toJSONString(list1);
    }

    public int getAccountSize(Context context) {
        AccountDao accountDao = getDaoSession(context).getAccountDao();
        return accountDao.queryBuilder().where(AccountDao.Properties.IsDel.eq(false)).list().size();
    }

}
