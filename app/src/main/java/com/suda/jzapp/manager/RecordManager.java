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
import com.suda.jzapp.dao.greendao.RemarkTip;
import com.suda.jzapp.dao.local.conf.ConfigLocalDao;
import com.suda.jzapp.dao.local.record.RecordLocalDAO;
import com.suda.jzapp.dao.local.record.RecordTypeLocalDao;
import com.suda.jzapp.manager.domain.AccountDetailDO;
import com.suda.jzapp.manager.domain.ChartRecordDo;
import com.suda.jzapp.manager.domain.LineChartDo;
import com.suda.jzapp.manager.domain.MonthReport;
import com.suda.jzapp.manager.domain.RecordDetailDO;
import com.suda.jzapp.manager.domain.RecordTypeIndexDO;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.DataConvertUtil;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.TextUtil;
import com.suda.jzapp.util.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

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
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(record.getRecordDate());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        record.setRecordDate(calendar.getTime());
        record.setIsDel(false);
        if (canSync()) {
            final AVRecord avRecord = DataConvertUtil.convertRecord2AVRecord(record);
            RecordType recordType = recordTypeDao.getRecordTypeById(_context, avRecord.getRecordTypeId());
            avRecord.setIconID(recordType.getRecordIcon());
            avRecord.setRecordName(recordType.getRecordDesc());
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
            sendEmptyMessage(handler, Constant.MSG_SUCCESS);
        }

        //记录备注
        if (!TextUtils.isEmpty(record.getRemark()) && (!(record.getRecordTypeID() == 27 || record.getRecordTypeID() == 26))) {
            RemarkTip remarkTip = recordLocalDAO.selectRemarkTipByRemark(_context, record.getRemark());
            if (remarkTip == null) {
                recordLocalDAO.insertNewRemarkTip(_context, record.getRemark(), false);
            } else {
                remarkTip.setUseTimes(remarkTip.getUseTimes() + 1);
                recordLocalDAO.updateRemarkTip(_context, remarkTip, false);
            }
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
        if (canSync()) {
            if (!TextUtils.isEmpty(record.getObjectID())) {
                AVRecord avRecord = DataConvertUtil.convertRecord2AVRecord(record);
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
                        AVRecord avRecord = DataConvertUtil.convertRecord2AVRecord(record);
                        if (list.size() > 0) {
                            avRecord.setObjectId(list.get(0).getObjectId());
                        }
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
            sendEmptyMessage(handler, Constant.MSG_SUCCESS);
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
        if (canSync()) {
            final AVRecordType avRecordType = DataConvertUtil.convertRecordType2AVRecordType(recordType);
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

        if (canSync()) {
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
        if (canSync()) {
            if (recordType.getSysType()) {
                //系统类型
                recordTypeDao.updateRecordType(_context, recordType);
                if (recordType.getIsDel()) {
                    updateRecordTypeIndex(null);
                }
            } else {
                //自定义类型
                if (!TextUtils.isEmpty(recordType.getObjectID())) {
                    AVRecordType avRecordType = DataConvertUtil.convertRecordType2AVRecordType(recordType);
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
                            AVRecordType avRecordType = DataConvertUtil.convertRecordType2AVRecordType(recordType);
                            if (list.size() > 0) {
                                avRecordType.setObjectId(list.get(0).getObjectId());
                            }
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
     * 月份作为分页条件查询记录
     *
     * @param pageIndex
     * @param handler
     */
    public void getRecordByPageIndex(final int pageIndex, final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                LogUtils.d("@@@" + pageIndex);
                List<Date> dates = recordLocalDAO.getRecordDate(_context, pageIndex);
                List<RecordDetailDO> recordDetailDos = new ArrayList<>();

                if (dates.size() == 0) {
                    sendMessage(handler, recordDetailDos, true);
                    return;
                }
                RecordDetailDO recordDetailDOMonthFirst = new RecordDetailDO();
                recordDetailDOMonthFirst.setIsFirstDay(true);
                recordDetailDOMonthFirst.setRecordDate(dates.get(0));
                recordDetailDos.add(recordDetailDOMonthFirst);
                double todayAllInMoney, todayAllOutMoney = 0;
                Map<Long, RecordType> recordTypeMap = new HashMap<>();
                for (Date date : dates) {
                    todayAllInMoney = 0;
                    todayAllOutMoney = 0;
                    RecordDetailDO RecordDetailDayFirst = new RecordDetailDO();
                    RecordDetailDayFirst.setIsDayFirstDay(true);
                    RecordDetailDayFirst.setRecordDate(date);
                    recordDetailDos.add(RecordDetailDayFirst);
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
                    RecordDetailDayFirst.setTodayAllInMoney(todayAllInMoney);
                    RecordDetailDayFirst.setTodayAllOutMoney(todayAllOutMoney);
                }
                sendMessage(handler, recordDetailDos, true);
            }
        });

    }

    /**
     * 获取账户当月流水账
     *
     * @param accountID
     * @param startDate
     * @param endDate
     * @param handler
     */
    public void getRecordsByMonthAndAccount(final long accountID, final long startDate, final long endDate, final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {

                Map<String, Object> map = new HashMap<String, Object>();
                List<RecordDetailDO> recordDetailDos = new ArrayList<>();
                Map<Long, RecordType> recordTypeMap = new HashMap<>();
                double outCount = 0;
                double inCount = 0;
                List<Record> records = recordLocalDAO.getRecordByMonthAndAccount(_context, accountID, startDate, endDate);
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
                    if (recordType.getRecordType() == Constant.RecordType.CHANGE.getId()) {
                        recordDetailDO.setIconId(Constant.RecordTypeConstant.ICON_TYPE_YU_E_BIAN_GENG);
                    }

                    recordDetailDO.setRecordDesc(recordType.getRecordDesc());
                    recordDetailDos.add(recordDetailDO);
                    if (record.getRecordMoney() > 0) {
                        inCount += record.getRecordMoney();
                    } else {
                        outCount += record.getRecordMoney();
                    }

                }
                map.put("recordDetailDos", recordDetailDos);
                map.put("inCount", inCount);
                map.put("outCount", Math.abs(outCount));

                sendMessage(handler, map, true);
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

    /**
     * 获取本月收支
     *
     * @param handler
     * @param out
     */
    public void getOutOrInRecordThisMonth(final Handler handler, final boolean out) {
        Calendar calendar = Calendar.getInstance();
        getOutOrInRecordByMonth(handler, out, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
    }

    /**
     * 获取每个月收支
     *
     * @param handler
     * @param out
     * @param year
     * @param month
     */
    public void getOutOrInRecordByMonth(final Handler handler, final boolean out, final int year, final int month) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                List<ChartRecordDo> list = new ArrayList<ChartRecordDo>();
                list.addAll(recordLocalDAO.getOutOrInRecordByMonth(_context, out, year, month));
                double moneyCount = 0;
                for (ChartRecordDo chartRecordDo : list) {
                    moneyCount += chartRecordDo.getRecordMoney();
                }
                for (ChartRecordDo chartRecordDo : list) {
                    chartRecordDo.setPer(chartRecordDo.getRecordMoney() / moneyCount * 100);
                }
                sendMessage(handler, list, true);
            }
        });
    }

    /**
     * 获取某年收支趋势
     *
     * @param year
     * @param handler
     */
    public void getYearRecordDetail(final int year, final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                Map<Integer, Double> out = recordLocalDAO.getYearRecordDetail(_context, year, true);
                Map<Integer, Double> in = recordLocalDAO.getYearRecordDetail(_context, year, false);
                List<LineChartDo> list = new ArrayList<LineChartDo>();
                Calendar calendar = Calendar.getInstance();
                int maxMon = calendar.get(Calendar.YEAR) != year ? 12 : calendar.get(Calendar.MONTH) + 1;

                for (int i = 0; i < maxMon; i++) {
                    double monthIn = 0;
                    double monthOut = 0;
                    if (out.get(i) != null)
                        monthOut = out.get(i);
                    if (in.get(i) != null)
                        monthIn = in.get(i);
                    LineChartDo lineChartDo = new LineChartDo();
                    lineChartDo.setMonth(i + 1);
                    lineChartDo.setAllIn(monthIn);
                    lineChartDo.setAllOut(monthOut);
                    lineChartDo.setAllLeft(monthIn + monthOut);
                    list.add(lineChartDo);
                }
                sendMessage(handler, list, true);
            }
        });
    }

    /**
     * 获取备注提示
     *
     * @param handler
     */
    public void getRemarkTips(final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                List<RemarkTip> remarkTips = recordLocalDAO.selectRemarkTips(_context);
                sendMessage(handler, remarkTips, true);
            }
        });
    }

    /**
     * 转账
     *
     * @param outAccountId
     * @param inAccountId
     * @param money
     * @param handler
     */
    public void moneyTransFer(final long outAccountId, final long inAccountId, final double money, final Handler handler) {
        final AccountDetailDO outAccountDetailDO = accountManager.getAccountByID(outAccountId);
        final AccountDetailDO inAccountDetailDO = accountManager.getAccountByID(inAccountId);

        accountManager.updateAccountMoney(outAccountId, -money, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                accountManager.updateAccountMoney(inAccountId, money, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Record record = new Record();
                        record.setIsDel(false);
                        record.setRemark("转出到" + inAccountDetailDO.getAccountName());
                        record.setRecordId(System.currentTimeMillis());
                        record.setAccountID(outAccountId);
                        record.setRecordType(Constant.RecordType.CHANGE.getId());
                        record.setRecordTypeID(26L);
                        record.setRecordMoney(TextUtil.gwtFormatNum(-money));
                        record.setRecordDate(new Date(System.currentTimeMillis()));
                        createNewRecord(record, new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                                super.handleMessage(msg);
                                Record record = new Record();
                                record.setIsDel(false);
                                record.setRemark("从" + outAccountDetailDO.getAccountName() + "转入");
                                record.setRecordId(System.currentTimeMillis());
                                record.setAccountID(inAccountId);
                                record.setRecordType(Constant.RecordType.CHANGE.getId());
                                record.setRecordTypeID(26L);
                                record.setRecordMoney(TextUtil.gwtFormatNum(money));
                                record.setRecordDate(new Date(System.currentTimeMillis()));
                                createNewRecord(record, new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                                        super.handleMessage(msg);
                                        if (handler != null)
                                            handler.sendEmptyMessage(Constant.MSG_SUCCESS);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    }

    public void getThisMonthReport(final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new TimerTask() {
            @Override
            public void run() {
                Calendar calendar = Calendar.getInstance();
                Map<Integer, Double> in = recordLocalDAO.getYearMonthRecordDetail(_context, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), false);
                Map<Integer, Double> out = recordLocalDAO.getYearMonthRecordDetail(_context, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), true);

                List<ChartRecordDo> chartRecordDos = recordLocalDAO.getOutOrInRecordByMonth(_context, true, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));


                MonthReport monthReport = new MonthReport();
                monthReport.setBudgetMoney(3000.00);
                monthReport.setInMoney(0.00);
                monthReport.setOutMoney(0.00);
                if (in.get(calendar.get(Calendar.MONTH)) != null) {
                    monthReport.setInMoney(in.get(calendar.get(Calendar.MONTH)));
                }
                if (out.get(calendar.get(Calendar.MONTH)) != null) {
                    monthReport.setOutMoney(out.get(calendar.get(Calendar.MONTH)));
                }
                if (chartRecordDos != null && chartRecordDos.size() > 0) {
                    monthReport.setOutMaxType(chartRecordDos.get(0).getRecordDesc());
                    monthReport.setOutMaxMoney(Math.abs(chartRecordDos.get(0).getRecordMoney()));
                }

                sendMessage(handler, monthReport, true);
            }
        });

    }

    public List<RecordDetailDO> getRecordsByRecordTypeIDAndMonth(long recordTypeID, int recordYear, int recordMonth) {
        List<Record> records = recordLocalDAO.getRecordsByRecordTypeIDAndMonth(_context, recordTypeID, recordYear, recordMonth);
        List<RecordDetailDO> recordDetailDOs = new ArrayList<>();
        Map<Long, RecordType> recordTypeMap = new HashMap<>();
        for (Record record : records) {
            RecordType recordType = recordTypeMap.get(record.getRecordTypeID());
            if (recordType == null) {
                recordType = recordTypeDao.getRecordTypeById(_context, record.getRecordTypeID());
                recordTypeMap.put(record.getRecordTypeID(), recordType);
            }
            RecordDetailDO recordDetailDO = new RecordDetailDO();
            recordDetailDO.setRecordDate(record.getRecordDate());
            recordDetailDO.setRecordID(-100);
            recordDetailDO.setRecordMoney(record.getRecordMoney());
            recordDetailDO.setRemark(record.getRemark());
            recordDetailDO.setIconId(recordType.getRecordIcon());
            if (recordType.getRecordType() == Constant.RecordType.CHANGE.getId()) {
                recordDetailDO.setIconId(Constant.RecordTypeConstant.ICON_TYPE_YU_E_BIAN_GENG);
            }

            recordDetailDO.setRecordDesc(recordType.getRecordDesc());
            recordDetailDOs.add(recordDetailDO);
        }
        return recordDetailDOs;
    }


    RecordLocalDAO recordLocalDAO = new RecordLocalDAO();
    RecordTypeLocalDao recordTypeDao = new RecordTypeLocalDao();
    ConfigLocalDao configLocalDao = new ConfigLocalDao();
    AccountManager accountManager = new AccountManager(_context);

    private final static String RECORD_INDEX_UPDATE = "RECORD_INDEX_UPDATE";


}
