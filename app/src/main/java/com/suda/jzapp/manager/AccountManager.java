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
     * 根据id查账户详细
     *
     * @param accountID
     * @return
     */
    public AccountDetailDO getAccountByID(final long accountID) {
        Account account = accountLocalDao.getAccountByID(accountID, _context);
        AccountType accountType = accountLocalDao.getAccountTypeByID(account.getAccountTypeID(), _context);
        AccountDetailDO accountDetailDO = DataConvertUtil.getAccountDetailDO(account, accountType);
        return accountDetailDO;
    }

    /**
     * 根据id查账户类型
     *
     * @param accountTypeId
     * @param handler
     */
    public void getAccountTypeByID(final long accountTypeId, final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                AccountType accountType = accountLocalDao.getAccountTypeByID(accountTypeId, _context);
                sendMessage(handler, accountType, accountType != null);
            }
        });
    }

    /**
     * 获取所有账户信息
     *
     * @param handler
     */
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

    /**
     * 查询所有账户类型
     *
     * @param handler
     */
    public void getAllAccountType(final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                List<AccountType> accountTypes = accountLocalDao.getAllAccountType(_context);
                sendMessage(handler, accountTypes);
            }
        });
    }

    /**
     * 创建新账户
     *
     * @param accountName
     * @param accountMoney
     * @param accountTypeID
     * @param accountRemark
     * @param handler
     */
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
            AVAccount avAccount = DataConvertUtil.convertAccount2AVAccount(account);
            avAccount.setAccountIsDel(false);
            avAccount.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        accountLocalDao.createNewAccount(account, _context);
                    } else {
                        account.setSyncStatus(false);
                        accountLocalDao.createNewAccount(account, _context);
                        getAvEx(e);
                    }
                    sendEmptyMessage(handler,Constant.MSG_SUCCESS);
                }
            });
        } else {
            account.setSyncStatus(false);
            accountLocalDao.createNewAccount(account, _context);
            sendEmptyMessage(handler,Constant.MSG_SUCCESS);
        }
    }

    /**
     * 删除账户
     *
     * @param accountID
     * @param handler
     */
    public void deleteAccountByID(final long accountID, final Handler handler) {
        editAccount(EDIT_TYPE_DEL, accountID, null, 0, 0, null, new Callback() {
            @Override
            public void doSth(boolean isSync, String objId) {
                accountLocalDao.deleteAccount(accountID, true, objId, _context);
            }
        }, handler);
    }

    /**
     * 更新账户名
     *
     * @param accountID
     * @param accountName
     * @param handler
     */
    public void updateAccountName(final long accountID, final String accountName, final Handler handler) {
        editAccount(EDIT_TYPE_ACCOUNT_NAME, accountID, null, 0, 0, accountName, new Callback() {
            @Override
            public void doSth(boolean isSync, String objId) {
                accountLocalDao.updateAccountName(accountID, accountName, isSync, objId, _context);
            }
        }, handler);
    }


    /**
     * 更新账户类型
     *
     * @param accountID
     * @param typeID
     * @param handler
     */
    public void updateAccountTypeID(final long accountID, final int typeID, final Handler handler) {
        editAccount(EDIT_TYPE_ACCOUNT_TYPE, accountID, null, typeID, 0, null, new Callback() {
            @Override
            public void doSth(boolean isSync, String objId) {
                accountLocalDao.updateAccountTypeID(accountID, typeID, isSync, objId, _context);
            }
        }, handler);
    }

    /**
     * 更新账户余额
     *
     * @param accountID
     * @param money
     * @param handler
     */
    public void updateAccountMoney(final long accountID, final double money, final Handler handler) {
        Account account = accountLocalDao.getAccountByID(accountID, _context);
        editAccount(EDIT_TYPE_ACCOUNT_MONEY, accountID, null, 0, account.getAccountMoney() + money, null, new Callback() {
            @Override
            public void doSth(boolean isSync, String objId) {
                accountLocalDao.updateAccountMoney(accountID, money, isSync, objId, _context);
            }
        }, handler);
    }


    /**
     * 更新账户说明
     *
     * @param accountID
     * @param remark
     * @param handler
     */
    public void updateAccountRemark(final long accountID, final String remark, final Handler handler) {
        editAccount(EDIT_TYPE_ACCOUNT_REMARK, accountID, remark, 0, 0, null, new Callback() {
            @Override
            public void doSth(boolean isSync, String objId) {
                accountLocalDao.updateAccountRemark(accountID, remark, isSync, objId, _context);
            }
        }, handler);
    }

    /**
     * 修改账户通用方法
     *
     * @param editType
     * @param accountID
     * @param remark
     * @param typeID
     * @param money
     * @param accountName
     * @param callback
     * @param handler
     */
    private void editAccount(final int editType, final long accountID, final String remark, final int typeID, final double money, final String accountName,
                             final Callback callback, final Handler handler) {
        if (!TextUtils.isEmpty(MyAVUser.getCurrentUserId())) {
            Account account = accountLocalDao.getAccountByID(accountID, _context);
            if (!TextUtils.isEmpty(account.getObjectID())) {
                AVAccount avAccount = DataConvertUtil.convertAccount2AVAccount(account);
                avAccount.setAccountIsDel(false);
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
                        getAvEx(e);
                        if (e == null) {
                            callback.doSth(true, null);
                        } else {
                            callback.doSth(false, null);
                        }
                        sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                    }
                });
                return;
            }

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
                            avAccount = DataConvertUtil.convertAccount2AVAccount(account);
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
                        final String objId = avAccount.getObjectId();
                        avAccount.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    callback.doSth(true, objId);
                                } else {
                                    getAvEx(e);
                                    callback.doSth(false, objId);
                                }
                                sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                            }
                        });
                    } else {
                        getAvEx(e);
                        callback.doSth(false, null);
                        sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                    }
                }
            });
        } else {
            callback.doSth(false, null);
            sendEmptyMessage(handler, Constant.MSG_SUCCESS);
        }
    }

    /**
     * 从云端获取全部账户信息初始化数据
     *
     * @param handler
     */
    public void initAccountData(final Handler handler) {
        AVQuery<AVAccount> query = AVObject.getQuery(AVAccount.class);

        query.limit(1000);
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
                        account.setObjectID(avAccount.getObjectId());
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
