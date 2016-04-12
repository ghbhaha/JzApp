package com.suda.jzapp.manager.domain;

/**
 * Created by ghbha on 2016/4/12.
 */
public class LineChartDo {
    private int month;
    private double allIn;
    private double allOut;
    private double allLeft;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getAllIn() {
        return allIn;
    }

    public void setAllIn(double allIn) {
        this.allIn = allIn;
    }

    public double getAllOut() {
        return allOut;
    }

    public void setAllOut(double allOut) {
        this.allOut = allOut;
    }

    public double getAllLeft() {
        return allLeft;
    }

    public void setAllLeft(double allLeft) {
        this.allLeft = allLeft;
    }
}
