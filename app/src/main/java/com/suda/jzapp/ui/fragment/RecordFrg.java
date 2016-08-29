package com.suda.jzapp.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVAnalytics;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.melnykov.fab.FloatingActionButton;
import com.suda.jzapp.R;
import com.suda.jzapp.dao.cloud.avos.pojo.user.MyAVUser;
import com.suda.jzapp.dao.greendao.Record;
import com.suda.jzapp.manager.RecordManager;
import com.suda.jzapp.manager.domain.RecordDetailDO;
import com.suda.jzapp.manager.domain.VoiceDo;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.misc.IntentConstant;
import com.suda.jzapp.ui.activity.MainActivity;
import com.suda.jzapp.ui.activity.record.CreateOrEditRecordActivity;
import com.suda.jzapp.ui.adapter.RecordAdapter;
import com.suda.jzapp.util.NetworkUtil;
import com.suda.jzapp.util.SPUtils;
import com.suda.jzapp.util.SnackBarUtil;
import com.suda.jzapp.util.ThemeUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by ghbha on 2016/2/15.
 */
public class RecordFrg extends Fragment implements MainActivity.ReloadCallBack {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        recordManager = new RecordManager(getActivity());
        mInitListener = new InitListener() {
            @Override
            public void onInit(int code) {
                Log.d(TAG, "SpeechRecognizer init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                }
            }
        };
        mRecognizerDialogListener = new RecognizerDialogListener() {
            /**
             * 识别回调错误.
             */
            public void onError(SpeechError error) {
            }

            @Override
            public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
                parseResult(recognizerResult);
            }
        };

        View view = inflater.inflate(R.layout.record_frg_layout, container, false);
        backGround = view.findViewById(R.id.background);
        mAddRecordBt = (FloatingActionButton) view.findViewById(R.id.add_new_record);
        mAddRecordBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordAdapter.resetOptStatus();
                if (SPUtils.gets(getActivity(), Constant.SP_FIRST_ADD, true)) {
                    SPUtils.put(getActivity(), Constant.SP_FIRST_ADD, false);
                    final MaterialDialog materialDialog = new MaterialDialog(getActivity());
                    materialDialog.setTitle("提示");
                    materialDialog.setMessage("长按可以进语音记账，更加快捷哦\n请说出如\"吃饭支出100元\"");
                    materialDialog.setNegativeButton(getResources().getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            materialDialog.dismiss();
                        }
                    });
                    materialDialog.show();
                    return;
                }
                Intent intent = new Intent(getActivity(), CreateOrEditRecordActivity.class);
                getActivity().startActivityForResult(intent, MainActivity.REQUEST_RECORD);
            }
        });

        //初始化讯飞
        mIatDialog = new RecognizerDialog(getActivity(), mInitListener);
        mIatDialog.setParameter(SpeechConstant.VAD_BOS, "4000");
        mIatDialog.setParameter(SpeechConstant.VAD_EOS, "1000");
        mIatDialog.setListener(mRecognizerDialogListener);
        mAddRecordBt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (NetworkUtil.checkNetwork(getActivity())) {
                    mIatDialog.show();
                    mVibrator.vibrate(50); //震动一下
                } else {
                    final MaterialDialog materialDialog = new MaterialDialog(getActivity());
                    materialDialog.setTitle("提示");
                    materialDialog.setMessage("请连接网络哦");
                    materialDialog.setNegativeButton(getResources().getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            materialDialog.dismiss();
                        }
                    });
                    materialDialog.show();
                }
                return false;
            }
        });

        foot = View.inflate(getActivity(), R.layout.record_foot, null);

        footTv = ((TextView) foot.findViewById(R.id.foot_tip));
        nullTipTv = ((TextView) view.findViewById(R.id.null_tip));

        recordLv = (ListView) view.findViewById(R.id.record_lv);
        recordLv.addFooterView(foot);
        recordDetailDOs = new ArrayList<>();
        mRecordAdapter = new RecordAdapter(getActivity(), recordDetailDOs, this);
        recordLv.setAdapter(mRecordAdapter);
        recordLv.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mForceSyncSrl = (SwipeRefreshLayout) view.findViewById(R.id.force_sync);
        mForceSyncSrl.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mForceSyncSrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recordManager.forceSync(new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        mForceSyncSrl.setRefreshing(false);
                        if (msg.what == Constant.MSG_SUCCESS) {
                            if (getActivity() != null)
                                ((MainActivity) getActivity()).refreshAll();
                            SnackBarUtil.showSnackInfo(backGround, getActivity(), "同步完成");
                        } else {
                            SnackBarUtil.showSnackInfo(backGround, getActivity(), "同步失败，请检查网络");
                        }
                    }
                });
            }
        });

        //resetFoot();

        reload(true);

        //recordLv.setEmptyView();

        recordLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount == 0)
                    return;
                if (firstVisibleItem + visibleItemCount == totalItemCount && !isRefresh) {
                    loadData();
                }
            }
        });


        ((MainActivity) getActivity()).setReloadRecordCallBack(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainColor = getResources().getColor(ThemeUtil.getTheme(getActivity()).getMainColorID());
        mainDarkColor = getResources().getColor(ThemeUtil.getTheme(getActivity()).getMainDarkColorID());
        mAddRecordBt.setColorNormal(mainColor);
        mAddRecordBt.setColorPressed(mainDarkColor);
        backGround.setBackground(new ColorDrawable(mainColor));
        nullTipTv.setTextColor(mainColor);
        footTv.setTextColor(mainColor);
        AVAnalytics.onFragmentStart("RecordFrg");
        //resetFoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("RecordFrg");
    }

    public void resetFoot() {
        nullTipTv.setTextColor(mainColor);
        footTv.setTextColor(mainColor);
        footTv.setOnClickListener(null);
        if (recordDetailDOs.size() > 0) {

        }
        nullTipTv.setVisibility(recordDetailDOs.size() > 0 ? View.GONE : View.VISIBLE);
        footTv.setVisibility(recordDetailDOs.size() == 0 ? View.GONE : View.VISIBLE);
        DateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日");
        if (MyAVUser.getCurrentUser() != null) {

            // foot.setVisibility(View.VISIBLE);
            Date date = MyAVUser.getCurrentUser().getCreatedAt();
            footTv.setText(format1.format(date) + "\n您开启了记账旅程");
        } else {
            // foot.setVisibility(View.GONE);
            footTv.setText(format1.format(new Date(System.currentTimeMillis())) + "\n您开启了记账旅程");
        }

    }

    @Override
    public void reload(final boolean needUpdateData) {

        if (!needUpdateData) {
            mRecordAdapter.notifyDataSetChanged();
            //resetFoot();
            return;
        }

        isRefresh = true;
        curPage = 1;

        recordManager.getRecordByPageIndex(curPage, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                curPage++;
                isRefresh = false;
                if (msg.what == Constant.MSG_SUCCESS) {
                    recordDetailDOs.clear();
                    recordDetailDOs.addAll((List<RecordDetailDO>) msg.obj);
                    mRecordAdapter.notifyDataSetChanged();
                    resetFoot();
                }
            }
        });
    }

    private void loadData() {
        isRefresh = true;
        recordManager.getRecordByPageIndex(curPage, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == Constant.MSG_SUCCESS) {
                    curPage++;
                    if (((List<RecordDetailDO>) msg.obj).size() == 0) {
                        return;
                    }
                    isRefresh = false;
                    recordDetailDOs.addAll((List<RecordDetailDO>) msg.obj);
                    mRecordAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void parseResult(com.iflytek.cloud.RecognizerResult results) {
        JSONObject jsonObject = JSON.parseObject(results.getResultString());
        if (2 == jsonObject.getInteger("sn"))
            return;

        try {
            JSONArray ws = jsonObject.getJSONArray("ws");
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < ws.size(); i++) {
                builder.append(ws.getJSONObject(i).getJSONArray("cw").getJSONObject(0).getString("w"));
            }

            if (TextUtils.isEmpty(builder.toString())) {
                SnackBarUtil.showSnackInfo(backGround, getActivity(), "小的没听清，请再说一遍");
                return;
            }
            recordManager.parseVoice(builder.toString(), new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    VoiceDo voiceDo = (VoiceDo) msg.obj;
                    if (voiceDo.getResultCode() == Constant.VOICE_PARSE_FAIL) {
                        SnackBarUtil.showSnackInfo(backGround, getActivity(), "小的没听清，请再说一遍");
                    } else if (voiceDo.getResultCode() == Constant.VOICE_PARSE_NOT_FOUND_RECORD_TYPE) {
                        SnackBarUtil.showSnackInfo(backGround, getActivity(), "小的未找到\"" + voiceDo.getSplitStr() + "\"");
                    } else {
                        Intent intent = new Intent(getActivity(), CreateOrEditRecordActivity.class);
                        Record record = new Record();
                        record.setRecordMoney(voiceDo.getMoney());
                        record.setRecordType(voiceDo.getRecordTypeDo().getRecordType());
                        record.setRecordTypeID(voiceDo.getRecordTypeDo().getRecordTypeID());
                        intent.putExtra(IntentConstant.VOICE_RECORD, record);
                        intent.putExtra(IntentConstant.VOICE_RECORD_TYPE, voiceDo.getRecordTypeDo());
                        getActivity().startActivityForResult(intent, MainActivity.REQUEST_RECORD);
                    }
                }
            });
        } catch (Exception e) {
            SnackBarUtil.showSnackInfo(backGround, getActivity(), "小的没听清，请再说一遍");
        }
    }

    private ListView recordLv;
    private TextView footTv;
    private View backGround;
    private int mainColor;
    private int mainDarkColor;
    private FloatingActionButton mAddRecordBt;
    private RecordManager recordManager;
    private RecordAdapter mRecordAdapter;
    private List<RecordDetailDO> recordDetailDOs;
    private int curPage = 1;
    private boolean isRefresh = true;
    private View foot;
    private TextView nullTipTv;
    private SwipeRefreshLayout mForceSyncSrl;

    private final String TAG = "SPEECH";
    private Vibrator mVibrator;

    private RecognizerDialog mIatDialog;

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener;

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener;

}