package com.suda.jzapp.dao.local.record;

import android.content.Context;
import android.database.Cursor;

import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.greendao.RecordDao;
import com.suda.jzapp.dao.local.BaseLocalDao;
import com.suda.jzapp.misc.Constant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ghbha on 2016/3/24.
 */
public class RecordLocalDAO extends BaseLocalDao {

    public void createNewRecord(Context context, Record record) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(record.getRecordDate());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        record.setRecordDate(calendar.getTime());

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
        String sql = "select sum(RECORD_MONEY) as todayCost from RECORD where " +
                " RECORD_TYPE=-1 and ACCOUNT_ID=" + accountId + " and RECORD_DATE between " + startDate.getTime() + " and " + endDate.getTime();
        Cursor c = getDaoSession(context).getDatabase().rawQuery(sql, null);
        c.moveToFirst();
        double todayCost = c.getDouble(0);
        c.close();
        return Math.abs(todayCost);
    }


    public List<Date> getRecordDateByPageSize(Context context, int pageIndex) {
        int pageSize = 5;
        if (pageIndex == 0)
            pageIndex = 1;
        int start = (pageIndex - 1) * pageSize;
        List<Date> list = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        builder.append(Constant.RecordType.SHOURU.getId()).append(",");
        builder.append(Constant.RecordType.ZUICHU.getId()).append(",");
        builder.append(Constant.RecordType.AA_SHOURU.getId()).append(",");
        builder.append(Constant.RecordType.AA_ZHICHU.getId());

        String sql = "select RECORD_DATE from RECORD where IS_DEL = 0 and RECORD_TYPE in (" + builder.toString() +
                ") GROUP BY RECORD_DATE ORDER BY RECORD_DATE DESC limit " + start + "," + pageSize + "";
        Cursor c = getDaoSession(context).getDatabase().rawQuery(sql, null);
        while (c.moveToNext()) {
            list.add(new Date(c.getLong(0)));
        }
        c.close();
        return list;
    }

    public List<Record> getRecordByDate(Context context, Date date) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        return recordDao.queryBuilder().where(RecordDao.Properties.IsDel.eq(false))
                .where(RecordDao.Properties.RecordDate.eq(date))
                .orderDesc(RecordDao.Properties.Id).list();
    }

    public Record getRecordById(Context context, long recordId) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        return getSingleData(recordDao.queryBuilder().where(RecordDao.Properties.Id.eq(recordId)).list());
    }
}
