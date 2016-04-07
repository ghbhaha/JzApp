package com.suda.jzapp.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.suda.jzapp.dao.cloud.avos.pojo.record.AVRecord;
import com.suda.jzapp.dao.cloud.avos.pojo.record.AVRecordType;
import com.suda.jzapp.dao.cloud.avos.pojo.record.AVRecordTypeIndex;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.Config;
import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.dao.greendao.RecordType;
import com.suda.jzapp.dao.local.conf.ConfigLocalDao;
import com.suda.jzapp.dao.local.record.RecordLocalDAO;
import com.suda.jzapp.dao.local.record.RecordTypeLocalDao;
import com.suda.jzapp.manager.domain.RecordDetailDO;
import com.suda.jzapp.manager.domain.RecordTypeIndexDO;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void createNewRecord(final Record record, final Handler handler) {
        //1网络创建不成功 SyncStatus 置0
        record.setIsDel(false);
        if (!TextUtils.isEmpty(MyAVUser.getCurrentUserId())) {
            final AVRecord avRecord = new AVRecord();
            avRecord.setUser(MyAVUser.getCurrentUser());
            avRecord.setRecordId(record.getRecordId());
            avRecord.setAccountId(record.getAccountID());
            avRecord.setRecordDate(record.getRecordDate());
            avRecord.setRecordMoney(record.getRecordMoney());
            avRecord.setRecordTypeId(record.getRecordTypeID());
            avRecord.setRecordType(record.getRecordType());
            avRecord.setRemark(record.getRemark());
            avRecord.setRecordIsDel(false);
            avRecord.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    record.setSyncStatus(e == null ? true : false);
                    recordLocalDAO.createNewRecord(_context, record);
                    getAvEx(e);
                    sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                }
            });
        } else {
            record.setSyncStatus(false);
            recordLocalDAO.createNewRecord(_context, record);
        }

    }

    /**
     * 修改记录
     *
     * @param record
     * @param handler
     */
    public void updateOldRecord(final Record record, final Handler handler) {
        //1网络创建不成功 SyncStatus 置0
        if (!TextUtils.isEmpty(MyAVUser.getCurrentUserId())) {
            if (!TextUtils.isEmpty(record.getObjectID())) {
                AVRecord avRecord = new AVRecord();
                avRecord.setObjectId(record.getObjectID());
                avRecord.setUser(MyAVUser.getCurrentUser());
                avRecord.setRecordId(record.getRecordId());
                avRecord.setAccountId(record.getAccountID());
                avRecord.setRecordDate(record.getRecordDate());
                avRecord.setRecordMoney(record.getRecordMoney());
                avRecord.setRecordTypeId(record.getRecordTypeID());
                avRecord.setRecordType(record.getRecordType());
                avRecord.setRemark(record.getRemark());
                avRecord.setRecordIsDel(record.getIsDel());
                avRecord.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            record.setSyncStatus(true);
                            recordLocalDAO.updateOldRecord(_context, record);
                        } else {
                            record.setSyncStatus(false);
                            recordLocalDAO.updateOldRecord(_context, record);
                        }
                        sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                        getAvEx(e);
                    }
                });
                return;
            }

            AVQuery<AVRecord> query = AVObject.getQuery(AVRecord.class);
            query.whereEqualTo(AVRecord.RECORD_ID, record.getRecordId());
            query.whereEqualTo(AVRecord.USER, MyAVUser.getCurrentUser());
            query.findInBackground(new FindCallback<AVRecord>() {
                @Override
                public void done(List<AVRecord> list, AVException e) {
                    if (e == null) {
                        AVRecord avRecord = null;
                        if (list.size() > 0) {
                            avRecord = list.get(0);
                        } else {
                            avRecord = new AVRecord();
                        }
                        avRecord.setUser(MyAVUser.getCurrentUser());
                        avRecord.setRecordId(record.getRecordId());
                        avRecord.setAccountId(record.getAccountID());
                        avRecord.setRecordDate(record.getRecordDate());
                        avRecord.setRecordMoney(record.getRecordMoney());
                        avRecord.setRecordTypeId(record.getRecordTypeID());
                        avRecord.setRecordType(record.getRecordType());
                        avRecord.setRemark(record.getRemark());
                        avRecord.setRecordIsDel(record.getIsDel());
                        avRecord.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    record.setSyncStatus(true);
                                    recordLocalDAO.updateOldRecord(_context, record);
                                } else {
                                    record.setSyncStatus(false);
                                    recordLocalDAO.updateOldRecord(_context, record);
                                }
                                if (handler != null)
                                    handler.sendEmptyMessage(Constant.MSG_SUCCESS);
                                getAvEx(e);
                            }
                        });
                    } else {
                        record.setSyncStatus(false);
                        recordLocalDAO.updateOldRecord(_context, record);
                        if (handler != null)
                            handler.sendEmptyMessage(Constant.MSG_SUCCESS);
                        getAvEx(e);
                    }
                }
            });
        } else {
            record.setSyncStatus(false);
            recordLocalDAO.updateOldRecord(_context, record);
            if (handler != null)
                handler.sendEmptyMessage(Constant.MSG_SUCCESS);
        }
    }

    /**
     * 创建新新记录类型
     */
    public void createNewRecordType(final RecordType recordType, final Handler handler) {

        //设置索引
        recordType.setIndex(recordTypeDao.getMaxIndexByRecordType(_context, recordType.getRecordType()));

        recordType.setSexProp(Constant.Sex.ALL.getId());
        recordType.setOccupation(Constant.Occupation.ALL.getId());
        recordType.setSysType(false);

        //1网络创建不成功 SyncStatus 置0
        if (!TextUtils.isEmpty(MyAVUser.getCurrentUserId())) {
            final AVRecordType avRecordType = new AVRecordType();
            avRecordType.setRecordType(recordType.getRecordType());
            avRecordType.setRecordTypeId(recordType.getRecordTypeID());
            avRecordType.setUser(MyAVUser.getCurrentUser());
            avRecordType.setRecordIcon(recordType.getRecordIcon());
            avRecordType.setIndex(recordType.getIndex());
            avRecordType.setRecordDesc(recordType.getRecordDesc());
            avRecordType.setRecordTypeIsDel(false);
            avRecordType.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    getAvEx(e);
                    if (e == null) {
                        recordType.setSyncStatus(true);
                    } else {
                        recordType.setSyncStatus(false);
                    }
                    recordTypeDao.createNewRecordType(_context, recordType);
                    updateRecordTypeIndex(handler);
                }
            });
        } else {
            recordType.setSyncStatus(false);
            recordTypeDao.createNewRecordType(_context, recordType);
            handler.sendEmptyMessage(Constant.MSG_SUCCESS);
        }
    }

    @Deprecated
    public synchronized void updateRecordTypeIndex(final Handler handler) {
        updateRecordTypeIndex(handler, false);
    }

    /**
     * 更新记录类型排序
     *
     * @param handler
     * @param serviceSync
     */
    public synchronized void updateRecordTypeIndex(final Handler handler, boolean serviceSync) {

        if (!TextUtils.isEmpty(MyAVUser.getCurrentUserId())) {
            Config config = configLocalDao.getConfigByKey(RECORD_INDEX_UPDATE, _context);
            if (config != null && "true".equals(config.getValue()) && serviceSync) {
                sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                return;
            }

            AVQuery<AVRecordTypeIndex> query = AVObject.getQuery(AVRecordTypeIndex.class);
            query.whereEqualTo(AVRecordType.USER, MyAVUser.getCurrentUser());
            query.findInBackground(new FindCallback<AVRecordTypeIndex>() {
                @Override
                public void done(List<AVRecordTypeIndex> list, AVException e) {
                    getAvEx(e);
                    if (e != null) {
                        sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                        Config config = configLocalDao.getConfigByKey(RECORD_INDEX_UPDATE, _context);
                        if (config == null) {
                            config = new Config();
                        }
                        config.setKey(RECORD_INDEX_UPDATE);
                        config.setValue("false");
                        configLocalDao.updateConfig(config, _context);
                    } else {
                        AVRecordTypeIndex avRecordTypeIndex = null;
                        if (list.size() > 0) {
                            avRecordTypeIndex = list.get(0);
                        } else {
                            avRecordTypeIndex = new AVRecordTypeIndex();
                        }
                        avRecordTypeIndex.setUser(MyAVUser.getCurrentUser());
                        avRecordTypeIndex.setData(recordTypeDao.getRecordTypeIndexInfo(_context));
                        avRecordTypeIndex.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                getAvEx(e);
                                Config config = configLocalDao.getConfigByKey(RECORD_INDEX_UPDATE, _context);
                                if (config == null) {
                                    config = new Config();
                                }
                                config.setKey(RECORD_INDEX_UPDATE);
                                if (e == null) {
                                    config.setValue("true");
                                } else {
                                    config.setValue("false");
                                }
                                configLocalDao.updateConfig(config, _context);
                                sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                            }
                        });
                    }
                }
            });
        }
    }

    /**
     * 更新记录类型
     *
     * @param recordType
     * @param handler
     */
    public void updateRecordType(final RecordType recordType, final Handler handler) {
        //1网络修改不成功 SyncStatus 置0
        if (!TextUtils.isEmpty(MyAVUser.getCurrentUserId())) {
            if (recordType.getSysType()) {
                //系统类型
                recordTypeDao.updateRecordType(_context, recordType);
                if (recordType.getIsDel()) {
                    updateRecordTypeIndex(null);
                }
            } else {
                //自定义类型
                if (!TextUtils.isEmpty(recordType.getObjectID())) {
                    AVRecordType avRecordType = new AVRecordType();
                    avRecordType.setObjectId(recordType.getObjectID());
                    avRecordType.setRecordType(recordType.getRecordType());
                    avRecordType.setRecordTypeId(recordType.getRecordTypeID());
                    avRecordType.setUser(MyAVUser.getCurrentUser());
                    avRecordType.setRecordIcon(recordType.getRecordIcon());
                    avRecordType.setIndex(recordType.getIndex());
                    avRecordType.setRecordDesc(recordType.getRecordDesc());
                    avRecordType.setRecordTypeIsDel(recordType.getIsDel());
                    avRecordType.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            recordType.setSysType(true);
                            recordTypeDao.updateRecordType(_context, recordType);
                            sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                            getAvEx(e);
                        }
                    });
                    return;
                }

                AVQuery<AVRecordType> query = AVObject.getQuery(AVRecordType.class);
                query.whereEqualTo(AVRecordType.USER, MyAVUser.getCurrentUser());
                query.whereEqualTo(AVRecordType.RECORD_TYPE_ID, recordType.getRecordTypeID());
                query.findInBackground(new FindCallback<AVRecordType>() {
                    @Override
                    public void done(List<AVRecordType> list, AVException e) {
                        if (e == null) {
                            AVRecordType avRecordType = null;
                            if (list.size() > 0) {
                                avRecordType = list.get(0);
                            } else {
                                avRecordType = new AVRecordType();
                            }
                            avRecordType.setRecordType(recordType.getRecordType());
                            avRecordType.setRecordTypeId(recordType.getRecordTypeID());
                            avRecordType.setUser(MyAVUser.getCurrentUser());
                            avRecordType.setRecordIcon(recordType.getRecordIcon());
                            avRecordType.setIndex(recordType.getIndex());
                            avRecordType.setRecordDesc(recordType.getRecordDesc());
                            avRecordType.setRecordTypeIsDel(recordType.getIsDel());
                            avRecordType.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    recordType.setSysType(true);
                                    recordTypeDao.updateRecordType(_context, recordType);
                                    sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                                    getAvEx(e);
                                }
                            });
                        } else {
                            recordType.setSysType(false);
                            recordTypeDao.updateRecordType(_context, recordType);
                            sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                        }
                        getAvEx(e);
                    }
                });
            }
        } else {
            if (!recordType.getSysType())
                recordType.setSyncStatus(false);
            recordTypeDao.updateRecordType(_context, recordType);
            if (recordType.getIsDel()) {
                updateRecordTypeIndex(null);
            }
            sendEmptyMessage(handler, Constant.MSG_SUCCESS);
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

    /**
     * 根据id获取记录类型
     *
     * @param id
     * @return
     */
    public RecordType getRecordTypeByID(long id) {
        return recordTypeDao.getRecordTypeById(_context, id);
    }

    /**
     * 更新排序
     *
     * @param list
     */
    public void updateRecordTypesOrder(List<RecordType> list) {
        recordTypeDao.updateRecordOrder(_context, list);
        //同步部分
        updateRecordTypeIndex(null);
    }

    /**
     * 时间作为分页条件查询记录
     *
     * @param pageIndex
     * @param handler
     */
    public void getRecordByPageIndex(final int pageIndex, final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                List<Date> dates = recordLocalDAO.getRecordDateByPageSize(_context, pageIndex);
                List<RecordDetailDO> recordDetailDos = new ArrayList<>();
                double todayAllInMoney, todayAllOutMoney = 0;
                Map<Long, RecordType> recordTypeMap = new HashMap<>();
                for (Date date : dates) {
                    todayAllInMoney = 0;
                    todayAllOutMoney = 0;
                    RecordDetailDO todayRecordDetail = new RecordDetailDO();
                    todayRecordDetail.setRecordDate(date);
                    recordDetailDos.add(todayRecordDetail);
                    List<Record> records = recordLocalDAO.getRecordByDate(_context, date);
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

    /**
     * 初始化从云端加载记录数据
     *
     * @param handler
     */
    public void initRecordData(final Handler handler) {
        AVQuery<AVRecord> query = AVObject.getQuery(AVRecord.class);
        query.whereEqualTo(AVRecord.USER, MyAVUser.getCurrentUser());
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                int count = i / PAGE_SIZE + 1;
                appendRecord(handler, 1, count);
            }
        });
    }

    /**
     * 分页查询全部数据
     *
     * @param handler
     * @param pageIndex
     * @param pageCount
     */
    private void appendRecord(final Handler handler, final int pageIndex, final int pageCount) {
        AVQuery<AVRecord> query = AVObject.getQuery(AVRecord.class);
        query.whereEqualTo(AVRecord.USER, MyAVUser.getCurrentUser());
        query.skip((pageIndex - 1) * PAGE_SIZE);
        query.limit(PAGE_SIZE);
        query.findInBackground(new FindCallback<AVRecord>() {
            @Override
            public void done(List<AVRecord> list, AVException e) {
                if (e == null) {
                    for (AVRecord avRecord : list) {
                        Record record = new Record();
                        record.setObjectID(avRecord.getObjectId());
                        record.setAccountID(avRecord.getAccountId());
                        record.setRecordId(avRecord.getRecordId());
                        record.setRecordType(avRecord.getRecordType());
                        record.setRecordTypeID(avRecord.getRecordTypeId());
                        record.setRecordDate(avRecord.getRecordDate());
                        record.setIsDel(avRecord.isRecordDel());
                        record.setRecordMoney(avRecord.getRecordMoney());
                        record.setRemark(avRecord.getRemark());
                        record.setSyncStatus(true);
                        recordLocalDAO.createNewRecord(_context, record);
                    }

                    if (pageIndex < pageCount) {
                        appendRecord(handler, pageIndex + 1, pageCount);
                    } else {
                        sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                    }
                } else {
                    sendEmptyMessage(handler, Constant.MSG_ERROR);
                }
            }
        });
    }

    /**
     * 初始化记录类型数据
     *
     * @param handler
     */
    public void initRecordTypeData(final Handler handler) {
        AVQuery<AVRecordType> query = AVObject.getQuery(AVRecordType.class);
        query.whereEqualTo(AVRecordType.USER, MyAVUser.getCurrentUser());
        query.limit(1000);
        query.findInBackground(new FindCallback<AVRecordType>() {
            @Override
            public void done(List<AVRecordType> list, AVException e) {
                Message message = new Message();
                if (e == null) {
                    if (list.size() > 0) {
                        recordTypeDao.clearAllRecordType(_context);
                        configLocalDao.initRecordType(_context);
                        for (AVRecordType avRecordType : list) {
                            RecordType recordType = new RecordType();
                            recordType.setObjectID(avRecordType.getObjectId());
                            recordType.setRecordTypeID(avRecordType.getRecordTypeId());
                            recordType.setRecordType(avRecordType.getRecordType());
                            recordType.setIsDel(avRecordType.isRecordTypeDel());
                            recordType.setIndex(avRecordType.getIndex());
                            recordType.setRecordIcon(avRecordType.getRecordRecordIcon());
                            recordType.setSexProp(Constant.Sex.ALL.getId());
                            recordType.setSysType(false);
                            recordType.setOccupation(Constant.Occupation.ALL.getId());
                            recordType.setSyncStatus(true);
                            recordType.setRecordDesc(avRecordType.getRecordDesc());
                            recordTypeDao.createNewRecordType(_context, recordType);
                        }
                    }
                    initRecordTypeIndex(handler);
                    return;
                } else {
                    message.what = Constant.MSG_ERROR;
                    getAvEx(e);
                }
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 初始化记录类型索引数据
     *
     * @param handler
     */
    public void initRecordTypeIndex(final Handler handler) {
        AVQuery<AVRecordTypeIndex> query = AVObject.getQuery(AVRecordTypeIndex.class);
        query.whereEqualTo(AVRecordTypeIndex.USER, MyAVUser.getCurrentUser());
        query.findInBackground(new FindCallback<AVRecordTypeIndex>() {
            @Override
            public void done(List<AVRecordTypeIndex> list, AVException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        AVRecordTypeIndex avRecordTypeIndex = list.get(0);
                        String data = avRecordTypeIndex.getData();
                        List<RecordTypeIndexDO> recordTypeIndexDOs = JSON.parseArray(data, RecordTypeIndexDO.class);
                        recordTypeDao.update2DelSysType(_context);
                        for (RecordTypeIndexDO recordTypeIndexDO : recordTypeIndexDOs) {
                            recordTypeDao.updateRecordTypeIndex(_context, recordTypeIndexDO);
                        }
                    }
                    sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                } else {
                    sendEmptyMessage(handler, Constant.MSG_ERROR);
                    getAvEx(e);
                }
            }
        });
    }

    RecordLocalDAO recordLocalDAO = new RecordLocalDAO();
    RecordTypeLocalDao recordTypeDao = new RecordTypeLocalDao();
    ConfigLocalDao configLocalDao = new ConfigLocalDao();

    private final static String RECORD_INDEX_UPDATE = "RECORD_INDEX_UPDATE";
}
