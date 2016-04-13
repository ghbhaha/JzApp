package com.suda.jzapp.dao.cloud.avos.pojo.user;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
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

    public AVFile getHeadImage() {
        return getAVFile(HEAD_IMAGE);
    }

    public void setHeadImage(AVFile value) {
        put(HEAD_IMAGE, value);
    }

    public Long getUserCode(){
        return getLong(USER_CODE);
    }

    public final static String HEAD_IMAGE = "headImage";
    public final static String USER_CODE = "userCode";
}
