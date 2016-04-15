package com.suda.jzapp.manager.domain;

/**
 * Created by ghbha on 2016/4/15.
 */
public class MonthReport {
    private double inMoney;
    private double outMoney;
    private double budgetMoney;

    private String outMaxType;
    private double outMaxMoney;

    public Double getInMoney() {
        return inMoney;
    }

    public void setInMoney(Double inMoney) {
        this.inMoney = inMoney;
    }

    public Double getOutMoney() {
        return outMoney;
    }

    public void setOutMoney(Double outMoney) {
        this.outMoney = outMoney;
    }

    public Double getBudgetMoney() {
        return budgetMoney;
    }

    public void setBudgetMoney(Double budgetMoney) {
        this.budgetMoney = budgetMoney;
    }

    public void setInMoney(double inMoney) {
        this.inMoney = inMoney;
    }

    public void setOutMoney(double outMoney) {
        this.outMoney = outMoney;
    }

    public void setBudgetMoney(double budgetMoney) {
        this.budgetMoney = budgetMoney;
    }

    public String getOutMaxType() {
        return outMaxType;
    }

    public void setOutMaxType(String outMaxType) {
        this.outMaxType = outMaxType;
    }

    public double getOutMaxMoney() {
        return outMaxMoney;
    }

    public void setOutMaxMoney(double outMaxMoney) {
        this.outMaxMoney = outMaxMoney;
    }
}
