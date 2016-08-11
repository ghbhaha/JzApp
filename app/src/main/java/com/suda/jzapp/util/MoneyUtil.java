package com.suda.jzapp.util;

import android.content.Context;

import com.suda.jzapp.R;

import java.math.BigDecimal;

/**
 * Created by suda on 2016/8/7.
 */
public class MoneyUtil {

    public static String getFormatMoney(Context context, double money) {
        return "" + getFormatNumStr(context, money);
    }

    public static String getFormatNumStr(Context context, double num) {
        return String.format(context.getResources().getString(R.string.record_money_format), getFormatNum(num));
    }

    public static double getFormatNum(double num) {
        BigDecimal per = new BigDecimal(num);
        return per.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
