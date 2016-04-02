package com.suda.jzapp.dao.bean;

/**
 * Created by ghbha on 2016/2/16.
 */
public class AccountDetailDO {
    private Long id;
    private Long AccountID;
    private Integer AccountTypeID;
    private String AccountName;
    private Double AccountMoney;
    private String AccountRemark;
    private String AccountColor;
    private Boolean SyncStatus;
    private Boolean isDel;
    private String AccountDesc;
    private Integer AccountIcon;
    private Double todayCost;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountID() {
        return AccountID;
    }

    public void setAccountID(Long accountID) {
        AccountID = accountID;
    }

    public Integer getAccountTypeID() {
        return AccountTypeID;
    }

    public void setAccountTypeID(Integer accountTypeID) {
        AccountTypeID = accountTypeID;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public Double getAccountMoney() {
        return AccountMoney;
    }

    public void setAccountMoney(Double accountMoney) {
        AccountMoney = accountMoney;
    }

    public String getAccountRemark() {
        return AccountRemark;
    }

    public void setAccountRemark(String accountRemark) {
        AccountRemark = accountRemark;
    }

    public String getAccountColor() {
        return AccountColor;
    }

    public void setAccountColor(String accountColor) {
        AccountColor = accountColor;
    }

    public Boolean getSyncStatus() {
        return SyncStatus;
    }

    public void setSyncStatus(Boolean syncStatus) {
        SyncStatus = syncStatus;
    }

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

    public String getAccountDesc() {
        return AccountDesc;
    }

    public void setAccountDesc(String accountDesc) {
        AccountDesc = accountDesc;
    }

    public Integer getAccountIcon() {
        return AccountIcon;
    }

    public void setAccountIcon(Integer accountIcon) {
        AccountIcon = accountIcon;
    }

    public Double getTodayCost() {
        return todayCost;
    }

    public void setTodayCost(Double todayCost) {
        this.todayCost = todayCost;
    }
}
