package com.suda.jzapp.manager;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.suda.jzapp.dao.cloud.avos.pojo.account.AVAccount;
import com.suda.jzapp.dao.cloud.avos.pojo.account.AVAccountIndex;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.dao.greendao.AccountType;
import com.suda.jzapp.dao.greendao.Config;
import com.suda.jzapp.dao.local.account.AccountLocalDao;
import com.suda.jzapp.dao.local.conf.ConfigLocalDao;
import com.suda.jzapp.dao.local.record.RecordLocalDAO;
import com.suda.jzapp.manager.domain.AccountDetailDO;
import com.suda.jzapp.manager.domain.AccountIndexDO;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.DataConvertUtil;
import com.suda.jzapp.util.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.Date;
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
        if (account == null)
            return null;
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
    public void createNewAccount(String accountName, double accountMoney, int accountTypeID, String accountRemark, int accountColor, final Handler handler) {
        final Account account = new Account();
        account.setAccountID(System.currentTimeMillis());
        account.setAccountColor("");
        account.setAccountName(accountName);
        account.setAccountMoney(accountMoney);
        account.setAccountTypeID(accountTypeID);
        account.setAccountRemark(accountRemark);
        account.setSyncStatus(true);
        account.setIsDel(false);
        account.setAccountColor(accountColor + "");

        Date now = new Date(System.currentTimeMillis());
        if (Constant.newSyncSwitch) {
            account.setCreatedAt(now);
            account.setUpdatedAt(now);
        }

        if (canSync()) {
            AVAccount avAccount = DataConvertUtil.convertAccount2AVAccount(account);
            avAccount.setAccountIsDel(false);
            if (Constant.newSyncSwitch) {
                avAccount.put(AVAccount.UPDATED_AT, now);
                avAccount.put(AVAccount.CREATED_AT, now);
            }
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
                    sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                }
            });
        } else {
            account.setSyncStatus(false);
            accountLocalDao.createNewAccount(account, _context);
            sendEmptyMessage(handler, Constant.MSG_SUCCESS);
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
        if (account == null)
            return;
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
     * 更新账户颜色
     *
     * @param accountID
     * @param color
     * @param handler
     */
    public void updateAccountColor(final long accountID, final String color, final Handler handler) {
        editAccount(EDIT_TYPE_ACCOUNT_COLOR, accountID, color, 0, 0, null, new Callback() {
            @Override
            public void doSth(boolean isSync, String objId) {
                accountLocalDao.updateAccountColor(accountID, color, isSync, objId, _context);
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

        final Date now = new Date(System.currentTimeMillis());

        if (canSync()) {
            Account account = accountLocalDao.getAccountByID(accountID, _context);
            if (Constant.newSyncSwitch) {
                account.setUpdatedAt(now);
            }
            if (!TextUtils.isEmpty(account.getObjectID())) {
                AVAccount avAccount = DataConvertUtil.convertAccount2AVAccount(account);
                avAccount.setAccountIsDel(false);
                if (Constant.newSyncSwitch) {
                    avAccount.put(AVObject.UPDATED_AT, now);
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
                } else if (editType == EDIT_TYPE_ACCOUNT_COLOR) {
                    avAccount.setAccountColor(remark);
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
                        if (Constant.newSyncSwitch) {
                            avAccount.put(AVObject.UPDATED_AT, now);
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
                        } else if (editType == EDIT_TYPE_ACCOUNT_COLOR) {
                            avAccount.setAccountColor(remark);
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
     */
    public void initAccountData() throws AVException {
        AVQuery<AVAccount> query = AVObject.getQuery(AVAccount.class);
        query.limit(1000);
        query.whereEqualTo(AVAccount.USER, MyAVUser.getCurrentUser());
        List<AVAccount> list = query.find();
        if (list.size() > 0) {
            accountLocalDao.clearAllAccount(_context);
            int i = 0;
            for (AVAccount avAccount : list) {
                Account account = new Account();
                account.setObjectID(avAccount.getObjectId());
                account.setAccountID(avAccount.getAccountId());
                account.setAccountTypeID(avAccount.getAccountTypeId());
                account.setAccountMoney(recordLocalDAO.getAccountMoneyByRecord(_context, avAccount.getAccountId()));
                account.setAccountRemark(avAccount.getAccountRemark());
                account.setIsDel(avAccount.isAccountDel());
                account.setAccountColor(avAccount.getAccountColor());
                account.setAccountName(avAccount.getAccountName());
                account.setSyncStatus(true);
                account.setIndex(i);
                if (Constant.newSyncSwitch) {
                    account.setCreatedAt(avAccount.getCreatedAt());
                    account.setUpdatedAt(avAccount.getUpdatedAt());
                }
                i++;
                accountLocalDao.createNewAccount(account, _context);
            }
        }
        initAccountIndex();
    }

    /**
     * 初始化账户索引数据
     */
    public void initAccountIndex() throws AVException {
        AVQuery<AVAccountIndex> query = AVObject.getQuery(AVAccountIndex.class);
        query.whereEqualTo(AVAccountIndex.USER, MyAVUser.getCurrentUser());
        List<AVAccountIndex> list = query.find();
        if (list.size() > 0) {
            AVAccountIndex avAccountIndex = list.get(0);
            String data = avAccountIndex.getData();
            List<AccountIndexDO> accountIndexDOs = JSON.parseArray(data, AccountIndexDO.class);
            Config config = new Config();
            config.setObjectID(avAccountIndex.getObjectId());
            config.setKey(ACCOUNT_INDEX_UPDATE);
            config.setValue("true");
            if (Constant.newSyncSwitch) {
                config.setUpdatedAt(avAccountIndex.getUpdatedAt());
                config.setCreatedAt(avAccountIndex.getCreatedAt());
            }
            configLocalDao.updateConfig(config, _context);
            accountLocalDao.updateAccountIndexByAccountIndex(_context, accountIndexDOs);
        }
    }

    /**
     * 更新账户排序
     *
     * @param handler
     * @param list
     */
    public void updateAccountIndex(final Handler handler, List<AccountDetailDO> list) {
        if (list != null) {
            accountLocalDao.updateAccountIndex(_context, list);
        }
        final Date now = new Date(System.currentTimeMillis());
        if (canSync()) {
            final Config config = configLocalDao.getConfigByKey(ACCOUNT_INDEX_UPDATE, _context);
            if (config != null && "true".equals(config.getValue()) && list == null) {
                sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                return;
            }
            if (config != null && !TextUtils.isEmpty(config.getObjectID())) {
                AVAccountIndex avAccountIndex = new AVAccountIndex();
                avAccountIndex.setObjectId(config.getObjectID());
                if (Constant.newSyncSwitch) {
                    avAccountIndex.put(AVObject.UPDATED_AT, now);
                    config.setUpdatedAt(now);
                }
                avAccountIndex.setData(accountLocalDao.getAccountIndexInfo(_context));
                avAccountIndex.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        getAvEx(e);
                        if (e == null) {
                            config.setValue("true");
                        } else {
                            config.setValue("false");
                        }
                        configLocalDao.updateConfig(config, _context);
                        sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                    }
                });
                return;
            }

            AVQuery<AVAccountIndex> query = AVObject.getQuery(AVAccountIndex.class);
            query.whereEqualTo(AVAccountIndex.USER, MyAVUser.getCurrentUser());
            query.findInBackground(new FindCallback<AVAccountIndex>() {
                @Override
                public void done(List<AVAccountIndex> list, AVException e) {
                    getAvEx(e);
                    if (e != null) {
                        sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                        Config config = configLocalDao.getConfigByKey(ACCOUNT_INDEX_UPDATE, _context);
                        if (config == null) {
                            config = new Config();
                        }
                        if (Constant.newSyncSwitch) {
                            config.setUpdatedAt(now);
                            config.setCreatedAt(now);
                        }
                        config.setKey(ACCOUNT_INDEX_UPDATE);
                        config.setValue("false");
                        configLocalDao.updateConfig(config, _context);
                    } else {
                        AVAccountIndex avAccountIndex = null;
                        if (list.size() > 0) {
                            avAccountIndex = list.get(0);
                        } else {
                            avAccountIndex = new AVAccountIndex();
                        }
                        if (Constant.newSyncSwitch) {
                            avAccountIndex.put(AVObject.UPDATED_AT, now);
                        }
                        final String objId = avAccountIndex.getObjectId();
                        avAccountIndex.setUser(MyAVUser.getCurrentUser());
                        avAccountIndex.setData(accountLocalDao.getAccountIndexInfo(_context));
                        avAccountIndex.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                getAvEx(e);
                                Config config = configLocalDao.getConfigByKey(ACCOUNT_INDEX_UPDATE, _context);
                                if (config == null) {
                                    config = new Config();
                                }
                                config.setObjectID(objId);
                                config.setKey(ACCOUNT_INDEX_UPDATE);
                                if (e == null) {
                                    config.setValue("true");
                                } else {
                                    config.setValue("false");
                                }
                                configLocalDao.updateConfig(config, _context);
                                sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                            }
                        });
                    }
                }
            });
        }
    }

    public void updateBudget(double money) {
        AccountLocalDao accountLocalDao = new AccountLocalDao();
        accountLocalDao.updateBudget(_context, money);
    }

    private final static int EDIT_TYPE_DEL = -1;
    private final static int EDIT_TYPE_ACCOUNT_NAME = 0;
    private final static int EDIT_TYPE_ACCOUNT_MONEY = 1;
    private final static int EDIT_TYPE_ACCOUNT_TYPE = 2;
    private final static int EDIT_TYPE_ACCOUNT_REMARK = 3;
    private final static int EDIT_TYPE_ACCOUNT_COLOR = 4;


    private AccountLocalDao accountLocalDao = new AccountLocalDao();
    private RecordLocalDAO recordLocalDAO = new RecordLocalDAO();
    private ConfigLocalDao configLocalDao = new ConfigLocalDao();
    private final static String ACCOUNT_INDEX_UPDATE = "ACCOUNT_INDEX_UPDATE";
}
