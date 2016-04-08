package com.suda.jzapp.view;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suda.jzapp.R;
import com.suda.jzapp.util.ThemeUtil;

/**
 * Created by ghbha on 2016/4/8.
 */
public class MyPreferenceCategory extends PreferenceCategory {

    private Context mContext = null;

    public MyPreferenceCategory(Context context) {
        super(context);
        this.mContext = context;
    }

    public MyPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public MyPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.preference_category_widget, parent, false);
        TextView textView = (TextView) view.findViewById(android.R.id.title);
        textView.setTextColor(mContext.getResources().getColor(ThemeUtil.getTheme(mContext).getMainColorID()));
        return view;
    }
}
