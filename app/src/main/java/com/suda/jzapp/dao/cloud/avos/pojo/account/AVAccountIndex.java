package com.suda.jzapp.dao.cloud.avos.pojo.account;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.suda.jzapp.util.DateTimeUtil;

import java.util.Date;

/**
 * Created by ghbha on 2016/4/7.
 */
@AVClassName("AccountIndex")
public class AVAccountIndex extends AVObject {

    public void setData(String data) {
        put(DATA, data);
    }

    public String getData() {
        return getString(DATA);
    }

    public void setUser(AVUser user) {
        put(USER, user);
    }

    public AVUser getUser() {
        return getAVUser(USER, AVUser.class);
    }

    public void setUpdatedAt(Date date) {
        put(AVObject.UPDATED_AT, DateTimeUtil.fmCQLDate(date));
    }

    public final static String USER = "User";
    public final static String DATA = "data";
}
