package com.suda.jzapp.dao.cloud.avos.pojo.record;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by Suda on 2015/9/16.
 */

@AVClassName("RecordType")
public class AVRecordType extends AVObject {

    public void setUserId(String userId) {
        put(USER_ID, userId);
    }

    public void setRecordTypeId(String recordTypeId) {
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

    public String getUserId() {
        return getString(USER_ID);
    }

    public String getRecordTypeId() {
        return getString(RECORD_TYPE_ID);
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

    public final static String USER_ID = "UserID";
    public final static String RECORD_TYPE_ID = "RecordTypeID";
    public final static String RECORD_DESC = "RecordDesc";
    public final static String RECORD_TYPE = "RecordType";
    public final static String RECORD_ICON = "RecordIcon";
    public final static String INDEX = "Index";
}
