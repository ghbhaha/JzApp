package com.suda.jzapp.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.system.AVUpdateCheck;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.manager.SystemManager;
import com.suda.jzapp.manager.domain.OptDO;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.ui.activity.MainActivity;
import com.suda.jzapp.ui.activity.account.MonthReportActivity;
import com.suda.jzapp.ui.activity.system.EditThemeActivity;
import com.suda.jzapp.ui.activity.user.UserLinkActivity;
import com.suda.jzapp.util.NetworkUtil;
import com.suda.jzapp.util.SnackBarUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by ghbha on 2016/2/14.
 */
public class OptMenuAdapter extends BaseAdapter {
    private Activity context;
    private List<OptDO> optDOs;
    private SystemManager mSystemManager;

    public OptMenuAdapter(List<OptDO> optDOs, Activity context) {
        this.optDOs = optDOs;
        this.context = context;
        mSystemManager = new SystemManager(context);
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
                if (optDO.getId() == 5) {
                    checkForUpdate(v);
                } else if (optDO.getId() == 7) {
                    context.finish();
                } else {
                    if (MyAVUser.getCurrentUser() == null && (optDO.getAct() == MonthReportActivity.class || optDO.getAct() == UserLinkActivity.class)) {
                        SnackBarUtil.showSnackInfo(v, context, "请先登陆账号哦");
                        return;
                    }
                    if (!NetworkUtil.checkNetwork(context) && optDO.getAct() == UserLinkActivity.class) {
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

    public void checkForUpdate(final View v) {
        mSystemManager.getUpdateInfo(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constant.MSG_ERROR) {
                    SnackBarUtil.showSnackInfo(v, context, "检测失败");
                } else {
                    final AVUpdateCheck check = (AVUpdateCheck) msg.obj;
                    if (check != null) {
                        final MaterialDialog materialDialog = new MaterialDialog(context);
                        materialDialog.setTitle("升级提示V" + check.getVersion());
                        materialDialog.setMessage(check.getUpdateInfo());
                        materialDialog.setNegativeButton(context.getResources().getString(R.string.download), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setData(Uri.parse(check.getLink()));
                                intent.setAction(Intent.ACTION_VIEW);
                                context.startActivity(intent);
                                materialDialog.dismiss();
                            }
                        });
                        materialDialog.setCanceledOnTouchOutside(true);
                        materialDialog.show();
                    } else {
                        SnackBarUtil.showSnackInfo(v, context, "未检测到更新");
                    }
                }
            }
        });
    }

    public class ViewHolder {
        public TextView title;
        public ImageView icon;
    }
}
