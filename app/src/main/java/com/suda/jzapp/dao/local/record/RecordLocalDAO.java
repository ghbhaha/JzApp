package com.suda.jzapp.dao.local.record;

import android.content.Context;
import android.database.Cursor;

import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.greendao.RecordDao;
import com.suda.jzapp.dao.local.BaseLocalDao;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ghbha on 2016/3/24.
 */
public class RecordLocalDAO extends BaseLocalDao {

    public void createNewRecord(Context context, Record record) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        recordDao.insert(record);
    }

    public double countTodayCostByAccountId(Context context, long accountId) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DATE, 1);
        Date endDate = calendar.getTime();
        String sql = "select sum(RECORD_MONEY) as todayCost from RECORD a,RECORD_TYPE b where a.RECORD_TYPE_ID = b.RECORD_TYPE_ID" +
                " and b.RECORD_TYPE=-1 and a.ACCOUNT_ID=" + accountId + " and a.RECORD_DATE between " + startDate.getTime() + " and " + endDate.getTime();
        Cursor c = getDaoSession(context).getDatabase().rawQuery(sql, null);
        c.moveToFirst();
        return Math.abs(c.getDouble(0));
    }

}
