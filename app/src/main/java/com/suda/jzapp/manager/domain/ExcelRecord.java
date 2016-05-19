package com.suda.jzapp.manager.domain;

import java.util.Date;

/**
 * Created by ghbha on 2016/5/19.
 */
public class ExcelRecord {
    private Date recordDate;
    private String recordAccount;
    private String recordName;
    private String remark;
    private double recordMoney;

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getRecordAccount() {
        return recordAccount;
    }

    public void setRecordAccount(String recordAccount) {
        this.recordAccount = recordAccount;
    }

    public String getRecordName() {
        return recordName;
    }

    public void setRecordName(String recordName) {
        this.recordName = recordName;
    }

    public double getRecordMoney() {
        return recordMoney;
    }

    public void setRecordMoney(double recordMoney) {
        this.recordMoney = recordMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
