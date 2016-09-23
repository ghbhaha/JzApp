package com.suda.jzapp.dao.cloud.avos.pojo.record;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.suda.jzapp.util.DateTimeUtil;

import java.util.Date;

/**
 * Created by Suda on 2015/9/16.
 */

@AVClassName("Record")
public class AVRecord extends AVObject {

    public void setUser(AVUser user) {
        put(USER, user);
    }

    public void setAccountId(long accountId) {
        put(ACCOUNT_ID, accountId);
    }

    public void setRecordTypeId(long recordTypeId) {
        put(RECORD_TYPE_ID, recordTypeId);
    }

    public void setRecordMoney(double money) {
        put(RECORD_MONEY, money);
    }

    public void setRecordDate(Date date) {
        put(RECORD_DATE, date);
    }

    public void setRemark(String remark) {
        put(REMARK, remark);
    }

    public void setRecordId(long recordId) {
        put(RECORD_ID, recordId);
    }

    public void setRecordType(int recordType) {
        put(RECORD_TYPE, recordType);
    }

    public void setRecordIsDel(boolean isDel) {
        put(RECORD_IS_DEL, isDel);
    }

    public long getAccountId() {
        return getLong(ACCOUNT_ID);
    }

    public AVUser getUser() {
        return getAVUser(USER, AVUser.class);
    }

    public long getRecordTypeId() {
        return getLong(RECORD_TYPE_ID);
    }

    public double getRecordMoney() {
        return getDouble(RECORD_MONEY);
    }

    public Date getRecordDate() {
        return getDate(RECORD_DATE);
    }

    public String getRemark() {
        return getString(REMARK);
    }

    public long getRecordId() {
        return getLong(RECORD_ID);
    }

    public int getRecordType() {
        return getInt(RECORD_TYPE);
    }

    public boolean isRecordDel() {
        return getBoolean(RECORD_IS_DEL);
    }

    public String getRecordName() {
        return getString(RECORD_NAME);
    }

    public void setRecordName(String recordName) {
        put(RECORD_NAME, recordName);
    }

    public int getIconID() {
        return getInt(ICON_ID);
    }

    public void setIconID(int iconID) {
        put(ICON_ID, iconID);
    }


    public final static String USER = "User";
    public final static String ACCOUNT_ID = "AccountID";
    public final static String RECORD_ID = "RecordID";
    public final static String RECORD_NAME = "RecordName";
    public final static String RECORD_MONEY = "RecordMoney";
    public final static String RECORD_TYPE_ID = "RecordTypeID";
    public final static String RECORD_TYPE = "RecordType";
    public final static String RECORD_DATE = "RecordDate";
    public final static String REMARK = "RecordRemark";
    public final static String RECORD_IS_DEL = "IsDel";
    public final static String ICON_ID = "IconID";
}
