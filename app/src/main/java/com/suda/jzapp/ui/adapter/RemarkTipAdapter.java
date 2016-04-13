package com.suda.jzapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suda.jzapp.R;
import com.suda.jzapp.dao.greendao.RemarkTip;
import com.suda.jzapp.manager.domain.AccountDetailDO;
import com.suda.jzapp.view.MyCircleRectangleTextView;

import java.util.List;

/**
 * Created by ghbha on 2016/4/13.
 */
public class RemarkTipAdapter extends RecyclerView.Adapter<RemarkTipAdapter.ViewHolder> {


    private Context context;
    private List<RemarkTip> remarkTips;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;


    public RemarkTipAdapter(Context context, List<RemarkTip> remarkTips) {
        this.remarkTips = remarkTips;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_remark_tip, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final RemarkTip remarkTip = remarkTips.get(position);
        holder.textView.setText(remarkTip.getRemark());
        holder.textView.setClickable(true);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRecyclerViewItemClickListener != null)
                    onRecyclerViewItemClickListener.onItemClick(v, remarkTip.getRemark());
            }
        });

    }

    @Override
    public int getItemCount() {
        return remarkTips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MyCircleRectangleTextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (MyCircleRectangleTextView) itemView.findViewById(R.id.remark_name);
        }
    }

    public void setOnRecyclerViewItemClickListener(OnRecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = recyclerViewItemClickListener;
    }


    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

}
