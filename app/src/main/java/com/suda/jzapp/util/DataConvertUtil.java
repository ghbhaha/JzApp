package com.suda.jzapp.util;

import android.text.TextUtils;

import com.suda.jzapp.dao.cloud.avos.pojo.account.AVAccount;
import com.suda.jzapp.dao.cloud.avos.pojo.record.AVRecord;
import com.suda.jzapp.dao.cloud.avos.pojo.record.AVRecordType;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.dao.greendao.AccountType;
import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.greendao.RecordType;
import com.suda.jzapp.manager.domain.AccountDetailDO;

/**
 * Created by ghbha on 2016/2/16.
 */
public class DataConvertUtil {
    public static AccountDetailDO getAccountDetailDO(Account account, AccountType accountType) {

        AccountDetailDO accountDetailDO = new AccountDetailDO();

        if (account == null) {
            return null;
        }

        accountDetailDO.setId(account.getId());
        accountDetailDO.setAccountID(account.getAccountID());
        accountDetailDO.setAccountTypeID(account.getAccountTypeID());
        accountDetailDO.setAccountName(account.getAccountName());
        accountDetailDO.setAccountMoney(account.getAccountMoney());
        accountDetailDO.setAccountRemark(account.getAccountRemark());
        accountDetailDO.setAccountColor(account.getAccountColor());
        accountDetailDO.setSyncStatus(account.getSyncStatus());
        accountDetailDO.setIsDel(account.getIsDel());
        accountDetailDO.setAccountDesc(accountType.getAccountDesc());
        accountDetailDO.setAccountIcon(accountType.getAccountIcon());

        return accountDetailDO;
    }

    public static AVRecord convertRecord2AVRecord(Record record) {
        AVRecord avRecord = new AVRecord();
        avRecord.setRecordId(record.getRecordId());
        avRecord.setUser(MyAVUser.getCurrentUser());
        avRecord.setRecordId(record.getRecordId());
        avRecord.setAccountId(record.getAccountID());
        avRecord.setRecordDate(record.getRecordDate());
        avRecord.setRecordMoney(record.getRecordMoney());
        avRecord.setRecordTypeId(record.getRecordTypeID());
        avRecord.setRecordType(record.getRecordType());
        avRecord.setRemark(record.getRemark());
        avRecord.setRecordIsDel(record.getIsDel());
        return avRecord;
    }

    public static AVAccount convertAccount2AVAccount(Account account) {
        AVAccount avAccount = new AVAccount();
        avAccount.setAccountName(account.getAccountName());
        avAccount.setUser(MyAVUser.getCurrentUser());
        avAccount.setAccountColor(account.getAccountColor());
        avAccount.setAccountId(account.getAccountID());
        avAccount.setAccountTypeId(account.getAccountTypeID());
        avAccount.setAccountMoney(account.getAccountMoney());
        avAccount.setAccountRemark(account.getAccountRemark());
        avAccount.setAccountIsDel(account.getIsDel());
        if (!TextUtils.isEmpty(account.getObjectID())) {
            avAccount.setObjectId(account.getObjectID());
        }
        return avAccount;
    }

    public static AVRecordType convertRecordType2AVRecordType(RecordType recordType) {
        AVRecordType avRecordType = new AVRecordType();
        avRecordType.setRecordType(recordType.getRecordType());
        avRecordType.setRecordTypeId(recordType.getRecordTypeID());
        avRecordType.setUser(MyAVUser.getCurrentUser());
        avRecordType.setRecordIcon(recordType.getRecordIcon());
        avRecordType.setIndex(recordType.getIndex());
        avRecordType.setRecordDesc(recordType.getRecordDesc());
        avRecordType.setRecordTypeIsDel(recordType.getIsDel());
        if (!TextUtils.isEmpty(recordType.getObjectID())) {
            avRecordType.setObjectId(recordType.getObjectID());
        }
        return avRecordType;
    }
}
