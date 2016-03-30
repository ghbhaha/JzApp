package com.suda.jzapp.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.suda.jzapp.R;
import com.suda.jzapp.util.IconTypeUtil;

import java.util.List;

/**
 * Created by ghbha on 2016/2/25.
 */
public class RecordTypeIconAdapter extends BaseAdapter {

    public RecordTypeIconAdapter(Context context, List<Integer> recordTypes) {
        super();
        this.recordTypes = recordTypes;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return recordTypes.size();
    }

    @Override
    public Object getItem(int position) {
        return recordTypes.get(position);
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
            convertView = mInflater.inflate(R.layout.item_record_type_icon, null);

            holder.icon = (ImageView) convertView.findViewById(R.id.record_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == mHidePosition) {
            convertView.setVisibility(View.INVISIBLE);
        } else {
            convertView.setVisibility(View.VISIBLE);
        }
        holder.icon.setImageResource(IconTypeUtil.getTypeIcon(recordTypes.get(position)));
        return convertView;
    }


    public class ViewHolder {
        public ImageView icon;
    }

    private List<Integer> recordTypes;
    private Context context;
    private LayoutInflater mInflater;
    private int mHidePosition = -1;
}
