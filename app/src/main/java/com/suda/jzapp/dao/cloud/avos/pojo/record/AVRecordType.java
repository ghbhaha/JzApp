package com.suda.jzapp.dao.cloud.avos.pojo.record;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.suda.jzapp.util.DateTimeUtil;

import java.util.Date;

/**
 * Created by Suda on 2015/9/16.
 */

@AVClassName("RecordType")
public class AVRecordType extends AVObject {

    public void setUser(AVUser user) {
        put(USER, user);
    }

    public void setRecordTypeId(long recordTypeId) {
        put(RECORD_TYPE_ID, recordTypeId);
    }

    public void setRecordDesc(String recordDesc) {
        put(RECORD_DESC, recordDesc);
    }

    public void setRecordType(int recordType) {
        put(RECORD_TYPE, recordType);
    }

    public void setRecordIcon(int icon) {
        put(RECORD_ICON, icon);
    }

    public void setIndex(int index) {
        put(INDEX, index);
    }

    public void setRecordTypeIsDel(boolean isDel) {
        put(RECORD_TYPE_IS_DEL, isDel);
    }

    public AVUser getUser() {
        return getAVUser(USER, AVUser.class);
    }


    public long getRecordTypeId() {
        return getLong(RECORD_TYPE_ID);
    }

    public String getRecordDesc() {
        return getString(RECORD_DESC);
    }

    public int getRecordType() {
        return getInt(RECORD_TYPE);
    }

    public int getRecordRecordIcon() {
        return getInt(RECORD_ICON);
    }

    public int getIndex() {
        return getInt(INDEX);
    }

    public boolean isRecordTypeDel() {
        return getBoolean(RECORD_TYPE_IS_DEL);
    }

    public final static String USER = "User";
    public final static String RECORD_TYPE_ID = "RecordTypeID";
    public final static String RECORD_DESC = "RecordDesc";
    public final static String RECORD_TYPE = "RecordType";
    public final static String RECORD_ICON = "RecordIcon";
    public final static String INDEX = "Index";
    public final static String RECORD_TYPE_IS_DEL = "IsDel";
}
