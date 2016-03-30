package com.suda.jzapp.util;


import com.avos.avoscloud.AVObject;
import com.suda.jzapp.dao.cloud.avos.pojo.AVAccount;
import com.suda.jzapp.dao.cloud.avos.pojo.AVRecord;
import com.suda.jzapp.dao.cloud.avos.pojo.AVRecordType;
import com.suda.jzapp.dao.cloud.avos.pojo.MyAVUser;

/**
 * Created by Suda on 2015/9/16.
 */
public class LoadAVOSLib {

    public static void LoadLib() {
        AVObject.registerSubclass(AVRecordType.class);
        AVObject.registerSubclass(AVRecord.class);
        AVObject.registerSubclass(AVAccount.class);
        AVObject.registerSubclass(MyAVUser.class);
    }
}
