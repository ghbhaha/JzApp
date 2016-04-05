package com.suda.jzapp.dao.local.record;

import android.content.Context;

import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.greendao.RecordDao;
import com.suda.jzapp.dao.greendao.RecordType;
import com.suda.jzapp.dao.greendao.RecordTypeDao;
import com.suda.jzapp.dao.local.BaseLocalDao;
import com.suda.jzapp.misc.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ghbha on 2016/2/25.
 */
public class RecordTypeLocalDao extends BaseLocalDao {


    public void createNewRecordType(Context context, RecordType recordType) {
        RecordTypeDao recordTypeDao = getDaoSession(context).getRecordTypeDao();
        recordTypeDao.insert(recordType);
    }

    public List<RecordType> getAllZhiChuRecordType(Context context) {
        RecordTypeDao recordTypeDao = getDaoSession(context).getRecordTypeDao();
        return recordTypeDao.queryBuilder().
                where(RecordTypeDao.Properties.IsDel.eq(false))
                .whereOr(RecordTypeDao.Properties.RecordType.eq(Constant.RecordType.ZUICHU.getId()),
                        RecordTypeDao.Properties.RecordType.eq(Constant.RecordType.AA_ZHICHU.getId()))
                .orderAsc(RecordTypeDao.Properties.Index)
                .build()
                .list();
    }

    public List<RecordType> getAllShouRuRecordType(Context context) {
        RecordTypeDao recordTypeDao = getDaoSession(context).getRecordTypeDao();
        return recordTypeDao.queryBuilder().
                where(RecordTypeDao.Properties.IsDel.eq(false))
                .whereOr(RecordTypeDao.Properties.RecordType.eq(Constant.RecordType.SHOURU.getId()),
                        RecordTypeDao.Properties.RecordType.eq(Constant.RecordType.AA_SHOURU.getId()))
                .orderAsc(RecordTypeDao.Properties.Index)
                .build()
                .list();
    }

    public RecordType getRecordTypeById(Context context, long id) {
        RecordTypeDao recordTypeDao = getDaoSession(context).getRecordTypeDao();
        return getSingleData(recordTypeDao.queryBuilder()
                .where(RecordTypeDao.Properties.RecordTypeID.eq(id))
                .build()
                .list());
    }

    public void updateRecordOrder(Context context, List<RecordType> list) {
        RecordTypeDao recordTypeDao = getDaoSession(context).getRecordTypeDao();
        recordTypeDao.queryBuilder().where(RecordTypeDao.Properties.RecordType.eq(list.get(0).getRecordType()))
                .where(RecordTypeDao.Properties.IsDel.eq(false))
                .buildDelete().executeDeleteWithoutDetachingEntities();
        //去除加号
        list.remove(list.size() - 1);

        int i = 0;
        //待同步
        for (RecordType recordType : list) {
            recordType.setSyncStatus(false);
            recordType.setIndex(i);
            i++;
        }

        recordTypeDao.insertOrReplaceInTx(list);
        list.add(new RecordType());

    }

    public int getMaxIndexByRecordType(Context context, int type) {
        RecordTypeDao recordTypeDao = getDaoSession(context).getRecordTypeDao();
        RecordType recordType = getSingleData(recordTypeDao.queryBuilder().where(RecordTypeDao.Properties.RecordType.eq(type))
                .where(RecordTypeDao.Properties.IsDel.eq(false))
                .orderDesc(RecordTypeDao.Properties.Index).limit(1).list());

        return recordType == null ? 0 : recordType.getIndex();
    }

    public void updateRecordType(Context context, RecordType recordType) {
        RecordTypeDao recordTypeDao = getDaoSession(context).getRecordTypeDao();
        recordTypeDao.update(recordType);
    }
}
