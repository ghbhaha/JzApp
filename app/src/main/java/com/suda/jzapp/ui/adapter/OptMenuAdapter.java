package com.suda.jzapp.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suda.jzapp.R;
import com.suda.jzapp.manager.domain.OptDO;
import com.suda.jzapp.ui.activity.MainActivity;
import com.suda.jzapp.ui.activity.system.EditThemeActivity;

import java.util.List;

/**
 * Created by ghbha on 2016/2/14.
 */
public class OptMenuAdapter extends BaseAdapter {
    private Activity context;
    private LayoutInflater mInflater;
    private List<OptDO> optDOs;

    public OptMenuAdapter(List<OptDO> optDOs, Activity context) {
        this.optDOs = optDOs;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return optDOs.size();
    }

    @Override
    public Object getItem(int position) {
        return optDOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;


        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.opt_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final OptDO optDO = optDOs.get(position);

        holder.icon.setBackgroundResource(optDO.getIcon());
        holder.title.setText(optDO.getTltle());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optDO.getAct() == null) {
                    context.finish();
                } else {
                    Intent intent = new Intent(context, optDO.getAct());
                    if (optDO.getAct() == EditThemeActivity.class) {
                        ((MainActivity)context).startActivityForResult(intent,MainActivity.REQUEST_EDIT_THEME);
                    } else
                        context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView title;
        public ImageView icon;
    }
}
