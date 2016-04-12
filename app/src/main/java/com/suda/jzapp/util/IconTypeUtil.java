package com.suda.jzapp.util;

import android.graphics.Color;

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
        return getTypeIconOrColor(iconType, true);
    }

    /**
     * 根据id匹配图标,颜色
     *
     * @param iconType
     * @return
     */
    public static int getTypeIconOrColor(int iconType, boolean icon) {
        switch (iconType) {
            case RecordTypeConstant.ICON_TYPE_YI_BAN:
                return icon ? R.drawable.icon_zhichu_type_yiban : Color.parseColor("#3ea6d6");
            case RecordTypeConstant.ICON_TYPE_CAN_YIN:
                return icon ? R.drawable.icon_zhichu_type_canyin : Color.parseColor("#b5ba3e");
            case RecordTypeConstant.ICON_TYPE_SHUI_GUO_LING_SHI:
                return icon ? R.drawable.icon_zhichu_type_shuiguolingshi : Color.parseColor("#6faa70");
            case RecordTypeConstant.ICON_TYPE_YAN_JIU_YIN_LIAO:
                return icon ? R.drawable.icon_zhichu_type_yanjiuyinliao : Color.parseColor("#ff6b6b");
            case RecordTypeConstant.ICON_TYPE_GOU_WU:
                return icon ? R.drawable.icon_zhichu_type_gouwu : Color.parseColor("#e7c03e");
            case RecordTypeConstant.ICON_TYPE_JIAO_TONG:
                return icon ? R.drawable.icon_zhichu_type_jiaotong : Color.parseColor("#f39c12");
            case RecordTypeConstant.ICON_TYPE_JU_JIA:
                return icon ? R.drawable.icon_zhichu_type_jujia : Color.parseColor("#b36a66");
            case RecordTypeConstant.ICON_TYPE_SHOU_JI_TONG_XUN:
                return icon ? R.drawable.icon_zhichu_type_shoujitongxun : Color.parseColor("#B485B0");
            case RecordTypeConstant.ICON_TYPE_BAO_XIAO_ZHANG:
                return icon ? R.drawable.icon_zhichu_type_baoxiaozhang : Color.parseColor("#6666aa");
            case RecordTypeConstant.ICON_TYPE_JIE_CHU:
                return icon ? R.drawable.icon_zhichu_type_jiechu : Color.parseColor("#d8c367");
            case RecordTypeConstant.ICON_TYPE_YU_LE:
                return icon ? R.drawable.icon_zhichu_type_yule : Color.parseColor("#d6a834");
            case RecordTypeConstant.ICON_TYPE_TAO_BAO:
                return icon ? R.drawable.icon_zhichu_type_taobao : Color.parseColor("#dd6032");
            case RecordTypeConstant.ICON_TYPE_REN_QING_SONG_LI:
                return icon ? R.drawable.icon_zhichu_type_renqingsongli : Color.parseColor("#dba7bc");
            case RecordTypeConstant.ICON_TYPE_YI_LIAO_JIAO_YU:
                return icon ? R.drawable.icon_zhichu_type_yiliaojiaoyu : Color.parseColor("#eb8abe");
            case RecordTypeConstant.ICON_TYPE_SHU_JI:
                return icon ? R.drawable.icon_zhichu_type_shuji : Color.parseColor("#9e7866");
            case RecordTypeConstant.ICON_TYPE_MEI_RONG_JIAN_SHEN:
                return icon ? R.drawable.icon_zhichu_type_meirongjianshen : Color.parseColor("#e2728e");
            case RecordTypeConstant.ICON_TYPE_CHONG_WU:
                return icon ? R.drawable.icon_zhichu_type_chongwu : Color.parseColor("#a1b2a5");
            case RecordTypeConstant.ICON_TYPE_GONG_ZI:
                return icon ? R.drawable.icon_shouru_type_gongzi : Color.parseColor("#be83b7");
            case RecordTypeConstant.ICON_TYPE_JIANG_JIN:
                return icon ? R.drawable.icon_shouru_type_jiangjin : Color.parseColor("#ed9241");
            case RecordTypeConstant.ICON_TYPE_JIAN_ZHI_WAI_KUAI:
                return icon ? R.drawable.icon_shouru_type_jianzhiwaikuai : Color.parseColor("#5fb0c5");
            case RecordTypeConstant.ICON_TYPE_TOU_ZI_SHOU_RU:
                return icon ? R.drawable.icon_shouru_type_touzishouru : Color.parseColor("#cc6603");
            case RecordTypeConstant.ICON_TYPE_LING_HUA_QIAN:
                return icon ? R.drawable.icon_shouru_type_linghuaqian : Color.parseColor("#889174");
            case RecordTypeConstant.ICON_TYPE_SHENG_HUO_FEI:
                return icon ? R.drawable.icon_shouru_type_shenghuofei : Color.parseColor("#d1b59d");
            case RecordTypeConstant.ICON_TYPE_HONG_BAO:
                return icon ? R.drawable.icon_shouru_type_hongbao : Color.parseColor("#e05b27");
            case RecordTypeConstant.ICON_TYPE_QI_TA:
                return icon ? R.drawable.icon_shouru_type_qita : Color.parseColor("#3ea6d6");
            case RecordTypeConstant.ICON_TYPE_JIE_RU:
                return icon ? R.drawable.icon_shouru_type_jieru : Color.parseColor("#b5a353");
            case RecordTypeConstant.ICON_TYPE_ADD_1:
                return icon ? R.drawable.icon_add_1 : Color.parseColor("#8ebcdf");
            case RecordTypeConstant.ICON_TYPE_ADD_2:
                return icon ? R.drawable.icon_add_2 : Color.parseColor("#66A6D1");
            case RecordTypeConstant.ICON_TYPE_ADD_3:
                return icon ? R.drawable.icon_add_3 : Color.parseColor("#26B6DF");
            case RecordTypeConstant.ICON_TYPE_ADD_4:
                return icon ? R.drawable.icon_add_4 : Color.parseColor("#6B83B7");
            case RecordTypeConstant.ICON_TYPE_ADD_5:
                return icon ? R.drawable.icon_add_5 : Color.parseColor("#6797A8");
            case RecordTypeConstant.ICON_TYPE_ADD_6:
                return icon ? R.drawable.icon_add_6 : Color.parseColor("#F0D878");
            case RecordTypeConstant.ICON_TYPE_ADD_7:
                return icon ? R.drawable.icon_add_7 : Color.parseColor("#C0AB58");
            case RecordTypeConstant.ICON_TYPE_ADD_8:
                return icon ? R.drawable.icon_add_8 : Color.parseColor("#EEBB96");
            case RecordTypeConstant.ICON_TYPE_ADD_9:
                return icon ? R.drawable.icon_add_9 : Color.parseColor("#FF7F00");
            case RecordTypeConstant.ICON_TYPE_ADD_10:
                return icon ? R.drawable.icon_add_10 : Color.parseColor("#FF9999");
            case RecordTypeConstant.ICON_TYPE_ADD_11:
                return icon ? R.drawable.icon_add_11 : Color.parseColor("#FF6B6B");
            case RecordTypeConstant.ICON_TYPE_ADD_12:
                return icon ? R.drawable.icon_add_12 : Color.parseColor("#DF3C3C");
            case RecordTypeConstant.ICON_TYPE_ADD_13:
                return icon ? R.drawable.icon_add_13 : Color.parseColor("#FF9AB6");
            case RecordTypeConstant.ICON_TYPE_ADD_14:
                return icon ? R.drawable.icon_add_14 : Color.parseColor("#E880A2");
            case RecordTypeConstant.ICON_TYPE_ADD_15:
                return icon ? R.drawable.icon_add_15 : Color.parseColor("#AD234B");
            case RecordTypeConstant.ICON_TYPE_ADD_16:
                return icon ? R.drawable.icon_add_16 : Color.parseColor("#61D2D6");
            case RecordTypeConstant.ICON_TYPE_ADD_17:
                return icon ? R.drawable.icon_add_17 : Color.parseColor("#9E7866");
            case RecordTypeConstant.ICON_TYPE_ADD_18:
                return icon ? R.drawable.icon_add_18 : Color.parseColor("#B19994");
            case RecordTypeConstant.ICON_TYPE_ADD_19:
                return icon ? R.drawable.icon_add_19 : Color.parseColor("#9C939E");
            case RecordTypeConstant.ICON_TYPE_ADD_20:
                return icon ? R.drawable.icon_add_20 : Color.parseColor("#BFB2BE");
            case RecordTypeConstant.ICON_TYPE_YU_E_BIAN_GENG:
                return icon ? R.drawable.yuegenghuan : Color.parseColor("#CCC259");
        }

        return icon ? R.drawable.icon_shouru_type_qita : Color.parseColor("#3EA6D6");
    }
}
