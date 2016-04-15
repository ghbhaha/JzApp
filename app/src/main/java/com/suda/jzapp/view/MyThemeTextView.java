package com.suda.jzapp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.suda.jzapp.util.ThemeUtil;

/**
 * Created by ghbha on 2016/4/15.
 */
public class MyThemeTextView extends TextView {
    public MyThemeTextView(Context context) {
        super(context);
        setTextColor(context.getResources().getColor(ThemeUtil.getTheme(context).getMainColorID()));
    }

    public MyThemeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextColor(context.getResources().getColor(ThemeUtil.getTheme(context).getMainColorID()));
    }


}
