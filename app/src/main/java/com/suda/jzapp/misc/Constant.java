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
        AA_ZHICHU(-2),AA_SHOURU(2),
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

        public final static int ICON_TYPE_COUNT = 46;
    }

    public final static int MSG_ERROR = 0;
    public final static int MSG_SUCCESS = 1;
}
