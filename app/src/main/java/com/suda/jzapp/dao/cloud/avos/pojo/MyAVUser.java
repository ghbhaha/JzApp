package com.suda.jzapp.dao.cloud.avos.pojo;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVUser;

/**
 * Created by Suda on 2015/10/9.
 */

@AVClassName("MyAVUser")
public class MyAVUser extends AVUser {

    public static String getCurrentUserId() {
        if (getCurrentUser() == null) {
            return "";
        } else {
            return getCurrentUser().getObjectId();
        }
    }
}
