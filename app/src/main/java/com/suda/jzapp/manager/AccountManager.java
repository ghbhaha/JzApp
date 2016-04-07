package com.suda.jzapp.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.suda.jzapp.dao.cloud.avos.pojo.account.AVAccount;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.dao.greendao.AccountType;
import com.suda.jzapp.dao.local.account.AccountLocalDao;
import com.suda.jzapp.dao.local.record.RecordLocalDAO;
import com.suda.jzapp.manager.domain.AccountDetailDO;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.DataConvertUtil;
import com.suda.jzapp.util.LogUtils;
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

    public Account getSuitAccount() {
        return accountLocalDao.getSuitAccount(_context);
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
                    accountDetailDO.setTodayCost(recordLocalDAO.countTodayCostByAccountId(_context, account.getAccountID()));
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
        account.setSyncStatus(true);
        account.setIsDel(false);
        if (!TextUtils.isEmpty(MyAVUser.getCurrentUserId())) {
            AVAccount avAccount = new AVAccount();
            avAccount.setAccountName(account.getAccountName());
            avAccount.setUser(MyAVUser.getCurrentUser());
            avAccount.setAccountColor(account.getAccountColor());
            avAccount.setAccountId(account.getAccountID());
            avAccount.setAccountTypeId(account.getAccountTypeID());
            avAccount.setAccountMoney(account.getAccountMoney());
            avAccount.setAccountRemark(account.getAccountRemark());
            avAccount.setAccountIsDel(false);
            avAccount.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        accountLocalDao.createNewAccount(account, _context);
                    } else {
                        account.setSyncStatus(false);
                        accountLocalDao.createNewAccount(account, _context);
                        LogUtils.getAvEx(e, _context);
                    }
                    if (handler != null)
                        handler.sendEmptyMessage(Constant.MSG_SUCCESS);
                }
            });
        } else {
            account.setSyncStatus(false);
            accountLocalDao.createNewAccount(account, _context);
            if (handler != null)
                handler.sendEmptyMessage(Constant.MSG_SUCCESS);
        }
    }

    public void deleteAccountByID(final long accountID, final Handler handler) {
        editAccount(EDIT_TYPE_DEL, accountID, null, 0, 0, null, new Callback() {
            @Override
            public void doSth(boolean isSync) {
                accountLocalDao.deleteAccount(accountID, true, _context);
            }
        }, handler);
    }

    public void updateAccountName(final long accountID, final String accountName, final Handler handler) {
        editAccount(EDIT_TYPE_ACCOUNT_NAME, accountID, null, 0, 0, accountName, new Callback() {
            @Override
            public void doSth(boolean isSync) {
                accountLocalDao.updateAccountName(accountID, accountName, isSync, _context);
            }
        }, handler);
    }


    public void updateAccountTypeID(final long accountID, final int typeID, final Handler handler) {
        editAccount(EDIT_TYPE_ACCOUNT_TYPE, accountID, null, typeID, 0, null, new Callback() {
            @Override
            public void doSth(boolean isSync) {
                accountLocalDao.updateAccountTypeID(accountID, typeID, isSync, _context);
            }
        }, handler);
    }


    public void updateAccountMoney(final long accountID, final double money, final Handler handler) {
        Account account = accountLocalDao.getAccountByID(accountID, _context);
        editAccount(EDIT_TYPE_ACCOUNT_MONEY, accountID, null, 0, account.getAccountMoney() + money, null, new Callback() {
            @Override
            public void doSth(boolean isSync) {
                accountLocalDao.updateAccountMoney(accountID, money, isSync, _context);
            }
        }, handler);
    }


    public void updateAccountRemark(final long accountID, final String remark, final Handler handler) {

        editAccount(EDIT_TYPE_ACCOUNT_REMARK, accountID, remark, 0, 0, null, new Callback() {
            @Override
            public void doSth(boolean isSync) {
                accountLocalDao.updateAccountRemark(accountID, remark, isSync, _context);
            }
        }, handler);
    }

    private void editAccount(final int editType, final long accountID, final String remark, final int typeID, final double money, final String accountName,
                             final Callback callback, final Handler handler) {
        if (!TextUtils.isEmpty(MyAVUser.getCurrentUserId())) {
            AVQuery<AVAccount> query = AVObject.getQuery(AVAccount.class);
            query.whereEqualTo(AVAccount.ACCOUNT_ID, accountID);
            query.whereEqualTo(AVAccount.USER, MyAVUser.getCurrentUser());
            query.findInBackground(new FindCallback<AVAccount>() {
                @Override
                public void done(List<AVAccount> list, AVException e) {
                    if (e == null) {
                        AVAccount avAccount = null;
                        if (list.size() > 0) {
                            avAccount = list.get(0);
                        } else {
                            Account account = accountLocalDao.getAccountByID(accountID, _context);
                            avAccount = new AVAccount();
                            avAccount.setAccountName(account.getAccountName());
                            avAccount.setUser(MyAVUser.getCurrentUser());
                            avAccount.setAccountColor(account.getAccountColor());
                            avAccount.setAccountId(account.getAccountID());
                            avAccount.setAccountTypeId(account.getAccountTypeID());
                            avAccount.setAccountMoney(account.getAccountMoney());
                            avAccount.setAccountRemark(account.getAccountRemark());
                            avAccount.setAccountIsDel(false);
                        }
                        if (editType == EDIT_TYPE_DEL) {
                            avAccount.setAccountIsDel(true);
                        } else if (editType == EDIT_TYPE_ACCOUNT_TYPE) {
                            avAccount.setAccountTypeId(typeID);
                        } else if (editType == EDIT_TYPE_ACCOUNT_MONEY) {
                            avAccount.setAccountMoney(money);
                        } else if (editType == EDIT_TYPE_ACCOUNT_REMARK) {
                            avAccount.setAccountRemark(remark);
                        } else if (editType == EDIT_TYPE_ACCOUNT_NAME) {
                            avAccount.setAccountName(accountName);
                        }
                        avAccount.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    callback.doSth(true);
                                } else {
                                    getAvEx(e);
                                    callback.doSth(false);
                                }
                                if (handler != null)
                                    handler.sendEmptyMessage(Constant.MSG_SUCCESS);
                            }
                        });
                    } else {
                        getAvEx(e);
                        callback.doSth(false);
                        if (handler != null)
                            handler.sendEmptyMessage(Constant.MSG_SUCCESS);
                    }
                }
            });
        } else {
            callback.doSth(false);
            if (handler != null)
                handler.sendEmptyMessage(Constant.MSG_SUCCESS);
        }
    }

    public void initAccountData(final Handler handler) {
        AVQuery<AVAccount> query = AVObject.getQuery(AVAccount.class);
        query.whereEqualTo(AVAccount.USER, MyAVUser.getCurrentUser());
        query.findInBackground(new FindCallback<AVAccount>() {
            @Override
            public void done(List<AVAccount> list, AVException e) {
                Message message = new Message();
                if (list.size() > 0)
                    accountLocalDao.clearAllAccount(_context);
                if (e == null) {
                    for (AVAccount avAccount : list) {
                        Account account = new Account();
                        account.setAccountID(avAccount.getAccountId());
                        account.setAccountTypeID(avAccount.getAccountTypeId());
                        account.setAccountMoney(avAccount.getAccountMoney());
                        account.setAccountRemark(avAccount.getAccountRemark());
                        account.setIsDel(avAccount.isAccountDel());
                        account.setAccountName(avAccount.getAccountName());
                        account.setSyncStatus(true);
                        accountLocalDao.createNewAccount(account, _context);
                    }
                    message.what = Constant.MSG_SUCCESS;
                } else {
                    message.what = Constant.MSG_ERROR;
                    getAvEx(e);
                }
                handler.sendMessage(message);
            }
        });
    }

    private final static int EDIT_TYPE_DEL = -1;
    private final static int EDIT_TYPE_ACCOUNT_NAME = 0;
    private final static int EDIT_TYPE_ACCOUNT_MONEY = 1;
    private final static int EDIT_TYPE_ACCOUNT_TYPE = 2;
    private final static int EDIT_TYPE_ACCOUNT_REMARK = 3;

    private AccountLocalDao accountLocalDao = new AccountLocalDao();
    private RecordLocalDAO recordLocalDAO = new RecordLocalDAO();
}
