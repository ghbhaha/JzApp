package com.suda.jzapp.manager.domain;

/**
 * Created by ghbha on 2016/2/14.
 */
public class OptDO {
    private int id;
    private int icon;
    private String tltle;
    private Class act;

    public OptDO(Class act, int id, int icon, String tltle) {
        this.act = act;
        this.id = id;
        this.icon = icon;
        this.tltle = tltle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Class getAct() {
        return act;
    }

    public void setAct(Class act) {
        this.act = act;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTltle() {
        return tltle;
    }

    public void setTltle(String tltle) {
        this.tltle = tltle;
    }
}
