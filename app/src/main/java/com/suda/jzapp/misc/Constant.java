package com.suda.jzapp.misc;

/**
 * Created by Suda on 2015/10/10.
 */
public class Constant {

    public enum Sex {
        BOY(1), GIRL(2), ALL(0);

        private int id;

        Sex(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public enum Occupation {
        STUDENT(1), WORKER(2), ALL(0);

        private int id;

        Occupation(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }


    public enum RecordType {
        SHOURU(1), ZUICHU(-1), ZHUANZHANG(0),
        AA_ZHICHU(-2), AA_SHOURU(2),
        CHANGE(3);
        private int id;

        RecordType(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

    }

    //账户类型
    public static class AccountTypeConstant {
        public final static int ACCOUNT_TYPE_XIAN_JIN = 0;
        public final static int ACCOUNT_TYPE_CHU_XU_KA = 1;
        public final static int ACCOUNT_TYPE_XIN_YONG_KA = 2;
        public final static int ACCOUNT_TYPE_WANG_LUO_ZHANG_HU = 3;
        public final static int ACCOUNT_TYPE_TOU_ZI_ZHANG_HU = 4;
        public final static int ACCOUNT_TYPE_CHU_ZHI_KA = 5;
    }

    //记录类型
    public static class RecordTypeConstant {

        //支出
        public final static int ICON_TYPE_YI_BAN = 0;
        public final static int ICON_TYPE_CAN_YIN = 1;
        public final static int ICON_TYPE_SHUI_GUO_LING_SHI = 2;
        public final static int ICON_TYPE_YAN_JIU_YIN_LIAO = 3;
        public final static int ICON_TYPE_GOU_WU = 4;
        public final static int ICON_TYPE_JIAO_TONG = 5;
        public final static int ICON_TYPE_JU_JIA = 6;
        public final static int ICON_TYPE_SHOU_JI_TONG_XUN = 7;
        public final static int ICON_TYPE_BAO_XIAO_ZHANG = 8;
        public final static int ICON_TYPE_JIE_CHU = 9;
        public final static int ICON_TYPE_YU_LE = 10;
        public final static int ICON_TYPE_TAO_BAO = 11;
        public final static int ICON_TYPE_REN_QING_SONG_LI = 12;
        public final static int ICON_TYPE_YI_LIAO_JIAO_YU = 13;
        public final static int ICON_TYPE_SHU_JI = 14;
        public final static int ICON_TYPE_MEI_RONG_JIAN_SHEN = 15;
        public final static int ICON_TYPE_CHONG_WU = 16;

        //收入
        public final static int ICON_TYPE_GONG_ZI = 17;
        public final static int ICON_TYPE_JIANG_JIN = 18;
        public final static int ICON_TYPE_JIAN_ZHI_WAI_KUAI = 19;
        public final static int ICON_TYPE_TOU_ZI_SHOU_RU = 20;
        public final static int ICON_TYPE_LING_HUA_QIAN = 21;
        public final static int ICON_TYPE_SHENG_HUO_FEI = 22;
        public final static int ICON_TYPE_HONG_BAO = 23;
        public final static int ICON_TYPE_QI_TA = 24;
        public final static int ICON_TYPE_JIE_RU = 25;

        //附加图标
        public final static int ICON_TYPE_ADD_1 = 26;
        public final static int ICON_TYPE_ADD_2 = 27;
        public final static int ICON_TYPE_ADD_3 = 28;
        public final static int ICON_TYPE_ADD_4 = 29;
        public final static int ICON_TYPE_ADD_5 = 30;
        public final static int ICON_TYPE_ADD_6 = 31;
        public final static int ICON_TYPE_ADD_7 = 32;
        public final static int ICON_TYPE_ADD_8 = 33;
        public final static int ICON_TYPE_ADD_9 = 34;
        public final static int ICON_TYPE_ADD_10 = 35;
        public final static int ICON_TYPE_ADD_11 = 36;
        public final static int ICON_TYPE_ADD_12 = 37;
        public final static int ICON_TYPE_ADD_13 = 38;
        public final static int ICON_TYPE_ADD_14 = 39;
        public final static int ICON_TYPE_ADD_15 = 40;
        public final static int ICON_TYPE_ADD_16 = 41;
        public final static int ICON_TYPE_ADD_17 = 42;
        public final static int ICON_TYPE_ADD_18 = 43;
        public final static int ICON_TYPE_ADD_19 = 44;
        public final static int ICON_TYPE_ADD_20 = 45;
        public final static int ICON_TYPE_NONE = 46;
        public final static int ICON_TYPE_YU_E_BIAN_GENG = 47;
        public final static int ICON_AD = 48;
        public final static int ICON_AN_JIE = 49;
        public final static int ICON_BAO_BAO = 50;
        public final static int ICON_BAO_JIAN = 51;
        public final static int ICON_BAO_XIAN = 52;
        public final static int ICON_BAO_XIAO = 53;
        public final static int ICON_CHA_SHUI_KA_FEI = 54;
        public final static int ICON_CHUAN_PIAO = 55;
        public final static int ICON_DAO_YOU = 56;
        public final static int ICON_DA_PAI = 57;
        public final static int ICON_DIAN_FEI = 58;
        public final static int ICON_DIAN_YING = 59;
        public final static int ICON_FANG_DAI = 60;
        public final static int ICON_FANG_ZU = 61;
        public final static int ICON_FAN_KA = 62;
        public final static int ICON_FEI_JI_PIAO = 63;
        public final static int ICON_FU_WU = 64;
        public final static int ICON_GONG_GONG_QI_CHE = 65;
        public final static int ICON_HAI_WAI_DAI_GOU = 66;
        public final static int ICON_HUAN_KUAN = 67;
        public final static int ICON_HUA_ZHAUNG_PIN = 68;
        public final static int ICON_HUO_CHE_PIAO = 69;
        public final static int ICON_HU_WAI_SHE_BEI = 70;
        public final static int ICON_JIU_SHUI = 71;
        public final static int ICON_JUE_CHU = 72;
        public final static int ICON_KU_ZI = 73;
        public final static int ICON_LI_FA = 74;
        public final static int ICON_LING_QIAN = 75;
        public final static int ICON_LING_SHI = 76;
        public final static int ICON_LV_YOU_DU_JIA = 77;
        public final static int ICON_MAI_CAI = 78;
        public final static int ICON_MA_JIANG = 79;
        public final static int ICON_MAO_ZI = 80;
        public final static int ICON_NAI_FEN = 81;
        public final static int ICON_QU_XAIN = 82;
        public final static int ICON_RI_CHANG_YONG_PIN = 83;
        public final static int ICON_SHI_PIN = 84;
        public final static int ICON_SHUI_FEI = 85;
        public final static int ICON_SHU_MA_CAHN_PIN = 86;
        public final static int ICON_SI_JIA_CHE = 87;
        public final static int ICON_TING_CHE_FEI = 88;
        public final static int ICON_TUI_KUAN = 89;
        public final static int ICON_WAN_FAN = 90;
        public final static int ICON_WANG_FEI = 91;
        public final static int ICON_WANG_GOU = 92;
        public final static int ICON_WAN_JU = 93;
        public final static int ICON_WEI_XIU_BAO_XIAO = 94;
        public final static int ICON_WU_YE = 95;
        public final static int ICON_XIAN_JIN = 96;
        public final static int ICON_XIAO_CHI = 97;
        public final static int ICON_XIAO_JING_JIA_ZHANG = 98;
        public final static int ICON_XIE_ZI = 99;
        public final static int ICON_XIN_YONG_KA_HUAN_KUAN = 100;
        public final static int ICON_XUE_FEI = 101;
        public final static int ICON_XI_ZAO = 102;
        public final static int ICON_YAN = 103;
        public final static int ICON_YAO_PIN_FEI = 104;
        public final static int ICON_YI_FU = 105;
        public final static int ICON_YING_HANG_SHOU_XU = 106;
        public final static int ICON_YI_WAI_PO_SUN = 107;
        public final static int ICON_YI_WAI_SUO_DE = 108;
        public final static int ICON_YOU_FEI = 109;
        public final static int ICON_YOU_XI = 110;
        public final static int ICON_YUN_DONG_JIAN_SHEN = 111;
        public final static int ICON_ZAO_CAN = 112;
        public final static int ICON_ZA_WU = 113;
        public final static int ICON_ZHI_FU_BAO = 114;
        public final static int ICON_ZHONG_FAN = 115;
        public final static int ICON_ZHUAN_ZHANG = 116;
        public final static int ICON_ZHU_SU = 117;
        public final static int ICON_ZUO_JI_FEI = 118;
        public final static int ICON_TYPE_COUNT = 119;
    }

    public final static String SP_SYNC_ONLY_WIFI = "sync_only_wifi";
    public final static String SP_GESTURE = "gesture";
    public final static String SP_TIP_ROUND_PIE = "SP_TIP_ROUND_PIE";
    public final static String SP_TIP_ROUND_LINE = "SP_TIP_ROUND_LINE";
    public final static String SP_TIP_ROUND_EDIT_BUDGET = "SP_TIP_ROUND_EDIT_BUDGET";
    public final static String SP_TIP_DONATE = "SP_TIP_DONATE";
    public final static String SP_ALARM_EVERY_DAY = "SP_ALARM_EVERY_DAY";
    public final static String SP_ALARM_TIME = "SP_ALARM_TIME";
    public final static String SP_FIRST_ADD = "SP_FIRST_ADD";
    public final static String SP_LAST_SYNC_AT = "SP_LAST_SYNC_AT";
    public final static String SP_NAV_IMG_TYPE = "SP_NAV_IMG_TYPE";

    public final static int MSG_ERROR = 0;
    public final static int MSG_SUCCESS = 1;


    public final static String[] ZHI_CHU_WORD = {"支出", "花去", "花了", "花费", "消费"};
    public final static String[] SHOU_RU_WORD = {"赚了", "收入", "赚取", "挣了"};

    public final static int VOICE_PARSE_FAIL = 0;
    public final static int VOICE_PARSE_SUCCESS = 1;
    public final static int VOICE_PARSE_NOT_FOUND_RECORD_TYPE = 2;

    public final static String QR_MARK = "JzAPP_";
    public final static String QR_MARK_HAVE_LINK = "JzAPP#";

    public final static double MAX = 99999999.99;
    public final static long TRANSFER_TYPE = 26L;
    public final static long CHANGE_TYPE = 27L;

    public final static boolean newSyncSwitch = false;

    public final static String NAV_IMG= "NAV_IMG.png" ;
}
