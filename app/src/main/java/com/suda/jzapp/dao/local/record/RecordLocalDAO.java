package com.suda.jzapp.dao.local.record;

import android.content.Context;

import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.greendao.RecordDao;
import com.suda.jzapp.dao.local.BaseLocalDao;

/**
 * Created by ghbha on 2016/3/24.
 */
public class RecordLocalDAO extends BaseLocalDao {

    public void createNewRecord(Context context, Record record) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        recordDao.insert(record);
    }

}
