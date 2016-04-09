package com.suda.jzapp.util;

import com.suda.jzapp.R;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.Constant.RecordTypeConstant;

/**
 * Created by Suda on 2015/9/18.
 */
public class IconTypeUtil {


    public static int getAccountIcon(int accountTypeID) {
        switch (accountTypeID) {
            case Constant.AccountTypeConstant.ACCOUNT_TYPE_XIAN_JIN:
                return R.drawable.icon_account_xianjin_grey;
            case Constant.AccountTypeConstant.ACCOUNT_TYPE_CHU_XU_KA:
                return R.drawable.icon_account_yinhangka_gray;
            case Constant.AccountTypeConstant.ACCOUNT_TYPE_XIN_YONG_KA:
                return R.drawable.icon_account_xinyongka_gray;
            case Constant.AccountTypeConstant.ACCOUNT_TYPE_WANG_LUO_ZHANG_HU:
                return R.drawable.icon_account_wangluozhanghu_gray;
            case Constant.AccountTypeConstant.ACCOUNT_TYPE_TOU_ZI_ZHANG_HU:
                return R.drawable.icon_account_gupiao_grey;
            case Constant.AccountTypeConstant.ACCOUNT_TYPE_CHU_ZHI_KA:
                return R.drawable.icon_account_chuzhika_grey;
        }

        return 0;
    }


    /**
     * 根据id匹配图标
     *
     * @param iconType
     * @return
     */
    public static int getTypeIcon(int iconType) {
        switch (iconType) {
            case RecordTypeConstant.ICON_TYPE_YI_BAN:
                return R.drawable.icon_zhichu_type_yiban;
            case RecordTypeConstant.ICON_TYPE_CAN_YIN:
                return R.drawable.icon_zhichu_type_canyin;
            case RecordTypeConstant.ICON_TYPE_SHUI_GUO_LING_SHI:
                return R.drawable.icon_zhichu_type_shuiguolingshi;
            case RecordTypeConstant.ICON_TYPE_YAN_JIU_YIN_LIAO:
                return R.drawable.icon_zhichu_type_yanjiuyinliao;
            case RecordTypeConstant.ICON_TYPE_GOU_WU:
                return R.drawable.icon_zhichu_type_gouwu;
            case RecordTypeConstant.ICON_TYPE_JIAO_TONG:
                return R.drawable.icon_zhichu_type_jiaotong;
            case RecordTypeConstant.ICON_TYPE_JU_JIA:
                return R.drawable.icon_zhichu_type_jujia;
            case RecordTypeConstant.ICON_TYPE_SHOU_JI_TONG_XUN:
                return R.drawable.icon_zhichu_type_shoujitongxun;
            case RecordTypeConstant.ICON_TYPE_BAO_XIAO_ZHANG:
                return R.drawable.icon_zhichu_type_baoxiaozhang;
            case RecordTypeConstant.ICON_TYPE_JIE_CHU:
                return R.drawable.icon_zhichu_type_jiechu;
            case RecordTypeConstant.ICON_TYPE_YU_LE:
                return R.drawable.icon_zhichu_type_yule;
            case RecordTypeConstant.ICON_TYPE_TAO_BAO:
                return R.drawable.icon_zhichu_type_taobao;
            case RecordTypeConstant.ICON_TYPE_REN_QING_SONG_LI:
                return R.drawable.icon_zhichu_type_renqingsongli;
            case RecordTypeConstant.ICON_TYPE_YI_LIAO_JIAO_YU:
                return R.drawable.icon_zhichu_type_yiliaojiaoyu;
            case RecordTypeConstant.ICON_TYPE_SHU_JI:
                return R.drawable.icon_zhichu_type_shuji;
            case RecordTypeConstant.ICON_TYPE_MEI_RONG_JIAN_SHEN:
                return R.drawable.icon_zhichu_type_meirongjianshen;
            case RecordTypeConstant.ICON_TYPE_CHONG_WU:
                return R.drawable.icon_zhichu_type_chongwu;
            case RecordTypeConstant.ICON_TYPE_GONG_ZI:
                return R.drawable.icon_shouru_type_gongzi;
            case RecordTypeConstant.ICON_TYPE_JIANG_JIN:
                return R.drawable.icon_shouru_type_jiangjin;
            case RecordTypeConstant.ICON_TYPE_JIAN_ZHI_WAI_KUAI:
                return R.drawable.icon_shouru_type_jianzhiwaikuai;
            case RecordTypeConstant.ICON_TYPE_TOU_ZI_SHOU_RU:
                return R.drawable.icon_shouru_type_touzishouru;
            case RecordTypeConstant.ICON_TYPE_LING_HUA_QIAN:
                return R.drawable.icon_shouru_type_linghuaqian;
            case RecordTypeConstant.ICON_TYPE_SHENG_HUO_FEI:
                return R.drawable.icon_shouru_type_shenghuofei;
            case RecordTypeConstant.ICON_TYPE_HONG_BAO:
                return R.drawable.icon_shouru_type_hongbao;
            case RecordTypeConstant.ICON_TYPE_QI_TA:
                return R.drawable.icon_shouru_type_qita;
            case RecordTypeConstant.ICON_TYPE_JIE_RU:
                return R.drawable.icon_shouru_type_jieru;
            case RecordTypeConstant.ICON_TYPE_ADD_1:
                return R.drawable.icon_add_1;
            case RecordTypeConstant.ICON_TYPE_ADD_2:
                return R.drawable.icon_add_2;
            case RecordTypeConstant.ICON_TYPE_ADD_3:
                return R.drawable.icon_add_3;
            case RecordTypeConstant.ICON_TYPE_ADD_4:
                return R.drawable.icon_add_4;
            case RecordTypeConstant.ICON_TYPE_ADD_5:
                return R.drawable.icon_add_5;
            case RecordTypeConstant.ICON_TYPE_ADD_6:
                return R.drawable.icon_add_6;
            case RecordTypeConstant.ICON_TYPE_ADD_7:
                return R.drawable.icon_add_7;
            case RecordTypeConstant.ICON_TYPE_ADD_8:
                return R.drawable.icon_add_8;
            case RecordTypeConstant.ICON_TYPE_ADD_9:
                return R.drawable.icon_add_9;
            case RecordTypeConstant.ICON_TYPE_ADD_10:
                return R.drawable.icon_add_10;
            case RecordTypeConstant.ICON_TYPE_ADD_11:
                return R.drawable.icon_add_11;
            case RecordTypeConstant.ICON_TYPE_ADD_12:
                return R.drawable.icon_add_12;
            case RecordTypeConstant.ICON_TYPE_ADD_13:
                return R.drawable.icon_add_13;
            case RecordTypeConstant.ICON_TYPE_ADD_14:
                return R.drawable.icon_add_14;
            case RecordTypeConstant.ICON_TYPE_ADD_15:
                return R.drawable.icon_add_15;
            case RecordTypeConstant.ICON_TYPE_ADD_16:
                return R.drawable.icon_add_16;
            case RecordTypeConstant.ICON_TYPE_ADD_17:
                return R.drawable.icon_add_17;
            case RecordTypeConstant.ICON_TYPE_ADD_18:
                return R.drawable.icon_add_18;
            case RecordTypeConstant.ICON_TYPE_ADD_19:
                return R.drawable.icon_add_19;
            case RecordTypeConstant.ICON_TYPE_ADD_20:
                return R.drawable.icon_add_20;
            case RecordTypeConstant.ICON_TYPE_YU_E_BIAN_GENG:
                return R.drawable.yuegenghuan;
        }

        return R.drawable.icon_shouru_type_qita;
    }
}
