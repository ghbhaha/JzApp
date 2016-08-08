package com.suda.jzapp.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.suda.jzapp.dao.cloud.avos.pojo.system.AVUpdateCheck;
import com.suda.jzapp.dao.greendao.Currency;
import com.suda.jzapp.dao.local.conf.ConfigLocalDao;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.AppUtil;
import com.suda.jzapp.util.NetworkUtil;
import com.suda.jzapp.util.ThreadPoolUtil;

import java.util.List;

/**
 * Created by suda on 2016/8/5.
 */
public class SystemManager extends BaseManager {
    public SystemManager(Context context) {
        super(context);
    }

    private ConfigLocalDao configLocalDao = new ConfigLocalDao();

    public void getUpdateInfo(final Handler handler) {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                PackageInfo packInfo = AppUtil.getAppInfo(_context);
                if (packInfo == null) {
                    sendEmptyMessage(handler, Constant.MSG_ERROR);
                    return;
                }
                AVQuery<AVUpdateCheck> avUpdateCheckAVQuery = AVQuery.getQuery(AVUpdateCheck.class);
                avUpdateCheckAVQuery.whereGreaterThan(AVUpdateCheck.VERSION_CODE, packInfo.versionCode);
                try {
                    List<AVUpdateCheck> list = avUpdateCheckAVQuery.find();
                    if (list != null && list.size() > 0) {
                        sendMessage(handler, list.get(0));
                    } else {
                        sendEmptyMessage(handler, Constant.MSG_SUCCESS);
                    }
                } catch (AVException e) {
                    e.printStackTrace();
                    sendEmptyMessage(handler, Constant.MSG_ERROR);
                }
            }
        });
    }

    public void getCurrency() {
        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {
                String httpUrl1 = "http://apis.baidu.com/apistore/currencyservice/type";
                String httpArg1 = "";

                try {
                    String result1 = NetworkUtil.request(httpUrl1, httpArg1);
                    JSONObject jo = JSON.parseObject(result1);
                    JSONArray ja = jo.getJSONArray("retData");
                    for (int i = 0; i < ja.size(); i++) {
                        String httpUrl = "http://apis.baidu.com/apistore/currencyservice/currency";
                        String httpArg = "fromCurrency=CNY&toCurrency=" + ja.get(i) + "&amount=1";
                        String result = NetworkUtil.request(httpUrl, httpArg);
                        jo = JSON.parseObject(result);
                        jo = jo.getJSONObject("retData");
                        Currency currency = JSON.parseObject(jo.toJSONString(),Currency.class);
                        configLocalDao.updateCurrency(currency,_context);
                        Log.e("sss", result);
                    }
                } catch (Exception e) {
                    Log.e("@@@@@@@@",e.toString());
                }
            }
        });
    }

}
