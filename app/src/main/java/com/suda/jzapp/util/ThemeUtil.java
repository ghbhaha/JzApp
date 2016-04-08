package com.suda.jzapp.util;

import android.content.Context;

import com.suda.jzapp.R;
import com.suda.jzapp.manager.domain.ThemeDO;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Suda on 2015/10/10.
 */
public class ThemeUtil {

    public static void setThemeColor(Context context, int themeID) {
        SPUtils.put(context, "themeID", themeID);

    }

    public static ThemeDO getTheme(Context context) {
        return map.get(SPUtils.get(context, "themeID", 0));
    }

    static Map<Integer, ThemeDO> map = new HashMap<>();

    static {
        map.put(0, new ThemeDO(0, R.color.activated_blue, R.color.dark_blue, R.color.activated_blue));
        map.put(1, new ThemeDO(0, R.color.activated_brown, R.color.dark_brown, R.color.activated_brown));
        map.put(2, new ThemeDO(0, R.color.activated_cyan, R.color.dark_cyan, R.color.activated_cyan));
        map.put(3, new ThemeDO(0, R.color.activated_dark, R.color.dark_dark, R.color.activated_dark));
        map.put(4, new ThemeDO(0, R.color.activated_deep_purple, R.color.dark_purple, R.color.activated_deep_purple));
        map.put(5, new ThemeDO(0, R.color.activated_green, R.color.dark_green, R.color.activated_green));
        map.put(6, new ThemeDO(0, R.color.activated_indigo, R.color.dark_indigo, R.color.activated_indigo));
        map.put(7, new ThemeDO(0, R.color.activated_orange, R.color.dark_orange, R.color.activated_orange));
        map.put(8, new ThemeDO(0, R.color.activated_red, R.color.dark_red, R.color.activated_red));
    }

    public static int getAppStyleId(Context context) {
        int theme = (int) SPUtils.get(context, "themeID", 0);
        switch (theme) {
            case 0:
                return R.style.AppTheme_BLUE;
            case 1:
                return R.style.AppTheme_BROWN;
            case 2:
                return R.style.AppTheme_CYAN;
            case 3:
                return R.style.AppTheme_DRAK;
            case 4:
                return R.style.AppTheme_DEEP_PURPLE;
            case 5:
                return R.style.AppTheme_GREEN;
            case 6:
                return R.style.AppTheme_INDIGO;
            case 7:
                return R.style.AppTheme_ORANGE;
            case 8:
                return R.style.AppTheme_RED;
            default:
                return R.style.AppTheme;
        }

    }

}
