package com.suda.jzapp.dao.local.conf;

import android.content.Context;

import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.dao.local.BaseLocalDao;
import com.suda.jzapp.dao.greendao.AccountType;
import com.suda.jzapp.dao.greendao.RecordType;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.Constant.AccountTypeConstant;
import com.suda.jzapp.misc.Constant.RecordTypeConstant;


/**
 * Created by Suda on 2015/11/10.
 */
public class ConfigLocalDao extends BaseLocalDao {


    /**
     * 初始化记录类型表
     *
     * @param context
     */
    public void initRecordType(Context context) {
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 0L, "一般", -1, true, RecordTypeConstant.ICON_TYPE_YI_BAN, 0, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 1L, "餐饮", -1, true, RecordTypeConstant.ICON_TYPE_CAN_YIN, 1, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 2L, "水果零食", -1, true, RecordTypeConstant.ICON_TYPE_SHUI_GUO_LING_SHI, 2, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 3L, "酒水饮料", -1, true, RecordTypeConstant.ICON_TYPE_YAN_JIU_YIN_LIAO, 3, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 4L, "购物", -1, true, RecordTypeConstant.ICON_TYPE_GOU_WU, 4, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 5L, "交通", -1, true, RecordTypeConstant.ICON_TYPE_JIAO_TONG, 5, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 6L, "居家", -1, true, RecordTypeConstant.ICON_TYPE_JU_JIA, 6, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 7L, "手机通讯", -1, true, RecordTypeConstant.ICON_TYPE_SHOU_JI_TONG_XUN, 7, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 8L, "报销账", -1, true, RecordTypeConstant.ICON_TYPE_BAO_XIAO_ZHANG, 8, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 9L, "借出", -1, true, RecordTypeConstant.ICON_TYPE_JIE_CHU, 9, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 10L, "娱乐", -1, true, RecordTypeConstant.ICON_TYPE_YU_LE, 10, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 11L, "淘宝", -1, true, RecordTypeConstant.ICON_TYPE_TAO_BAO, 11, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 12L, "人情礼物", -1, true, RecordTypeConstant.ICON_TYPE_REN_QING_SONG_LI, 12, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 13L, "医疗教育", -1, true, RecordTypeConstant.ICON_TYPE_YI_LIAO_JIAO_YU, 13, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 14L, "书籍", -1, true, RecordTypeConstant.ICON_TYPE_SHU_JI, 14, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 15L, "美容运动", -1, true, RecordTypeConstant.ICON_TYPE_MEI_RONG_JIAN_SHEN, 15, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 16L, "宠物", -1, true, RecordTypeConstant.ICON_TYPE_CHONG_WU, 16, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 17L, "工资", 1, true, RecordTypeConstant.ICON_TYPE_GONG_ZI, 17, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 18L, "奖金", 1, true, RecordTypeConstant.ICON_TYPE_JIANG_JIN, 18, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 19L, "外快兼职", 1, true, RecordTypeConstant.ICON_TYPE_JIAN_ZHI_WAI_KUAI, 19, Constant.Sex.ALL.getId(), Constant.Occupation.STUDENT.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 20L, "投资收入", 1, true, RecordTypeConstant.ICON_TYPE_TOU_ZI_SHOU_RU, 20, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 21L, "零花钱", 1, true, RecordTypeConstant.ICON_TYPE_LING_HUA_QIAN, 21, Constant.Sex.ALL.getId(), Constant.Occupation.STUDENT.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 22L, "生活费", 1, true, RecordTypeConstant.ICON_TYPE_SHENG_HUO_FEI, 22, Constant.Sex.ALL.getId(), Constant.Occupation.STUDENT.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 23L, "红包", 1, true, RecordTypeConstant.ICON_TYPE_HONG_BAO, 23, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 24L, "其他", 1, true, RecordTypeConstant.ICON_TYPE_QI_TA, 24, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 25L, "借入", 1, true, RecordTypeConstant.ICON_TYPE_JIE_RU, 25, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 26L, "转账", 0, true, 26, 26, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 27L, "余额变更", 2, true, 27, 27, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false));
    }

    /**
     * 初始化账户类型表
     *
     * @param context
     */
    public void initAccountTypeDb(Context context) {
        getDaoSession(context).getAccountTypeDao().insert(new AccountType(null, AccountTypeConstant.ACCOUNT_TYPE_XIAN_JIN, "现金", 0));
        getDaoSession(context).getAccountTypeDao().insert(new AccountType(null, AccountTypeConstant.ACCOUNT_TYPE_CHU_XU_KA, "储蓄卡", 1));
        getDaoSession(context).getAccountTypeDao().insert(new AccountType(null, AccountTypeConstant.ACCOUNT_TYPE_XIN_YONG_KA, "信用卡", 2));
        getDaoSession(context).getAccountTypeDao().insert(new AccountType(null, AccountTypeConstant.ACCOUNT_TYPE_WANG_LUO_ZHANG_HU, "网络账户", 3));
        getDaoSession(context).getAccountTypeDao().insert(new AccountType(null, AccountTypeConstant.ACCOUNT_TYPE_TOU_ZI_ZHANG_HU, "投资账户", 4));
        getDaoSession(context).getAccountTypeDao().insert(new AccountType(null, AccountTypeConstant.ACCOUNT_TYPE_CHU_ZHI_KA, "储值卡", 5));
    }

    public void createDefaultAccount(Context context) {
        getDaoSession(context).getAccountDao().insert(new Account(null, System.currentTimeMillis(), 0, "现金", 0.00, "", "", false, false));
    }

    private final static String DB_NAME = "conf.db";

}
