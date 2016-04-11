package com.suda.jzapp.manager.domain;

/**
 * Created by ghbha on 2016/4/10.
 */
public class ChartRecordDo {

    private double recordMoney;
    private String recordDesc;
    private int iconId;
    private Long RecordTypeID;
    private double per;

    public double getRecordMoney() {
        return recordMoney;
    }

    public void setRecordMoney(double recordMoney) {
        this.recordMoney = recordMoney;
    }

    public String getRecordDesc() {
        return recordDesc;
    }

    public void setRecordDesc(String recordDesc) {
        this.recordDesc = recordDesc;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public Long getRecordTypeID() {
        return RecordTypeID;
    }

    public void setRecordTypeID(Long recordTypeID) {
        RecordTypeID = recordTypeID;
    }

    public double getPer() {
        return per;
    }

    public void setPer(double per) {
        this.per = per;
    }
}
