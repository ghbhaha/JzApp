package com.suda.jzapp.dao.local.record;

import android.content.Context;
import android.database.Cursor;

import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.greendao.RecordDao;
import com.suda.jzapp.dao.greendao.RemarkTip;
import com.suda.jzapp.dao.greendao.RemarkTipDao;
import com.suda.jzapp.dao.local.BaseLocalDao;
import com.suda.jzapp.manager.domain.ChartRecordDo;
import com.suda.jzapp.manager.domain.MyDate;
import com.suda.jzapp.misc.Constant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ghbha on 2016/3/24.
 */
public class RecordLocalDAO extends BaseLocalDao {

    /**
     * 根据某个消费类型查询频率最高的账户
     *
     * @param context
     * @return
     */
    public long getMoreUseAccountByRecord(Context context, long recordTypeId) {
        String sql = "SELECT ACCOUNT_ID FROM RECORD WHERE RECORD_TYPE_ID = " + recordTypeId
                + " AND IS_DEL=0 GROUP BY ACCOUNT_ID ORDER BY COUNT(ACCOUNT_ID) DESC LIMIT 0,1";
        Cursor c = null;
        long accountId = 0;
        try {
            c = getDaoSession(context).getDatabase().rawQuery(sql, null);
            if (c.moveToFirst()) {
                accountId = c.getLong(0);
            }
        } catch (Exception t) {
            t.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return accountId;
    }

    public void createOrUpdateRecord(Context context, Record record) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        Record recordOld = getSingleData(recordDao.queryBuilder()
                .whereOr(RecordDao.Properties.RecordId.eq(record.getRecordId())
                        , RecordDao.Properties.ObjectID.eq(record.getObjectID())).build().list());
        if (recordOld != null) {
            record.setId(recordOld.getId());
            updateOldRecord(context, record);
        } else {
            createNewRecord(context, record);
        }
    }

    public double getAccountMoneyByRecord(Context context, long accountID) {
        String sql = "SELECT SUM(RECORD_MONEY) FROM RECORD WHERE IS_DEL = 0 "
                + " AND ACCOUNT_ID = '" + accountID + "'";
        double count = 0;
        Cursor c = null;
        try {
            c = getDaoSession(context).getDatabase().rawQuery(sql, null);
            if (c.moveToFirst()) {
                count = c.getDouble(0);
            }
        } catch (Exception t) {
            t.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return count;
    }

    public void createNewRecord(Context context, Record record) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(record.getRecordDate());
        record.setYear(calendar.get(Calendar.YEAR));
        record.setMonth(calendar.get(Calendar.MONTH));
        record.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        recordDao.insert(record);
    }

    public void clearAllRecord(Context context) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        recordDao.deleteAll();
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
                " RECORD_TYPE=-1 and IS_DEL =0 and ACCOUNT_ID=" + accountId + " and RECORD_DATE >= " + startDate.getTime() + " and RECORD_DATE<" + endDate.getTime();
        Cursor c = getDaoSession(context).getDatabase().rawQuery(sql, null);
        c.moveToFirst();
        double todayCost = c.getDouble(0);
        c.close();
        return Math.abs(todayCost);
    }


    public List<MyDate> getRecordDate(Context context, int pageIndex) {
        if (pageIndex == 0)
            pageIndex = 1;
        pageIndex--;


        List<MyDate> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append(Constant.RecordType.SHOURU.getId()).append(",");
        builder.append(Constant.RecordType.ZUICHU.getId()).append(",");
        builder.append(Constant.RecordType.AA_SHOURU.getId()).append(",");
        builder.append(Constant.RecordType.AA_ZHICHU.getId());

        String sql1 = "select YEAR,MONTH FROM RECORD WHERE IS_DEL = 0 " +
                "and RECORD_TYPE in (" + builder.toString() +
                ") GROUP BY YEAR,MONTH ORDER BY YEAR DESC,MONTH DESC limit " + pageIndex + ",1";

        int year = 0;
        int month = 0;
        Cursor c1 = getDaoSession(context).getDatabase().rawQuery(sql1, null);
        if (!c1.moveToFirst()) {
            return list;
        } else {
            year = c1.getInt(0);
            month = c1.getInt(1);
            c1.close();
        }

        String sql2 = "select YEAR,MONTH,DAY from RECORD where IS_DEL = 0 and YEAR ="
                + year + " and MONTH =" + month
                + " and RECORD_TYPE in (" + builder.toString() +
                ") GROUP BY MONTH,DAY ORDER BY MONTH DESC,DAY DESC";

        Cursor c2 = getDaoSession(context).getDatabase().rawQuery(sql2, null);
        while (c2.moveToNext()) {
            list.add(new MyDate(c2.getInt(0), c2.getInt(1), c2.getInt(2)));
        }
        c2.close();

//        String sql = "select RECORD_DATE from RECORD where IS_DEL = 0 and YEAR ="
//                + year + " and MONTH =" + month
//                + " and RECORD_TYPE in (" + builder.toString() +
//                ") GROUP BY RECORD_DATE ORDER BY RECORD_DATE DESC";
//        Cursor c = getDaoSession(context).getDatabase().rawQuery(sql, null);
//        while (c.moveToNext()) {
//            list.add(new Date(c.getLong(0)));
//        }
//        c.close();
        return list;
    }

//    public List<Record> getRecordByDate(Context context, Date date) {
//        RecordDao recordDao = getDaoSession(context).getRecordDao();
//        return recordDao.queryBuilder().where(RecordDao.Properties.IsDel.eq(false))
//                .where(RecordDao.Properties.RecordDate.eq(date))
//                .whereOr(RecordDao.Properties.RecordType.eq(Constant.RecordType.SHOURU.getId()),
//                        RecordDao.Properties.RecordType.eq(Constant.RecordType.AA_SHOURU.getId()),
//                        RecordDao.Properties.RecordType.eq(Constant.RecordType.ZUICHU.getId()),
//                        RecordDao.Properties.RecordType.eq(Constant.RecordType.AA_ZHICHU.getId()))
//                .orderDesc(RecordDao.Properties.Id).list();
//    }

    public List<Record> getRecordByMyDate(Context context, MyDate date) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        return recordDao.queryBuilder().where(RecordDao.Properties.IsDel.eq(false))
                .where(RecordDao.Properties.Year.eq(date.getYear()))
                .where(RecordDao.Properties.Month.eq(date.getMonth()))
                .where(RecordDao.Properties.Day.eq(date.getDay()))
                .whereOr(RecordDao.Properties.RecordType.eq(Constant.RecordType.SHOURU.getId()),
                        RecordDao.Properties.RecordType.eq(Constant.RecordType.AA_SHOURU.getId()),
                        RecordDao.Properties.RecordType.eq(Constant.RecordType.ZUICHU.getId()),
                        RecordDao.Properties.RecordType.eq(Constant.RecordType.AA_ZHICHU.getId()))
                .orderDesc(RecordDao.Properties.Id).list();
    }

    public Record getRecordById(Context context, long recordId) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        return getSingleData(recordDao.queryBuilder().where(RecordDao.Properties.RecordId.eq(recordId)).list());
    }

    public List<Record> getRecordsByRecordTypeIDAndMonth(Context context, long recordTypeID, int recordYear, int recordMonth) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        return recordDao.queryBuilder().where(RecordDao.Properties.IsDel.eq(false))
                .where(RecordDao.Properties.RecordTypeID.eq(recordTypeID))
                .where(RecordDao.Properties.Year.eq(recordYear))
                .where(RecordDao.Properties.Month.eq(recordMonth))
                .orderDesc(RecordDao.Properties.RecordDate)
                .list();
    }

    public void updateOldRecord(Context context, Record record) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(record.getRecordDate());
        record.setYear(calendar.get(Calendar.YEAR));
        record.setMonth(calendar.get(Calendar.MONTH));
        record.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        recordDao.update(record);
    }

    public List<Record> getNotBackData(Context context) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        return recordDao.queryBuilder().where(RecordDao.Properties.SyncStatus.eq(false))
                .list();
    }

    public List<Record> getRecordByMonthAndAccount(Context context, long accountID, long startDate, long endDate) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        return recordDao.queryBuilder()
                .where(RecordDao.Properties.AccountID.eq(accountID))
                .where(RecordDao.Properties.IsDel.eq(false))
                .whereOr(RecordDao.Properties.RecordDate.eq(startDate), RecordDao.Properties.RecordDate.gt(startDate))
                .where(RecordDao.Properties.RecordDate.lt(endDate))
                .orderDesc(RecordDao.Properties.RecordDate)
                .orderDesc(RecordDao.Properties.RecordId)
                .list();
    }

    public List<Record> getRecordByMonth(Context context, long startDate, long endDate) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        return recordDao.queryBuilder()
                .where(RecordDao.Properties.IsDel.eq(false))
                .where(RecordDao.Properties.RecordDate.gt(startDate))
                .where(RecordDao.Properties.RecordDate.lt(endDate))
                .whereOr(RecordDao.Properties.RecordType.eq(Constant.RecordType.ZUICHU.getId()),
                        RecordDao.Properties.RecordType.eq(Constant.RecordType.AA_ZHICHU.getId()))
                .orderDesc(RecordDao.Properties.RecordDate)
                .orderDesc(RecordDao.Properties.RecordId)
                .list();
    }

    public List<ChartRecordDo> getOutOrInRecordByMonth(Context context, boolean out, int year, int month) {
        List<ChartRecordDo> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        if (out) {
            builder.append(Constant.RecordType.AA_ZHICHU.getId()).append(",");
            builder.append(Constant.RecordType.ZUICHU.getId());
        } else {
            builder.append(Constant.RecordType.SHOURU.getId()).append(",");
            builder.append(Constant.RecordType.AA_SHOURU.getId());
        }

        String sql = "select b.[RECORD_TYPE_ID],b.[RECORD_DESC],b.[RECORD_ICON],SUM(a.[RECORD_MONEY]) FROM RECORD a,RECORD_TYPE b " +
                "where a.[RECORD_TYPE_ID] = b.[RECORD_TYPE_ID] and a.[IS_DEL] = 0 and YEAR ="
                + year + " and MONTH =" + month +
                " and b.RECORD_TYPE in (" + builder.toString() +
                ") group by  b.[RECORD_TYPE_ID] order by SUM(a.[RECORD_MONEY]) " + (out ? "asc" : "desc");
        Cursor c = getDaoSession(context).getDatabase().rawQuery(sql, null);
        while (c.moveToNext()) {
            ChartRecordDo chartRecordDo = new ChartRecordDo();
            chartRecordDo.setRecordTypeID(c.getLong(0));
            chartRecordDo.setRecordDesc(c.getString(1));
            chartRecordDo.setIconId(c.getInt(2));
            chartRecordDo.setRecordMoney(c.getDouble(3));
            chartRecordDo.setRecordYear(year);
            chartRecordDo.setRecordMonth(month);
            list.add(chartRecordDo);
        }
        return list;
    }

    public Map<Integer, Double> getYearRecordDetail(Context context, int year, boolean out) {
        return getYearMonthRecordDetail(context, year, -1, out);
    }

    public Map<Integer, Double> getYearMonthRecordDetail(Context context, int year, int month, boolean out) {
        Map<Integer, Double> result = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (out) {
            builder.append(Constant.RecordType.AA_ZHICHU.getId()).append(",");
            builder.append(Constant.RecordType.ZUICHU.getId());
        } else {
            builder.append(Constant.RecordType.SHOURU.getId()).append(",");
            builder.append(Constant.RecordType.AA_SHOURU.getId());
        }

        String sql = "select MONTH,sum(RECORD_MONEY) from RECORD where IS_DEL = 0 and RECORD_TYPE in (" +
                builder.toString() +
                ") and YEAR = " + year +
                (month > -1 ? (" and MONTH =" + month) : "") +
                " GROUP BY MONTH";
        Cursor c = getDaoSession(context).getDatabase().rawQuery(sql, null);
        while (c.moveToNext()) {
            result.put(c.getInt(0), c.getDouble(1));
        }
        c.close();
        return result;
    }

    public void insertNewRemarkTip(Context context, String remark, boolean syncStatus) {
        RemarkTipDao remarkTipDao = getDaoSession(context).getRemarkTipDao();
        RemarkTip remarkTip = new RemarkTip();
        remarkTip.setRemark(remark);
        remarkTip.setIsDel(false);
        remarkTip.setUseTimes(1);
        remarkTip.setSyncStatus(syncStatus);
        remarkTipDao.insert(remarkTip);
    }

    public void updateRemarkTip(Context context, RemarkTip remarkTip, boolean syncStatus) {
        RemarkTipDao remarkTipDao = getDaoSession(context).getRemarkTipDao();
        remarkTipDao.update(remarkTip);
    }

    public RemarkTip selectRemarkTipByRemark(Context context, String remark) {
        RemarkTipDao remarkTipDao = getDaoSession(context).getRemarkTipDao();
        return getSingleData(remarkTipDao.queryBuilder().where(RemarkTipDao.Properties.Remark.eq(remark)).list());
    }

    public void deleteRemarkTip(Context context, long id) {
        RemarkTipDao remarkTipDao = getDaoSession(context).getRemarkTipDao();
        remarkTipDao.queryBuilder().where(RemarkTipDao.Properties.Id.eq(id)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public List<RemarkTip> selectRemarkTips(Context context) {
        RemarkTipDao remarkTipDao = getDaoSession(context).getRemarkTipDao();
        return remarkTipDao.queryBuilder().where(RemarkTipDao.Properties.IsDel.eq(false)).where(RemarkTipDao.Properties.UseTimes.ge(2))
                .orderDesc(RemarkTipDao.Properties.UseTimes)
                .list();
    }

    public int getRecordDayCount(Context context) {
        StringBuilder builder = new StringBuilder();
        builder.append(Constant.RecordType.AA_ZHICHU.getId()).append(",");
        builder.append(Constant.RecordType.ZUICHU.getId()).append(",");
        builder.append(Constant.RecordType.SHOURU.getId()).append(",");
        builder.append(Constant.RecordType.AA_SHOURU.getId());

        String sql = "select YEAR,MONTH,DAY from RECORD where IS_DEL = 0 "
                + " and RECORD_TYPE in (" + builder.toString() +
                ") GROUP BY YEAR,MONTH,DAY";

        int count = 0;
        Cursor c = null;
        try {
            c = getDaoSession(context).getDatabase().rawQuery(sql, null);
            count = c.getCount();
        } catch (Exception t) {

        } finally {
            if (c != null)
                c.close();
        }

        return count;
    }

    public boolean haveRecord(Context context) {
        RecordDao recordDao = getDaoSession(context).getRecordDao();
        return recordDao.queryBuilder().list().size() > 0;
    }


}
