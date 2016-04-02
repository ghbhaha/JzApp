package com.suda.jzapp.ui.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.RecordType;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.util.IconTypeUtil;
import com.suda.jzapp.view.drag.DragGridApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ghbha on 2016/2/25.
 */
public class NewRecordTypeAdapter extends BaseAdapter implements DragGridApi {

    public NewRecordTypeAdapter(Context context, List<RecordType> recordTypes) {
        super();
        this.recordTypes = recordTypes;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.recordManager = new RecordManager(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_record_type, null);
            holder.title = (TextView) convertView.findViewById(R.id.record_title);
            holder.icon = (ImageView) convertView.findViewById(R.id.record_icon);
            holder.deleteIcon = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (position == mHidePosition) {
            convertView.setVisibility(View.INVISIBLE);
        } else {
            convertView.setVisibility(View.VISIBLE);
        }

        //最后一个位置为添加按钮 不可移动，别的按钮也不可以移到最后
        if (position == recordTypes.size() - 1) {
            if (convertView.getTag(R.string.app_name) == null)
                convertView.setTag(R.string.app_name, "last");

            holder.title.setText("添加");
            holder.icon.setImageResource(R.drawable.ic_add_white);
            // holder.icon.setBackgroundResource(R.drawable.cricle_add);
            holder.deleteIcon.setVisibility(View.GONE);
        } else {
            convertView.setTag(R.string.app_name, null);
            final RecordType recordType = recordTypes.get(position);
            // holder.icon.setBackgroundResource(-1);
            holder.title.setText(recordType.getRecordDesc());
            holder.icon.setImageResource(IconTypeUtil.getTypeIcon(recordType.getRecordIcon()));

            if (mShake) {
                AnimatorSet mAnimatorSet = new AnimatorSet();
                ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(convertView, "rotation", 2, 0, -2);
                objectAnimator.setRepeatMode(Animation.REVERSE);
                objectAnimator.setRepeatCount(Integer.MAX_VALUE);
                objectAnimator.setDuration(400);
                mAnimatorSet.playTogether(objectAnimator);
                mAnimatorSet.start();
                animatorSets.add(mAnimatorSet);
                holder.deleteIcon.setVisibility(View.VISIBLE);
            } else {
                holder.deleteIcon.setVisibility(View.GONE);
            }
            holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recordTypes.remove(position);
                    recordManager.deleteRecordType(recordType);
                    notifyDataSetChanged();
                }
            });
        }




        return convertView;
    }


    @Override
    public void reorderItems(int oldPosition, int newPosition) {
        RecordType temp = recordTypes.get(oldPosition);
        if (oldPosition < newPosition) {
            for (int i = oldPosition; i < newPosition; i++) {
                Collections.swap(recordTypes, i, i + 1);
            }
        } else if (oldPosition > newPosition) {
            for (int i = oldPosition; i > newPosition; i--) {
                Collections.swap(recordTypes, i, i - 1);
            }
        }
        recordManager.updateRecordTypesOrder(recordTypes);
        recordTypes.set(newPosition, temp);
    }

    @Override
    public void setHideItem(int hidePosition) {
        this.mHidePosition = hidePosition;
        notifyDataSetChanged();
    }

    public class ViewHolder {
        public TextView title;
        public ImageView icon;
        public ImageView deleteIcon;
    }

    public void setShake(boolean shake) {
        mShake = shake;
        this.notifyDataSetChanged();
        if (!shake) {
            for (AnimatorSet mAnimatorSet : animatorSets) {
                mAnimatorSet.end();
            }
            animatorSets.clear();
        }
    }

    public boolean ismShake() {
        return mShake;
    }

    private boolean mShake = false;
    private List<RecordType> recordTypes;
    private Context context;
    private LayoutInflater mInflater;
    private int mHidePosition = -1;
    private RecordManager recordManager;
    private List<AnimatorSet> animatorSets = new ArrayList<>();
}
