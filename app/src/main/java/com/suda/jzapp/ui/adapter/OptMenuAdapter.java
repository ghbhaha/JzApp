package com.suda.jzapp.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.manager.domain.OptDO;
import com.suda.jzapp.ui.activity.MainActivity;
import com.suda.jzapp.ui.activity.account.MonthReportActivity;
import com.suda.jzapp.ui.activity.system.EditThemeActivity;
import com.suda.jzapp.ui.activity.user.UserLinkActivity;
import com.suda.jzapp.util.NetworkUtil;
import com.suda.jzapp.util.SnackBarUtil;
import com.suda.jzapp.util.ThemeUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import java.util.List;

/**
 * Created by ghbha on 2016/2/14.
 */
public class OptMenuAdapter extends BaseAdapter {
    private Activity context;
    private List<OptDO> optDOs;

    public OptMenuAdapter(List<OptDO> optDOs, Activity context) {
        this.optDOs = optDOs;
        this.context = context;
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
            convertView = View.inflate(context, R.layout.opt_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            convertView.setBackgroundResource(R.drawable.ripple);
        }

        final OptDO optDO = optDOs.get(position);

        holder.icon.setImageResource(optDO.getIcon());
        holder.icon.setColorFilter(context.getResources().getColor(ThemeUtil.getTheme(context).getMainColorID()));
        holder.title.setText(optDO.getTltle());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (optDO.getId() == 4) {
                    checkForUpdate();
                } else if (optDO.getId() == 6) {
                    context.finish();
                } else {
                    if (MyAVUser.getCurrentUser() == null && (optDO.getAct() == MonthReportActivity.class || optDO.getAct() == UserLinkActivity.class)) {
                        SnackBarUtil.showSnackInfo(v, context, "请先登陆账号哦");
                        return;
                    }
                    if (!NetworkUtil.checkNetwork(context)) {
                        SnackBarUtil.showSnackInfo(v, context, "请打开网络");
                        return;
                    }

                    Intent intent = new Intent(context, optDO.getAct());
                    if (optDO.getAct() == EditThemeActivity.class) {
                        ((MainActivity) context).startActivityForResult(intent, MainActivity.REQUEST_EDIT_THEME);
                    } else
                        context.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    public void checkForUpdate() {

        UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
            @Override
            public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                switch (i) {
                    case UpdateStatus.Yes: // has update
                        UmengUpdateAgent.showUpdateDialog(context, updateResponse);
                        break;
                    case UpdateStatus.No: // has no update
                        Toast.makeText(context, "没有检测到更新", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.NoneWifi: // none wifi
                        Toast.makeText(context, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                        break;
                    case UpdateStatus.Timeout: // time out
                        Toast.makeText(context, "连接超时", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        UmengUpdateAgent.forceUpdate(context);
    }

    public class ViewHolder {
        public TextView title;
        public ImageView icon;
    }
}
