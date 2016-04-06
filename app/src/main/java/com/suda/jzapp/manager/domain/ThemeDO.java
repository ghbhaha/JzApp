package com.suda.jzapp.manager.domain;

/**
 * Created by ghbha on 2016/2/14.
 */
public class ThemeDO {


    private int themeID;
    private int mainColorID;
    private int mainDarkColorID;
    private int textColorID;

    public ThemeDO(int themeID, int mainColorID, int mainDarkColorID, int textColorID) {
        this.themeID = themeID;
        this.mainColorID = mainColorID;
        this.mainDarkColorID = mainDarkColorID;
        this.textColorID = textColorID;
    }

    public int getTextColorID() {
        return textColorID;
    }

    public void setTextColorID(int textColorID) {
        this.textColorID = textColorID;
    }

    public int getThemeID() {
        return themeID;
    }

    public void setThemeID(int themeID) {
        this.themeID = themeID;
    }

    public int getMainColorID() {
        return mainColorID;
    }

    public void setMainColorID(int mainColorID) {
        this.mainColorID = mainColorID;
    }

    public int getMainDarkColorID() {
        return mainDarkColorID;
    }

    public void setMainDarkColorID(int mainDarkColorID) {
        this.mainDarkColorID = mainDarkColorID;
    }
}
