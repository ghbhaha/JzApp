package com.suda.jzapp.util;

import android.content.Context;

import com.suda.jzapp.R;

/**
 * Created by suda on 2016/8/7.
 */
public class MoneyUtil {

    public static String getFormatMoney(Context context, double money) {
        return "" + String.format(context.getResources().getString(R.string.record_money_format), money);
    }
}
