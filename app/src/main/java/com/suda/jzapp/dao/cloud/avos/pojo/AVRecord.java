package com.suda.jzapp.dao.cloud.avos.pojo;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.Date;

/**
 * Created by Suda on 2015/9/16.
 */

@AVClassName("Record")
public class AVRecord extends AVObject {

    public void setUserId(String userId) {
        put(USER_ID, userId);
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

    public long getAccountId() {
        return getLong(ACCOUNT_ID);
    }

    public String getUserId() {
        return getString(USER_ID);
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

    public final static String USER_ID = "UserID";
    public final static String ACCOUNT_ID = "AccountID";
    public final static String RECORD_TYPE_ID = "RecordTypeID";
    public final static String RECORD_MONEY = "RecordMoney";
    public final static String RECORD_DATE = "RecordDate";
    public final static String REMARK = "Remark";

}
