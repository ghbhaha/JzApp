package com.suda.jzapp.manager;

import android.content.Context;
import android.os.Handler;

import com.suda.jzapp.dao.bean.AccountDetailDO;
import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.dao.greendao.AccountType;
import com.suda.jzapp.dao.local.account.AccountLocalDao;
import com.suda.jzapp.dao.local.record.RecordLocalDAO;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.DataConvertUtil;
import com.suda.jzapp.util.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suda on 2015/11/16.
 */
public class AccountManager extends BaseManager {
    public AccountManager(Context context) {
        super(context);
    }

    /**
     * 获取多有账户
     *
     * @param userName
     * @param handler
     */
    public void getAllAccount(String userName, Handler handler) {
        //  List<Account> accounts =

    }

    public AccountDetailDO getAccountByID(final long accountID) {
        Account account = accountLocalDao.getAccountByID(accountID, _context);
        AccountType accountType = accountLocalDao.getAccountTypeByID(account.getAccountTypeID(), _context);
        AccountDetailDO accountDetailDO = DataConvertUtil.getAccountDetailDO(account, accountType);
        return accountDetailDO;
    }

    public void getAccountTypeByID(final long accountTypeId, final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                AccountType accountType = accountLocalDao.getAccountTypeByID(accountTypeId, _context);
                sendMessage(handler, accountType, accountType != null);
            }
        });

    }

    public void getAllAccount(final Handler handler) {

        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                List<Account> accounts = accountLocalDao.getAllAccount(_context);
                List<AccountDetailDO> accountDetailDOs = new ArrayList<AccountDetailDO>();
                for (Account account : accounts) {
                    AccountType accountType = accountLocalDao.getAccountTypeByID(account.getAccountTypeID(), _context);
                    AccountDetailDO accountDetailDO = DataConvertUtil.getAccountDetailDO(account, accountType);
                    accountDetailDO.setTodayCost(recordLocalDAO.countTodayCostByAccountId(_context,account.getAccountID()));
                    accountDetailDOs.add(accountDetailDO);
                }
                sendMessage(handler, accountDetailDOs);
            }
        });
    }

    public void getAllAccountType(final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                List<AccountType> accountTypes = accountLocalDao.getAllAccountType(_context);
                sendMessage(handler, accountTypes);
            }
        });
    }

    public void createNewAccount(String accountName, double accountMoney, int accountTypeID, String accountRemark, final Handler handler) {
        final Account account = new Account();
        account.setAccountID(System.currentTimeMillis());
        account.setAccountColor("");
        account.setAccountName(accountName);
        account.setAccountMoney(accountMoney);
        account.setAccountTypeID(accountTypeID);
        account.setAccountRemark(accountRemark);
        account.setSyncStatus(false);
        account.setIsDel(false);
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                accountLocalDao.createNewAccount(account, _context);
                if (handler != null) {
                    handler.sendEmptyMessage(Constant.MSG_SUCCESS);
                }
            }
        });
    }

    public void deleteAccountByID(final long accountID, final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                accountLocalDao.deleteAccount(accountID, _context);

                if (handler != null)
                    handler.sendEmptyMessage(Constant.MSG_SUCCESS);

            }
        });
    }

    public void updateAccountName(final long accountID, final String accountName, final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                accountLocalDao.updateAccountName(accountID, accountName, _context);

                if (handler != null)
                    handler.sendEmptyMessage(Constant.MSG_SUCCESS);

            }
        });
    }


    public void updateAccountTypeID(final long accountID, final int typeID, final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                accountLocalDao.updateAccountTypeID(accountID, typeID, _context);

                if (handler != null)
                    handler.sendEmptyMessage(Constant.MSG_SUCCESS);

            }
        });
    }


    public void updateAccountMoney(final long accountID, final double money, final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                accountLocalDao.updateAccountMoney(accountID, money, _context);

                if (handler != null)
                    handler.sendEmptyMessage(Constant.MSG_SUCCESS);

            }
        });
    }


    public void updateAccountRemark(final long accountID, final String remark, final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                accountLocalDao.updateAccountRemark(accountID, remark, _context);

                if (handler != null)
                    handler.sendEmptyMessage(Constant.MSG_SUCCESS);

            }
        });
    }

    private AccountLocalDao accountLocalDao = new AccountLocalDao();
    private RecordLocalDAO recordLocalDAO = new RecordLocalDAO();
}
