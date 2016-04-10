package com.suda.jzapp.manager.domain;

import java.util.Date;

/**
 * Created by ghbha on 2016/4/4.
 */
public class RecordDetailDO {

    private long recordID;
    private double recordMoney;
    private double todayAllInMoney;
    private double todayAllOutMoney;
    private Date recordDate;
    private int iconId;
    private String Remark;
    private String recordDesc;
    private boolean isMonthFirstDay;
    private boolean isDayFirstDay;

    public long getRecordID() {
        return recordID;
    }

    public void setRecordID(long recordID) {
        this.recordID = recordID;
    }

    public double getRecordMoney() {
        return recordMoney;
    }

    public void setRecordMoney(double recordMoney) {
        this.recordMoney = recordMoney;
    }

    public double getTodayAllInMoney() {
        return todayAllInMoney;
    }

    public void setTodayAllInMoney(double todayAllInMoney) {
        this.todayAllInMoney = todayAllInMoney;
    }

    public double getTodayAllOutMoney() {
        return todayAllOutMoney;
    }

    public void setTodayAllOutMoney(double todayAllOutMoney) {
        this.todayAllOutMoney = todayAllOutMoney;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getRecordDesc() {
        return recordDesc;
    }

    public void setRecordDesc(String recordDesc) {
        this.recordDesc = recordDesc;
    }

    public boolean isMonthFirstDay() {
        return isMonthFirstDay;
    }

    public void setIsFirstDay(boolean isFirstDay) {
        this.isMonthFirstDay = isFirstDay;
    }

    public boolean isDayFirstDay() {
        return isDayFirstDay;
    }

    public void setIsDayFirstDay(boolean isDayFirstDay) {
        this.isDayFirstDay = isDayFirstDay;
    }

}
