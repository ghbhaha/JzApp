package com.suda.jzapp.dao.local.conf;

import android.content.Context;

import com.suda.jzapp.dao.greendao.Account;
import com.suda.jzapp.dao.greendao.AccountType;
import com.suda.jzapp.dao.greendao.Config;
import com.suda.jzapp.dao.greendao.ConfigDao;
import com.suda.jzapp.dao.greendao.RecordType;
import com.suda.jzapp.dao.local.BaseLocalDao;
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
                new RecordType(null, 0L, "一般", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_YI_BAN, 0, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 1L, "餐饮", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_CAN_YIN, 1, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 2L, "水果零食", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_SHUI_GUO_LING_SHI, 2, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 3L, "酒水饮料", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_YAN_JIU_YIN_LIAO, 3, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 4L, "购物", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_GOU_WU, 4, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 5L, "交通", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_JIAO_TONG, 5, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 6L, "居家", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_JU_JIA, 6, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 7L, "手机通讯", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_SHOU_JI_TONG_XUN, 7, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 8L, "报销账", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_BAO_XIAO_ZHANG, 8, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 9L, "借出", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_JIE_CHU, 9, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 10L, "娱乐", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_YU_LE, 10, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 11L, "淘宝", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_TAO_BAO, 11, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 12L, "人情礼物", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_REN_QING_SONG_LI, 12, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 13L, "医疗教育", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_YI_LIAO_JIAO_YU, 13, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 14L, "书籍", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_SHU_JI, 14, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 15L, "美容运动", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_MEI_RONG_JIAN_SHEN, 15, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 16L, "宠物", Constant.RecordType.ZUICHU.getId(), true, RecordTypeConstant.ICON_TYPE_CHONG_WU, 16, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 17L, "工资", Constant.RecordType.SHOURU.getId(), true, RecordTypeConstant.ICON_TYPE_GONG_ZI, 17, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 18L, "奖金", Constant.RecordType.SHOURU.getId(), true, RecordTypeConstant.ICON_TYPE_JIANG_JIN, 18, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 19L, "外快兼职", Constant.RecordType.SHOURU.getId(), true, RecordTypeConstant.ICON_TYPE_JIAN_ZHI_WAI_KUAI, 19, Constant.Sex.ALL.getId(), Constant.Occupation.STUDENT.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 20L, "投资收入", Constant.RecordType.SHOURU.getId(), true, RecordTypeConstant.ICON_TYPE_TOU_ZI_SHOU_RU, 20, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 21L, "零花钱", Constant.RecordType.SHOURU.getId(), true, RecordTypeConstant.ICON_TYPE_LING_HUA_QIAN, 21, Constant.Sex.ALL.getId(), Constant.Occupation.STUDENT.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 22L, "生活费", Constant.RecordType.SHOURU.getId(), true, RecordTypeConstant.ICON_TYPE_SHENG_HUO_FEI, 22, Constant.Sex.ALL.getId(), Constant.Occupation.STUDENT.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 23L, "红包", Constant.RecordType.SHOURU.getId(), true, RecordTypeConstant.ICON_TYPE_HONG_BAO, 23, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 24L, "其他", Constant.RecordType.SHOURU.getId(), true, RecordTypeConstant.ICON_TYPE_QI_TA, 24, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 25L, "借入", Constant.RecordType.SHOURU.getId(), true, RecordTypeConstant.ICON_TYPE_JIE_RU, 25, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 26L, "转账", Constant.RecordType.ZHUANZHANG.getId(), true, 26, 26, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        getDaoSession(context).getRecordTypeDao().insert(
                new RecordType(null, 27L, "余额变更", Constant.RecordType.CHANGE.getId(), true, RecordTypeConstant.ICON_TYPE_YU_E_BIAN_GENG, 27, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        //  getDaoSession(context).getRecordTypeDao().insert(
        //          new RecordType(null, 28L, "AA", Constant.RecordType.AA_ZHICHU.getId(), true, 29, 28, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));
        //  getDaoSession(context).getRecordTypeDao().insert(
        //         new RecordType(null, 29L, "AA", Constant.RecordType.AA_SHOURU.getId(), true, 29, 29, Constant.Sex.ALL.getId(), Constant.Occupation.ALL.getId(), true, false, ""));


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
        getDaoSession(context).getAccountDao().insert(new Account(null, System.currentTimeMillis(), 0, "现金", 0.00, "", "", false, false, "", 0));
    }

    public Config getConfigByKey(String key, Context context) {
        ConfigDao configDao = getDaoSession(context).getConfigDao();
        return getSingleData(configDao.queryBuilder().where(ConfigDao.Properties.Key.eq(key)).list());

    }

    public void updateConfig(Config config, Context context) {
        ConfigDao configDao = getDaoSession(context).getConfigDao();
        if (config.getId() != null) {
            configDao.update(config);
        } else {
            configDao.insert(config);
        }
    }
}
