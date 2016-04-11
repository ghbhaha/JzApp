package com.suda.jzapp.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by ghbha on 2016/2/26.
 */
public class TextUtil {
    public static String getFormatMoney(double money) {
        DecimalFormat df;
        if (-1 < money && money < 1)
            df = new DecimalFormat("0.00");
        else
            df = new DecimalFormat("#.00");

        return df.format(money);
    }

    public static double gwtFormatNum(double num) {
        BigDecimal per = new BigDecimal(num);
        return per.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
