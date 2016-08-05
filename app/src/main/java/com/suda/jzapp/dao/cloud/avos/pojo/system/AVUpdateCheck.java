package com.suda.jzapp.dao.cloud.avos.pojo.system;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by suda on 2016/8/5.
 */
@AVClassName("UpdateCheckDO")
public class AVUpdateCheck extends AVObject {

    public String getUpdateInfo() {
        return getString(UPDATE_INFO);
    }

    public String getVersion() {
        return getString(VERSION);
    }

    public int getVersionCode() {
        return getInt(VERSION_CODE);
    }

    public String getLink() {
        return getString(LINK);
    }

    public final static String UPDATE_INFO = "updateInfo";
    public final static String VERSION = "version";
    public final static String VERSION_CODE = "versionCode";
    public final static String LINK = "link";

}
