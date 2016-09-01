package com.suda.jzapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.suda.jzapp.dao.cloud.avos.pojo.account.AVAccount;
import com.suda.jzapp.dao.cloud.avos.pojo.record.AVRecord;
import com.suda.jzapp.dao.cloud.avos.pojo.record.AVRecordType;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.greendao.RecordType;
import com.suda.jzapp.dao.local.account.AccountLocalDao;
import com.suda.jzapp.dao.local.record.RecordLocalDAO;
import com.suda.jzapp.dao.local.record.RecordTypeLocalDao;
import com.suda.jzapp.manager.AccountManager;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.AlarmUtil;
import com.suda.jzapp.util.DataConvertUtil;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.NetworkUtil;
import com.suda.jzapp.util.SPUtils;

import java.util.List;

/**
 * Created by Suda on 2015/10/9.
 */
public class BackupService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if ((boolean) SPUtils.get(this, true, Constant.SP_ALARM_EVERY_DAY, false))
            AlarmUtil.createAlarm(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (canSync()) {
            LogUtils.e(TAG, "START_SYNC");
            syncRecord();
            syncAccount();
            syncRecordType();
            recordManager.updateRecordTypeIndex(null, true);
            accountManager.updateAccountIndex(null, null);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void syncRecord() {
        List<Record> records = recordLocalDAO.getNotSyncData(this);
        for (final Record record : records) {
            LogUtils.e(TAG, "START_SYNC_RECORD" + record.getRecordId());
            if (TextUtils.isEmpty(record.getObjectID())) {
                AVQuery<AVRecord> query = AVObject.getQuery(AVRecord.class);
                query.whereEqualTo(AVRecord.RECORD_ID, record.getRecordId());
                query.whereEqualTo(AVRecord.USER, MyAVUser.getCurrentUser());
                query.findInBackground(new FindCallback<AVRecord>() {
                    @Override
                    public void done(List<AVRecord> list, AVException e) {
                        if (e == null) {
                            AVRecord avRecord = null;
                            String objId = null;
                            if (list.size() > 0) {
                                objId = list.get(0).getObjectId();
                            }

                            avRecord = DataConvertUtil.convertRecord2AVRecord(record);
                            if (!TextUtils.isEmpty(objId)) {
                                avRecord.setObjectId(objId);
                                record.setObjectID(objId);
                            }
                            avRecord.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    record.setSyncStatus(true);
                                    recordLocalDAO.updateOldRecord(BackupService.this, record);
                                }
                            });
                        }
                    }
                });

            } else {
                AVRecord avRecord = DataConvertUtil.convertRecord2AVRecord(record);
                avRecord.setObjectId(record.getObjectID());
                avRecord.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        record.setSyncStatus(true);
                        recordLocalDAO.updateOldRecord(BackupService.this, record);
                    }
                });
            }
        }
    }

    private void syncAccount() {
        List<Account> accountList = accountLocalDao.getNotSyncData(this);
        for (final Account account : accountList) {
            LogUtils.e(TAG, "START_SYNC_ACCOUNT" + account.getAccountID());
            if (!TextUtils.isEmpty(account.getObjectID())) {
                AVAccount avAccount = DataConvertUtil.convertAccount2AVAccount(account);
                avAccount.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        account.setSyncStatus(true);
                        accountLocalDao.updateAccount(BackupService.this, account);
                    }
                });
            } else {
                AVQuery<AVAccount> query = AVObject.getQuery(AVAccount.class);
                query.whereEqualTo(AVAccount.USER, MyAVUser.getCurrentUser());
                query.findInBackground(new FindCallback<AVAccount>() {
                    @Override
                    public void done(List<AVAccount> list, AVException e) {
                        if (e == null) {
                            String objId = null;
                            if (list.size() > 0) {
                                objId = list.get(0).getObjectId();
                            }
                            AVAccount avAccount = DataConvertUtil.convertAccount2AVAccount(account);
                            if (!TextUtils.isEmpty(objId)) {
                                avAccount.setObjectId(objId);
                                account.setObjectID(objId);
                            }
                            avAccount.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    account.setSyncStatus(true);
                                    accountLocalDao.updateAccount(BackupService.this, account);
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    private void syncRecordType() {
        List<RecordType> recordTypes = recordTypeLocalDao.getNotSyncData(this);
        for (final RecordType recordType : recordTypes) {
            LogUtils.e(TAG, "START_SYNC_RECORD_TYPE" + recordType.getRecordTypeID());
            if (!TextUtils.isEmpty(recordType.getObjectID())) {
                AVRecordType avRecordType = DataConvertUtil.convertRecordType2AVRecordType(recordType);
                avRecordType.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        recordType.setSyncStatus(true);
                        recordTypeLocalDao.updateRecordType(BackupService.this, recordType);
                    }
                });
            } else {
                AVQuery<AVRecordType> query = AVObject.getQuery(AVRecordType.class);
                query.whereEqualTo(AVRecordType.USER, MyAVUser.getCurrentUser());
                query.findInBackground(new FindCallback<AVRecordType>() {
                    @Override
                    public void done(List<AVRecordType> list, AVException e) {
                        if (e == null) {
                            String objId = null;
                            if (list.size() > 0) {
                                objId = list.get(0).getObjectId();
                            }
                            AVRecordType avRecordType = DataConvertUtil.convertRecordType2AVRecordType(recordType);
                            if (!TextUtils.isEmpty(objId)) {
                                avRecordType.setObjectId(objId);
                                recordType.setObjectID(objId);
                            }
                            avRecordType.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    recordType.setSyncStatus(true);
                                    recordTypeLocalDao.updateRecordType(BackupService.this, recordType);
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    private boolean canSync() {
        return MyAVUser.getCurrentUser() != null &&
                ((boolean) SPUtils.get(this, true, Constant.SP_SYNC_ONLY_WIFI, false) ? NetworkUtil.checkWifi(this) : NetworkUtil.checkNetwork(this));
    }


    private RecordLocalDAO recordLocalDAO = new RecordLocalDAO();
    private RecordTypeLocalDao recordTypeLocalDao = new RecordTypeLocalDao();
    private AccountLocalDao accountLocalDao = new AccountLocalDao();
    private RecordManager recordManager = new RecordManager(this);
    private AccountManager accountManager = new AccountManager(this);
    private static final String TAG = "SYNC_SERVICE";
}
