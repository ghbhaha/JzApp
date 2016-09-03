package com.suda.jzapp.dao.cloud.avos.pojo.account;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.util.DateTimeUtil;

import java.util.Date;

/**
 * Created by Suda on 2015/9/16.
 */

@AVClassName("Account")
public class AVAccount extends AVObject {

    public void setUser(AVUser user) {
        put(USER, user);
    }

    public void setAccountId(long accountId) {
        put(ACCOUNT_ID, accountId);
    }

    public void setAccountTypeId(int accountType) {
        put(ACCOUNT_TYPE_ID, accountType);
    }

    public void setAccountColor(String accountColor) {
        put(ACCOUNT_COLOR, accountColor);
    }

    public void setAccountName(String accountName) {
        put(ACCOUNT_NAME, accountName);
    }

    public void setAccountMoney(double money) {
        put(ACCOUNT_MONEY, money);
    }

    public void setAccountRemark(String accountRemark) {
        put(ACCOUNT_REMARK, accountRemark);
    }

    public void setAccountIsDel(boolean isDel) {
        put(ACCOUNT_IS_DEL, isDel);
    }

    public AVUser getUser() {
        return getAVUser(USER, AVUser.class);
    }

    public long getAccountId() {
        return getLong(ACCOUNT_ID);
    }

    public int getAccountTypeId() {
        return getInt(ACCOUNT_TYPE_ID);
    }

    public String getAccountColor() {
        return getString(ACCOUNT_COLOR);
    }

    public String getAccountName() {
        return getString(ACCOUNT_NAME);
    }

    public double getAccountMoney() {
        return getDouble(ACCOUNT_MONEY);
    }

    public String getAccountRemark() {
        return getString(ACCOUNT_REMARK);
    }

    public boolean isAccountDel() {
        return getBoolean(ACCOUNT_IS_DEL);
    }

    public void setUpdatedAt(Date date) {
        put(AVObject.UPDATED_AT, DateTimeUtil.fmCQLDate(date));
    }

    public final static String USER = "User";
    public final static String ACCOUNT_ID = "AccountID";
    public final static String ACCOUNT_TYPE_ID = "AccountTypeID";
    public final static String ACCOUNT_COLOR = "AccountColor";
    public final static String ACCOUNT_NAME = "AccountName";
    public final static String ACCOUNT_MONEY = "AccountMoney";
    public final static String ACCOUNT_REMARK = "AccountRemark";
    public final static String ACCOUNT_IS_DEL = "IsDel";
}
