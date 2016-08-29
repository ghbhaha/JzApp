package com.suda.jzapp.util;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by ghbha on 2016/4/7.
 */
public class SnackBarUtil {
    public static void showSnackInfo(View parentView, Context context, String msg) {
        if (context == null)
            return;
        Snackbar snackbar = Snackbar.make(parentView, msg, Snackbar.LENGTH_SHORT)
                .setAction("Action", null);
        Snackbar.SnackbarLayout ve = (Snackbar.SnackbarLayout) snackbar.getView();
        ve.setBackgroundColor(context.getResources().getColor(ThemeUtil.getTheme(context).getMainColorID()));
        snackbar.show();
    }
}
