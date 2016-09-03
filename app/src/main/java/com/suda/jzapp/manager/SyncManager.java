package com.suda.jzapp.manager;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.suda.jzapp.dao.cloud.avos.pojo.account.AVAccount;
import com.suda.jzapp.dao.cloud.avos.pojo.account.AVAccountIndex;
import com.suda.jzapp.dao.cloud.avos.pojo.record.AVRecord;
import com.suda.jzapp.dao.cloud.avos.pojo.record.AVRecordType;
import com.suda.jzapp.dao.cloud.avos.pojo.record.AVRecordTypeIndex;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.dao.greendao.Config;
import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.greendao.RecordType;
import com.suda.jzapp.dao.local.account.AccountLocalDao;
import com.suda.jzapp.dao.local.conf.ConfigLocalDao;
import com.suda.jzapp.dao.local.record.RecordLocalDAO;
import com.suda.jzapp.dao.local.record.RecordTypeLocalDao;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.DataConvertUtil;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.SPUtils;
import com.suda.jzapp.util.ThreadPoolUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by suda on 2016/9/2.
 */
public class SyncManager extends BaseManager {
    public SyncManager(Context context) {
        super(context);
    }

    public void forceBackup() {
        try {
            forceBackup(new Date(System.currentTimeMillis()));
        } catch (AVException r) {
            AVAnalytics.onError(_context, r.toString());
        }
    }

    /**
     * 强制备份
     *
     * @throws AVException
     */
    public void forceBackup(Date date) throws AVException {
        Config config = configLocalDao.getConfigByKey(RECORD_INDEX_UPDATE, _context);
        if (config != null && !config.getBooleanValue()) {
            AVRecordTypeIndex avRecordTypeIndex = new AVRecordTypeIndex();
            if (!TextUtils.isEmpty(config.getObjectID())) {
                avRecordTypeIndex.setObjectId(config.getObjectID());
            } else {
                AVQuery<AVRecordTypeIndex> query = AVObject.getQuery(AVRecordTypeIndex.class);
                query.whereEqualTo(AVRecordType.USER, MyAVUser.getCurrentUser());
                List<AVRecordTypeIndex> list = query.find();
                String objId = null;
                if (list.size() > 0) {
                    objId = list.get(0).getObjectId();
                }
                if (!TextUtils.isEmpty(objId)) {
                    avRecordTypeIndex.setObjectId(config.getObjectID());
                    config.setObjectID(objId);
                }
            }
            avRecordTypeIndex.setData(recordTypeDao.getRecordTypeIndexInfo(_context));
            avRecordTypeIndex.setUpdatedAt(date);
            avRecordTypeIndex.save();
            config.setBooleanValue(true);
            configLocalDao.updateConfig(config, _context);
        }

        config = configLocalDao.getConfigByKey(ACCOUNT_INDEX_UPDATE, _context);
        if (config != null && !config.getBooleanValue()) {
            AVAccountIndex avAccountIndex = new AVAccountIndex();
            if (!TextUtils.isEmpty(config.getObjectID())) {
                avAccountIndex.setObjectId(config.getObjectID());
            } else {
                AVQuery<AVAccountIndex> query = AVObject.getQuery(AVAccountIndex.class);
                query.whereEqualTo(AVRecordType.USER, MyAVUser.getCurrentUser());
                List<AVAccountIndex> list = query.find();
                String objId = null;
                if (list.size() > 0) {
                    objId = list.get(0).getObjectId();
                }
                if (!TextUtils.isEmpty(objId)) {
                    avAccountIndex.setObjectId(config.getObjectID());
                    config.setObjectID(objId);
                }
            }
            avAccountIndex.setData(accountLocalDao.getAccountIndexInfo(_context));
            avAccountIndex.setUpdatedAt(date);
            avAccountIndex.save();
            config.setBooleanValue(true);
            configLocalDao.updateConfig(config, _context);
        }

        //备份Record
        List<Record> records = recordLocalDAO.getNotBackData(_context);
        for (final Record record : records) {
            AVRecord avRecord = null;
            if (!TextUtils.isEmpty(record.getObjectID())) {
                avRecord = DataConvertUtil.convertRecord2AVRecord(record);
            } else {
                AVQuery<AVRecord> query = AVObject.getQuery(AVRecord.class);
                query.whereEqualTo(AVRecord.RECORD_ID, record.getRecordId());
                query.whereEqualTo(AVRecord.USER, MyAVUser.getCurrentUser());
                List<AVRecord> list = query.find();
                String objId = null;
                if (list.size() > 0) {
                    objId = list.get(0).getObjectId();
                }
                avRecord = DataConvertUtil.convertRecord2AVRecord(record);
                if (!TextUtils.isEmpty(objId)) {
                    avRecord.setObjectId(objId);
                    record.setObjectID(objId);
                }
            }
            avRecord.setUpdatedAt(date);
            avRecord.save();
            record.setSyncStatus(true);
            recordLocalDAO.updateOldRecord(_context, record);
        }

        //备份RecordType
        List<RecordType> recordTypes = recordTypeDao.getNotBackData(_context);
        for (final RecordType recordType : recordTypes) {
            AVRecordType avRecordType = null;
            if (!TextUtils.isEmpty(recordType.getObjectID())) {
                avRecordType = DataConvertUtil.convertRecordType2AVRecordType(recordType);
            } else {
                AVQuery<AVRecordType> query = AVObject.getQuery(AVRecordType.class);
                query.whereEqualTo(AVRecordType.USER, MyAVUser.getCurrentUser());
                query.whereEqualTo(AVRecordType.RECORD_TYPE_ID, recordType.getRecordTypeID());
                List<AVRecordType> list = query.find();
                String objId = null;
                if (list.size() > 0) {
                    objId = list.get(0).getObjectId();
                }
                avRecordType = DataConvertUtil.convertRecordType2AVRecordType(recordType);
                if (!TextUtils.isEmpty(objId)) {
                    avRecordType.setObjectId(objId);
                    recordType.setObjectID(objId);
                }
            }
            avRecordType.setUpdatedAt(date);
            avRecordType.save();
            recordType.setSyncStatus(true);
            recordTypeDao.updateRecordType(_context, recordType);
        }

        //备份Account
        List<Account> accountList = accountLocalDao.getNotBackData(_context);
        for (final Account account : accountList) {
            AVAccount avAccount = null;
            if (!TextUtils.isEmpty(account.getObjectID())) {
                avAccount = DataConvertUtil.convertAccount2AVAccount(account);
            } else {
                AVQuery<AVAccount> query = AVObject.getQuery(AVAccount.class);
                query.whereEqualTo(AVAccount.USER, MyAVUser.getCurrentUser());
                query.whereEqualTo(AVAccount.ACCOUNT_ID, account.getAccountID());
                List<AVAccount> list = query.find();
                String objId = null;
                if (list.size() > 0) {
                    objId = list.get(0).getObjectId();
                }
                avAccount = DataConvertUtil.convertAccount2AVAccount(account);
                if (!TextUtils.isEmpty(objId)) {
                    avAccount.setObjectId(objId);
                    account.setObjectID(objId);
                }
            }
            avAccount.setUpdatedAt(date);
            avAccount.save();
            account.setSyncStatus(true);
            accountLocalDao.updateAccount(_context, account);
        }
    }

    /**
     * 强制恢复
     *
     * @throws AVException
     */
    public void forceRestore(Date lastSyncDate) throws AVException {
        //恢复RecordType
        AVQuery<AVRecordType> avRecordTypeAVQuery = AVQuery.getQuery(AVRecordType.class);
        avRecordTypeAVQuery.whereEqualTo(AVRecordType.USER, MyAVUser.getCurrentUser());
        avRecordTypeAVQuery.whereGreaterThan(AVRecordType.UPDATED_AT, lastSyncDate);
        List<AVRecordType> avRecordTypes = avRecordTypeAVQuery.find();
        for (AVRecordType avRecordType : avRecordTypes) {
            RecordType recordType = new RecordType();
            recordType.setObjectID(avRecordType.getObjectId());
            recordType.setRecordTypeID(avRecordType.getRecordTypeId());
            recordType.setRecordType(avRecordType.getRecordType());
            recordType.setIsDel(avRecordType.isRecordTypeDel());
            recordType.setIndex(avRecordType.getIndex());
            recordType.setRecordIcon(avRecordType.getRecordRecordIcon());
            recordType.setSexProp(Constant.Sex.ALL.getId());
            recordType.setSysType(false);
            recordType.setOccupation(Constant.Occupation.ALL.getId());
            recordType.setSyncStatus(true);
            recordType.setRecordDesc(avRecordType.getRecordDesc());
            recordTypeDao.createOrUpdateRecordType(_context, recordType);
        }

        //恢复record
        AVQuery<AVRecord> avRecordAVQuery = AVQuery.getQuery(AVRecord.class);
        avRecordAVQuery.whereEqualTo(AVRecord.USER, MyAVUser.getCurrentUser());
        avRecordAVQuery.whereGreaterThan(AVRecord.UPDATED_AT, lastSyncDate);
        List<AVRecord> avRecords = avRecordAVQuery.find();
        for (AVRecord avRecord : avRecords) {
            Record record = new Record();
            record.setObjectID(avRecord.getObjectId());
            record.setAccountID(avRecord.getAccountId());
            record.setRecordId(avRecord.getRecordId());
            record.setRecordType(avRecord.getRecordType());
            record.setRecordTypeID(avRecord.getRecordTypeId());
            record.setRecordDate(avRecord.getRecordDate());
            record.setIsDel(avRecord.isRecordDel());
            record.setRecordMoney(avRecord.getRecordMoney());
            record.setRemark(avRecord.getRemark());
            record.setSyncStatus(true);
            recordLocalDAO.createOrUpdateRecord(_context, record);
        }

        //恢复Account
        AVQuery<AVAccount> accountAVQuery = AVQuery.getQuery(AVAccount.class);
        accountAVQuery.whereEqualTo(AVAccount.USER, MyAVUser.getCurrentUser());
        accountAVQuery.whereGreaterThan(AVAccount.UPDATED_AT, lastSyncDate);
        List<AVAccount> avAccounts = accountAVQuery.find();
        for (AVAccount avAccount : avAccounts) {
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
            accountLocalDao.createOrUpdateAccount(_context, account);
        }

        //更新排序
        recordManager.initRecordTypeIndex();
        accountManager.initAccountIndex();
    }

    /**
     * 强制同步
     *
     * @throws AVException
     */
    public void forceSync(final Handler handler) {
        if (!canSync()) {
            sendEmptyMessage(handler, Constant.MSG_ERROR);
            return;
        }
        if (System.currentTimeMillis() - ((long) SPUtils.get(_context, false, Constant.SP_LAST_SYNC_AT, 0l)) < 10 * 1000) {
            sendEmptyMessage(handler, Constant.MSG_SHORT);
            return;
        }

        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                long lastSync = (long) SPUtils.get(_context, false, Constant.SP_LAST_SYNC_AT, 0l);
                long newSync = System.currentTimeMillis();
                SPUtils.put(_context, false, Constant.SP_LAST_SYNC_AT, newSync);
                Date lastSyncDate = new Date(lastSync);
                Date newSyncDate = new Date(newSync);
                try {
                    forceRestore(lastSyncDate);
                    forceBackup(newSyncDate);
                    sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                } catch (AVException a) {
                    SPUtils.put(_context, false, Constant.SP_LAST_SYNC_AT, lastSync);
                    sendEmptyMessage(handler, Constant.MSG_ERROR);
                    LogUtils.getAvEx(a, _context);
                }
            }
        });
    }

    public int getNotBackDataCount() {
        List<Record> records = recordLocalDAO.getNotBackData(_context);
        List<RecordType> recordTypes = recordTypeDao.getNotBackData(_context);
        List<Account> accountList = accountLocalDao.getNotBackData(_context);
        Config config1 = configLocalDao.getConfigByKey(ACCOUNT_INDEX_UPDATE, _context);
        Config config2 = configLocalDao.getConfigByKey(RECORD_INDEX_UPDATE, _context);
        int notBackCount = 0;
        notBackCount += records.size();
        notBackCount += recordTypes.size();
        notBackCount += accountList.size();
        if (config1 != null && !config1.getBooleanValue())
            notBackCount++;
        if (config2 != null && !config1.getBooleanValue())
            notBackCount++;
        return notBackCount;
    }

    RecordLocalDAO recordLocalDAO = new RecordLocalDAO();
    RecordTypeLocalDao recordTypeDao = new RecordTypeLocalDao();
    ConfigLocalDao configLocalDao = new ConfigLocalDao();
    AccountLocalDao accountLocalDao = new AccountLocalDao();
    AccountManager accountManager = new AccountManager(_context);
    RecordManager recordManager = new RecordManager(_context);
    private final static String RECORD_INDEX_UPDATE = "RECORD_INDEX_UPDATE";
    private final static String ACCOUNT_INDEX_UPDATE = "ACCOUNT_INDEX_UPDATE";
}
