package com.suda.jzapp.util;

import android.graphics.Color;

import com.suda.jzapp.R;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.Constant.RecordTypeConstant;

/**
 * Created by Suda on 2015/9/18.
 */
public class IconTypeUtil {

    /**
     * 获取账户类型icon
     *
     * @param accountTypeID
     * @return
     */
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
            case RecordTypeConstant.ICON_AD:
                return icon ? R.drawable.ad : Color.parseColor("#3F745A");
            case RecordTypeConstant.ICON_AN_JIE:
                return icon ? R.drawable.anjie : Color.parseColor("#3AAB7C");
            case RecordTypeConstant.ICON_BAO_BAO:
                return icon ? R.drawable.baobao : Color.parseColor("#9CC051");
            case RecordTypeConstant.ICON_BAO_JIAN:
                return icon ? R.drawable.baojian : Color.parseColor("#0C9D19");
            case RecordTypeConstant.ICON_BAO_XIAN:
                return icon ? R.drawable.baoxian : Color.parseColor("#008E56");
            case RecordTypeConstant.ICON_BAO_XIAO:
                return icon ? R.drawable.baoxiao : Color.parseColor("#6666AA");
            case RecordTypeConstant.ICON_CHA_SHUI_KA_FEI:
                return icon ? R.drawable.chashuikafei : Color.parseColor("#7F5C1E");
            case RecordTypeConstant.ICON_CHUAN_PIAO:
                return icon ? R.drawable.chuanpiao : Color.parseColor("#68ABB5");
            case RecordTypeConstant.ICON_DAO_YOU:
                return icon ? R.drawable.daoyou : Color.parseColor("#749CA0");
            case RecordTypeConstant.ICON_DA_PAI:
                return icon ? R.drawable.dapai : Color.parseColor("#FE663D");
            case RecordTypeConstant.ICON_DIAN_FEI:
                return icon ? R.drawable.dianfei : Color.parseColor("#E0A902");
            case RecordTypeConstant.ICON_DIAN_YING:
                return icon ? R.drawable.dianying : Color.parseColor("#926550");
            case RecordTypeConstant.ICON_FANG_DAI:
                return icon ? R.drawable.fangdai : Color.parseColor("#A587C4");
            case RecordTypeConstant.ICON_FANG_ZU:
                return icon ? R.drawable.fangzu : Color.parseColor("#AD234B");
            case RecordTypeConstant.ICON_FAN_KA:
                return icon ? R.drawable.fanka : Color.parseColor("#9CD2E6");
            case RecordTypeConstant.ICON_FEI_JI_PIAO:
                return icon ? R.drawable.feijipiao : Color.parseColor("#33475F");
            case RecordTypeConstant.ICON_FU_WU:
                return icon ? R.drawable.fuwu : Color.parseColor("#D05793");
            case RecordTypeConstant.ICON_GONG_GONG_QI_CHE:
                return icon ? R.drawable.gonggongqiche : Color.parseColor("#366FE6");
            case RecordTypeConstant.ICON_HAI_WAI_DAI_GOU:
                return icon ? R.drawable.haiwaidaigou : Color.parseColor("#749CCE");
            case RecordTypeConstant.ICON_HUAN_KUAN:
                return icon ? R.drawable.huankuan : Color.parseColor("#65DFBB");
            case RecordTypeConstant.ICON_HUA_ZHAUNG_PIN:
                return icon ? R.drawable.huazhuangpin : Color.parseColor("#D36AA8");
            case RecordTypeConstant.ICON_HUO_CHE_PIAO:
                return icon ? R.drawable.huochepiao : Color.parseColor("#BC4938");
            case RecordTypeConstant.ICON_HU_WAI_SHE_BEI:
                return icon ? R.drawable.huwaishebei : Color.parseColor("#A6A4DF");
            case RecordTypeConstant.ICON_JIU_SHUI:
                return icon ? R.drawable.jiushui : Color.parseColor("#B42712");
            case RecordTypeConstant.ICON_JUE_CHU:
                return icon ? R.drawable.juechu : Color.parseColor("#AE107D");
            case RecordTypeConstant.ICON_KU_ZI:
                return icon ? R.drawable.kuzi : Color.parseColor("#5085BC");
            case RecordTypeConstant.ICON_LI_FA:
                return icon ? R.drawable.lifa : Color.parseColor("#2672C0");
            case RecordTypeConstant.ICON_LING_QIAN:
                return icon ? R.drawable.lingqian : Color.parseColor("#3B5998");
            case RecordTypeConstant.ICON_LING_SHI:
                return icon ? R.drawable.lingshi : Color.parseColor("#EF417A");
            case RecordTypeConstant.ICON_LV_YOU_DU_JIA:
                return icon ? R.drawable.lvyoudujia : Color.parseColor("#706FAA");
            case RecordTypeConstant.ICON_MAI_CAI:
                return icon ? R.drawable.maicai : Color.parseColor("#5DBF92");
            case RecordTypeConstant.ICON_MA_JIANG:
                return icon ? R.drawable.majiang : Color.parseColor("#056D4E");
            case RecordTypeConstant.ICON_MAO_ZI:
                return icon ? R.drawable.mao : Color.parseColor("#F4D041");
            case RecordTypeConstant.ICON_NAI_FEN:
                return icon ? R.drawable.naifen : Color.parseColor("#4978D0");
            case RecordTypeConstant.ICON_QU_XAIN:
                return icon ? R.drawable.quxian : Color.parseColor("#A66A79");
            case RecordTypeConstant.ICON_RI_CHANG_YONG_PIN:
                return icon ? R.drawable.richangyongpin : Color.parseColor("#09ACE9");
            case RecordTypeConstant.ICON_SHI_PIN:
                return icon ? R.drawable.shipin : Color.parseColor("#CDBA7F");
            case RecordTypeConstant.ICON_SHUI_FEI:
                return icon ? R.drawable.shuifei : Color.parseColor("#47A7E6");
            case RecordTypeConstant.ICON_SHU_MA_CAHN_PIN:
                return icon ? R.drawable.shumachanpin : Color.parseColor("#2CB7BC");
            case RecordTypeConstant.ICON_SI_JIA_CHE:
                return icon ? R.drawable.sijiache : Color.parseColor("#B4CA5C");
            case RecordTypeConstant.ICON_TING_CHE_FEI:
                return icon ? R.drawable.tingchefei : Color.parseColor("#FE463D");
            case RecordTypeConstant.ICON_TUI_KUAN:
                return icon ? R.drawable.tuikuan : Color.parseColor("#BB49C5");
            case RecordTypeConstant.ICON_WAN_FAN:
                return icon ? R.drawable.wanfan : Color.parseColor("#CF0C11");
            case RecordTypeConstant.ICON_WANG_FEI:
                return icon ? R.drawable.wangfei : Color.parseColor("#CC5151");
            case RecordTypeConstant.ICON_WANG_GOU:
                return icon ? R.drawable.wanggou : Color.parseColor("#5C9B8C");
            case RecordTypeConstant.ICON_WAN_JU:
                return icon ? R.drawable.wanju : Color.parseColor("#7F401E");
            case RecordTypeConstant.ICON_WEI_XIU_BAO_XIAO:
                return icon ? R.drawable.weixiubaoyang : Color.parseColor("#1C7998");
            case RecordTypeConstant.ICON_WU_YE:
                return icon ? R.drawable.wuye : Color.parseColor("#826D7B");
            case RecordTypeConstant.ICON_XIAN_JIN:
                return icon ? R.drawable.xianjin : Color.parseColor("#BE7676");
            case RecordTypeConstant.ICON_XIAO_CHI:
                return icon ? R.drawable.xiaochi : Color.parseColor("#4C6F01");
            case RecordTypeConstant.ICON_XIAO_JING_JIA_ZHANG:
                return icon ? R.drawable.xiaojingjiazhang : Color.parseColor("#A2A9CB");
            case RecordTypeConstant.ICON_XIE_ZI:
                return icon ? R.drawable.xiezi : Color.parseColor("#9659AE");
            case RecordTypeConstant.ICON_XIN_YONG_KA_HUAN_KUAN:
                return icon ? R.drawable.xinyongkahuankuan : Color.parseColor("#A3A3A3");
            case RecordTypeConstant.ICON_XUE_FEI:
                return icon ? R.drawable.xuefei : Color.parseColor("#0088B0");
            case RecordTypeConstant.ICON_XI_ZAO:
                return icon ? R.drawable.xizao : Color.parseColor("#9EA66A");
            case RecordTypeConstant.ICON_YAN:
                return icon ? R.drawable.yan : Color.parseColor("#7A6BD6");
            case RecordTypeConstant.ICON_YAO_PIN_FEI:
                return icon ? R.drawable.yaopinfei : Color.parseColor("#D53062");
            case RecordTypeConstant.ICON_YI_FU:
                return icon ? R.drawable.yifu : Color.parseColor("#FD557D");
            case RecordTypeConstant.ICON_YING_HANG_SHOU_XU:
                return icon ? R.drawable.yinhangshouxufei : Color.parseColor("#93ADB7");
            case RecordTypeConstant.ICON_YI_WAI_PO_SUN:
                return icon ? R.drawable.yiwaiposun : Color.parseColor("#F1AD43");
            case RecordTypeConstant.ICON_YI_WAI_SUO_DE:
                return icon ? R.drawable.yiwaisuode : Color.parseColor("#FF7F00");
            case RecordTypeConstant.ICON_YOU_FEI:
                return icon ? R.drawable.youfei : Color.parseColor("#0E5A3A");
            case RecordTypeConstant.ICON_YOU_XI:
                return icon ? R.drawable.youxi : Color.parseColor("#F95318");
            case RecordTypeConstant.ICON_YUN_DONG_JIAN_SHEN:
                return icon ? R.drawable.yundongjianshen : Color.parseColor("#DE9EEE");
            case RecordTypeConstant.ICON_ZAO_CAN:
                return icon ? R.drawable.zaocan : Color.parseColor("#FCB577");
            case RecordTypeConstant.ICON_ZA_WU:
                return icon ? R.drawable.zawu : Color.parseColor("#7C195D");
            case RecordTypeConstant.ICON_ZHI_FU_BAO:
                return icon ? R.drawable.zhifubao : Color.parseColor("#2DC0FC");
            case RecordTypeConstant.ICON_ZHONG_FAN:
                return icon ? R.drawable.zhongfan : Color.parseColor("#FF6C00");
            case RecordTypeConstant.ICON_ZHU_SU:
                return icon ? R.drawable.zhusu : Color.parseColor("#7D6C65");
            case RecordTypeConstant.ICON_ZUO_JI_FEI:
                return icon ? R.drawable.zuojifei : Color.parseColor("#EE8359");
            case RecordTypeConstant.ICON_ZHUAN_ZHANG:
                return icon ? R.drawable.zhuanzhang : Color.parseColor("#DF476C");
        }

        return icon ? R.drawable.icon_shouru_type_qita : Color.parseColor("#3EA6D6");
    }
}
