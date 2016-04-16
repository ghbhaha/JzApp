package com.suda.jzapp.ui.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.bumptech.glide.Glide;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.suda.jzapp.BaseActivity;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.record.AVRecord;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.cloud.avos.pojo.user.UserLink;
import com.suda.jzapp.dao.greendao.RecordType;
import com.suda.jzapp.dao.greendao.User;
import com.suda.jzapp.dao.local.record.RecordTypeLocalDao;
import com.suda.jzapp.manager.UserManager;
import com.suda.jzapp.manager.domain.MsgDo;
import com.suda.jzapp.manager.domain.RecordDetailDO;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.MsgConstant;
import com.suda.jzapp.misc.MyMessageHandler;
import com.suda.jzapp.ui.activity.system.CaptureActivityAnyOrientation;
import com.suda.jzapp.ui.adapter.AccountRecordAdapter;
import com.suda.jzapp.util.LogUtils;
import com.suda.jzapp.util.NetworkUtil;
import com.suda.jzapp.util.SnackBarUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.drakeet.materialdialog.MaterialDialog;

public class UserLinkActivity extends BaseActivity implements MyMessageHandler.MsgCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ThemeUtil.getAppStyleId(this));
        setContentView(R.layout.activity_account_link);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userManager = new UserManager(this);
        myMessageHandler = new MyMessageHandler(this);
        myMessageHandler.setMsgCallBack(this);
        initWidget();
        AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class, myMessageHandler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                LogUtils.d("MainActivity", "Cancelled scan");
            } else {
                LogUtils.d("MainActivity", "Scanned");
                sendLinkRequest(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        linkBt.setBackgroundTintList(getResources().getColorStateList(getMainTheme().getMainColorID()));
    }

    @Override
    protected void onDestroy() {
        userManager.closeIMClient();
        super.onDestroy();
    }

    @Override
    protected void initWidget() {
        linkBt = (FloatingActionButton) findViewById(R.id.fab);
        linkRecordsLv = (ListView) findViewById(R.id.link_records);
        linkRecordsLv.setVisibility(View.GONE);

        View head = View.inflate(this, R.layout.item_link_head, null);
        myHead = (CircleImageView) head.findViewById(R.id.my_head);
        otherHead = (CircleImageView) head.findViewById(R.id.other_head);
        myName = (TextView) head.findViewById(R.id.my_name);
        otherName = (TextView) head.findViewById(R.id.other_name);
        ((ImageView) head.findViewById(R.id.link_image)).setColorFilter(getColor(this, getMainTheme().getMainColorID()));
        head.findViewById(R.id.link_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MaterialDialog materialDialog = new MaterialDialog(UserLinkActivity.this);
                materialDialog.setTitle("解除关联？")
                        .setMessage("");
                materialDialog.setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userLink.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(AVException e) {
                                materialDialog.dismiss();
                                linkRecordsLv.setVisibility(View.GONE);
                                linkBt.setVisibility(View.VISIBLE);
                                otherUserName = "";
                                UserManager.userLink=null;

                            }
                        });
                    }
                });
                materialDialog.setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDialog.dismiss();
                    }
                });
                materialDialog.show();
            }
        });
        recordDetailDOs = new ArrayList<>();
        accountRecordAdapter = new AccountRecordAdapter(this, recordDetailDOs);
        linkRecordsLv.addHeaderView(head);
        linkRecordsLv.setAdapter(accountRecordAdapter);

        initUserLink();

    }

    private void initUserLink() {
        userManager.queryUserLinkByUser(userManager.getCurUserName(), new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.obj == null) {
                    linkBt.setVisibility(View.VISIBLE);
                } else {
                    userLink = (UserLink) msg.obj;
                    List<String> users = ((UserLink) msg.obj).getMembers();
                    if (!userManager.getCurUserName().equals(users.get(0))) {
                        otherUserName = users.get(0);
                    } else {
                        otherUserName = users.get(1);
                    }
                    initLinkUserInfo();
                }

            }
        });
    }

    private void initLinkUserInfo() {

        userManager.getMe(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                final User me = (User) msg.obj;
                super.handleMessage(msg);
                userManager.queryUser(otherUserName, new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        otherUser = (User) msg.obj;
                        linkRecordsLv.setVisibility(View.VISIBLE);
                        myName.setText(me.getUserName());
                        otherName.setText(otherUserName);
                        Glide.with(UserLinkActivity.this.getApplicationContext()).
                                load(me.getHeadImage()).error(R.mipmap.suda)
                                .into(myHead);
                        Glide.with(UserLinkActivity.this.getApplicationContext()).
                                load(otherUser.getHeadImage()).error(R.mipmap.suda)
                                .into(otherHead);
                        initRecordData();
                    }
                });
            }
        });
    }

    private void initRecordData() {
        recordDetailDOs.clear();
        MyAVUser myAVUser = new MyAVUser();
        myAVUser.setObjectId(otherUser.getUserId());
        AVQuery<AVRecord> query = AVObject.getQuery(AVRecord.class);
        query.whereEqualTo(AVRecord.USER, myAVUser);
        query.whereEqualTo(AVRecord.RECORD_TYPE, -1);
        query.whereEqualTo(AVRecord.RECORD_IS_DEL, false);
        query.orderByDescending(AVRecord.RECORD_ID);
        //缓存2分钟
       // query.setCachePolicy(AVQuery.CachePolicy.CACHE_ELSE_NETWORK);
        //query.setMaxCacheAge(2 * 60 * 1000);
        query.findInBackground(new FindCallback<AVRecord>() {
            @Override
            public void done(List<AVRecord> list, AVException e) {
                if (e == null) {
                    for (AVRecord avRecord : list) {
                        RecordDetailDO recordDetailDO = new RecordDetailDO();
                        recordDetailDO.setRecordID(-100);
                        recordDetailDO.setIconId(avRecord.getIconID());
                        recordDetailDO.setRecordDate(avRecord.getRecordDate());
                        recordDetailDO.setRecordMoney(avRecord.getRecordMoney());
                        recordDetailDO.setRemark(avRecord.getRemark());
                        recordDetailDO.setIconId(avRecord.getIconID());
                        recordDetailDO.setRecordDesc(avRecord.getRecordName());
                        if (avRecord.getRecordTypeId() < 1000) {
                            RecordType recordType = recordTypeLocalDao.getRecordTypeById(UserLinkActivity.this, avRecord.getRecordTypeId());
                            recordDetailDO.setIconId(recordType.getRecordIcon());
                            recordDetailDO.setRecordDesc(recordType.getRecordDesc());
                        }

                        recordDetailDOs.add(recordDetailDO);
                    }
                    accountRecordAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void link(View view) {
        if (!NetworkUtil.checkNetwork(this)) {
            SnackBarUtil.showSnackInfo(linkBt, UserLinkActivity.this, "请打开网络");
            return;
        }

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        integrator.setPrompt("请扫描需要关联账户的二维码");
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    public void show(MsgDo msgDo) {
        if (msgDo.getMsgType() == MsgConstant.MSG_TYPE_LINK_ACCEPT) {
            linkBt.setVisibility(View.INVISIBLE);
            initUserLink();
            SnackBarUtil.showSnackInfo(linkBt, this, msgDo.getSendUser() + "接受关联");
        }
        if (msgDo.getMsgType() == MsgConstant.MSG_TYPE_LINK_CANCEL) {
            SnackBarUtil.showSnackInfo(linkBt, this, msgDo.getSendUser() + "拒绝关联");
        }
    }

    private void sendLinkRequest(String code) {

        if (TextUtils.isEmpty(code))
            return;

        if (code.contains(Constant.QR_MARK)) {
            final String userName = code.replace(Constant.QR_MARK, "");
            userManager.getMe(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    final User user = (User) msg.obj;
                    if (userName.equals(user.getUserName())) {
                        SnackBarUtil.showSnackInfo(linkBt, UserLinkActivity.this, "自己不可与自己关联哦");
                        // Toast.makeText(UserLinkActivity.this, "自己不可与自己关联哦", Toast.LENGTH_LONG).show();
                        return;
                    }

                    AVQuery<UserLink> query = AVObject.getQuery(UserLink.class);
                    List<String> users = new ArrayList<String>();
                    users.add(userName);
                    //users.add(user.getUserName());
                    query.whereContainsAll(UserLink.MEMBER, users);
                    query.findInBackground(new FindCallback<UserLink>() {
                        @Override
                        public void done(List<UserLink> list, AVException e) {
                            if (e == null) {
                                if (list.size() > 0) {
                                    SnackBarUtil.showSnackInfo(linkBt, UserLinkActivity.this, "对方已经关联过账户");
                                    return;
                                } else {
                                    userManager.sendMsg(userName, MsgConstant.MSG_TYPE_LINK_REQUEST, userName, user.getHeadImage(), new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                            super.handleMessage(msg);
                                            SnackBarUtil.showSnackInfo(linkBt, UserLinkActivity.this, "请求成功");
                                            //Toast.makeText(UserLinkActivity.this, "请求成功", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
            });
        }
    }


    FloatingActionButton linkBt;
    private UserManager userManager;
    private MyMessageHandler myMessageHandler;
    private String otherUserName;
    private ListView linkRecordsLv;
    private CircleImageView myHead, otherHead;
    private TextView myName, otherName;
    private AccountRecordAdapter accountRecordAdapter;
    List<RecordDetailDO> recordDetailDOs;
    private UserLink userLink;
    private User otherUser;
    private RecordTypeLocalDao recordTypeLocalDao = new RecordTypeLocalDao();
}
