package com.suda.jzapp.dao.cloud.avos.pojo.user;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.PaasClient;

import java.lang.reflect.Field;

/**
 * Created by Suda on 2015/10/9.
 */

@AVClassName("MyAVUser")
public class MyAVUser extends AVUser {


    /**
     * fix 退出应用后获取用户信息不全问题
     * @return
     */
    public static MyAVUser getCurrentUser() {
        try {
            PaasClient paasClient = PaasClient.storageInstance();
            Field currentUser = PaasClient.class.getDeclaredField("currentUser");
            currentUser.setAccessible(true);
            AVUser avUser = (AVUser) currentUser.get(paasClient);
            if (avUser != null
                    && avUser.getSessionToken() == null) {
                currentUser.set(paasClient, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getCurrentUser(MyAVUser.class);
    }

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
