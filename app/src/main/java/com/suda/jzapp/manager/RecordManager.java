package com.suda.jzapp.manager;

import android.content.Context;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.suda.jzapp.manager.domain.RecordDetailDO;
import com.suda.jzapp.dao.cloud.avos.pojo.record.AVRecord;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.greendao.RecordType;
import com.suda.jzapp.dao.local.record.RecordLocalDAO;
import com.suda.jzapp.dao.local.record.RecordTypeLocalDao;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.text.TextUtils;

/**
 * Created by ghbha on 2016/2/28.
 */
public class RecordManager extends BaseManager {
    public RecordManager(Context context) {
        super(context);
    }

    /**
     * 创建新记录
     *
     * @param record
     */
    public void createNewRecord(final Record record) {
        //1网络创建不成功 SyncStatus 置0
        record.setIsDel(false);
        if (!TextUtils.isEmpty(MyAVUser.getCurrentUserId())) {
            final AVRecord avRecord = new AVRecord();
            avRecord.setAccountId(record.getAccountID());
            avRecord.setRecordDate(record.getRecordDate());
            avRecord.setRecordMoney(record.getRecordMoney());
            avRecord.setRecordTypeId(record.getRecordTypeID());
            avRecord.setRemark(record.getRemark());
            avRecord.setUserId(MyAVUser.getCurrentUserId());
            avRecord.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    record.setSyncStatus(e == null ? true : false);
                    recordDao.createNewRecord(_context, record);
                    getAvEx(e);
                }
            });
        } else {
            record.setSyncStatus(false);
            recordDao.createNewRecord(_context, record);
        }

    }

    public void updateOldRecord(final Record record){
        //1网络创建不成功 SyncStatus 置0
        record.setIsDel(false);
        if (!TextUtils.isEmpty(MyAVUser.getCurrentUserId())) {

        } else {
            record.setSyncStatus(false);
            recordDao.updateOldRecord(_context, record);
        }
    }

    /**
     * 创建新新记录类型
     */
    public void createNewRecordType(final RecordType recordType) {

        //设置索引
//        List<RecordType> recordTypes = getRecordTypeByType(recordType.getRecordType());
//        if (recordTypes.size() == 0) {
//            recordType.setIndex(0);
//        } else {
//            int index = recordTypes.get(recordTypes.size() - 1).getIndex();
//            recordType.setIndex(index + 1);
//        }

        recordType.setIndex(recordTypeDao.getMaxIndexByRecordType(_context, recordType.getRecordType()));

        recordType.setSexProp(Constant.Sex.ALL.getId());
        recordType.setOccupation(Constant.Occupation.ALL.getId());
        recordType.setSysType(false);

        //1网络创建不成功 SyncStatus 置0
        if (!TextUtils.isEmpty(MyAVUser.getCurrentUserId())) {

        } else {
            recordType.setSyncStatus(false);
            recordTypeDao.createNewRecordType(_context, recordType);
        }
    }

    /**
     * 获取收入、支持记录
     */
    public List<RecordType> getRecordTypeByType(final int type) {

        List<RecordType> recordTypes = new ArrayList<RecordType>();
        if (type == Constant.RecordType.SHOURU.getId()) {
            recordTypes.addAll(recordTypeDao.getAllShouRuRecordType(_context));
        } else {
            recordTypes.addAll(recordTypeDao.getAllZhiChuRecordType(_context));
        }
        return recordTypes;

    }

    public RecordType getRecordTypeByID(long id) {
        return recordTypeDao.getRecordTypeById(_context, id);
    }

    public void deleteRecordType(RecordType recordType) {
        //1网络修改不成功 SyncStatus 置0
        if (!TextUtils.isEmpty(MyAVUser.getCurrentUserId())) {

        } else {
            recordType.setSyncStatus(false);
            recordType.setIsDel(true);
            recordTypeDao.updateRecordType(_context, recordType);
        }
    }

    public void updateRecordTypesOrder(List<RecordType> list) {
        recordTypeDao.updateRecordOrder(_context, list);
        //同步部分
    }

    public void getRecordByPageIndex(final int pageIndex, final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                List<Date> dates = recordDao.getRecordDateByPageSize(_context, pageIndex);
                List<RecordDetailDO> recordDetailDos = new ArrayList<>();
                double todayAllInMoney, todayAllOutMoney = 0;
                Map<Long, RecordType> recordTypeMap = new HashMap<>();
                for (Date date : dates) {
                    todayAllInMoney = 0;
                    todayAllOutMoney = 0;
                    RecordDetailDO todayRecordDetail = new RecordDetailDO();
                    todayRecordDetail.setRecordDate(date);
                    recordDetailDos.add(todayRecordDetail);
                    List<Record> records = recordDao.getRecordByDate(_context, date);
                    for (Record record : records) {
                        RecordType recordType = recordTypeMap.get(record.getRecordTypeID());
                        if (recordType == null) {
                            recordType = recordTypeDao.getRecordTypeById(_context, record.getRecordTypeID());
                            recordTypeMap.put(record.getRecordTypeID(), recordType);
                        }
                        RecordDetailDO recordDetailDO = new RecordDetailDO();
                        recordDetailDO.setRecordDate(record.getRecordDate());
                        recordDetailDO.setRecordID(record.getRecordId());
                        recordDetailDO.setRecordMoney(record.getRecordMoney());
                        recordDetailDO.setRemark(record.getRemark());
                        recordDetailDO.setIconId(recordType.getRecordIcon());
                        recordDetailDO.setRecordDesc(recordType.getRecordDesc());
                        if (recordDetailDO.getRecordMoney() > 0)
                            todayAllInMoney += recordDetailDO.getRecordMoney();
                        else
                            todayAllOutMoney += recordDetailDO.getRecordMoney();

                        recordDetailDos.add(recordDetailDO);
                    }
                    todayRecordDetail.setTodayAllInMoney(todayAllInMoney);
                    todayRecordDetail.setTodayAllOutMoney(todayAllOutMoney);
                }

                sendMessage(handler, recordDetailDos, true);
            }
        });

    }

    RecordLocalDAO recordDao = new RecordLocalDAO();
    RecordTypeLocalDao recordTypeDao = new RecordTypeLocalDao();

}
