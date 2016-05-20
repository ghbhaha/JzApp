package com.suda.jzapp.manager.domain;

import com.suda.jzapp.dao.greendao.RecordType;

/**
 * Created by ghbha on 2016/5/20.
 */
public class VoiceDo {

    private int resultCode;
    private String splitStr;
    private double money;
    private RecordType recordTypeDo;

    public String getSplitStr() {
        return splitStr;
    }

    public void setSplitStr(String splitStr) {
        this.splitStr = splitStr;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public RecordType getRecordTypeDo() {
        return recordTypeDo;
    }

    public void setRecordTypeDo(RecordType recordTypeDo) {
        this.recordTypeDo = recordTypeDo;
    }
}
