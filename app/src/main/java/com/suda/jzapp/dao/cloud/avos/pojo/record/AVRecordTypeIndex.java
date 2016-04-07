package com.suda.jzapp.dao.cloud.avos.pojo.record;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;

/**
 * Created by ghbha on 2016/4/7.
 */
@AVClassName("RecordTypeIndex")
public class AVRecordTypeIndex extends AVObject {

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

    public final static String USER = "User";
    public final static String DATA = "data";
}
