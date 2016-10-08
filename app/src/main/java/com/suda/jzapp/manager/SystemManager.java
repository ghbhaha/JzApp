package com.suda.jzapp.manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.suda.jzapp.dao.cloud.avos.pojo.system.AVUpdateCheck;
import com.suda.jzapp.dao.greendao.Currency;
import com.suda.jzapp.dao.greendao.YiYan;
import com.suda.jzapp.dao.local.conf.ConfigLocalDao;
import com.suda.jzapp.misc.Constant;
import com.suda.jzapp.util.AppUtil;
import com.suda.jzapp.util.NetworkUtil;
import com.suda.jzapp.util.SPUtils;
import com.suda.jzapp.util.ThreadPoolUtil;

import java.util.ArrayList;
import java.util.Collections;
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

    public void getYiYan(final Handler handler) {


        ThreadPoolUtil.getThreadPoolService().execute(new Runnable() {
            @Override
            public void run() {

                String cuntomYiyan = (String) SPUtils.get(_context, true, "yi_yan_custom", "");
                if (!TextUtils.isEmpty(cuntomYiyan)) {
                    sendMessage(handler, cuntomYiyan);
                    return;
                }

                YiYan yiYan = configLocalDao.queryYiYan(_context);
                if (yiYan != null) {
                    sendMessage(handler, yiYan.getContent());
                }

                boolean syncWifi = (boolean) SPUtils.get(_context, true, "yi_yan_sync_wifi", true);

                if (syncWifi && !NetworkUtil.checkWifi(_context))
                    return;

                try {
                    String result1 = getYiYan();
                    if (!TextUtils.isEmpty(result1)) {
                        if (yiYan == null) {
                            sendMessage(handler, result1);
                            yiYan = new YiYan();
                        }
                        yiYan.setContent(result1);
                        configLocalDao.insertNewYiYan(yiYan, _context);
                    } else
                        sendEmptyMessage(handler, Constant.MSG_ERROR);
                } catch (Exception e) {
                    Log.e("@@@@@@@@", e.toString());
                }
            }
        });
    }

    private String getYiYan() {
        final String lwl12 = "https://api.lwl12.com/hitokoto/main/get?charset=utf-8";
        final String bilibibi = "http://hitoapi.cc/sp/";
        final String ad = "https://api.imjad.cn/hitokoto/?charset=utf-8&length=150&encode=json&fun=sync";
        final String _853 = "http://hitokoto.bronya.net/rand/";
        List<String> urls = new ArrayList<>();
        urls.add(lwl12);
        urls.add(bilibibi);
        urls.add(ad);
        urls.add(_853);
        Collections.shuffle(urls);
        String result1 = NetworkUtil.request(urls.get(0), "");
        switch (urls.get(0)) {
            case lwl12:
                break;
            case bilibibi:
                result1 = JSON.parseObject(result1).getString("text");
                break;
            case ad:
            case _853:
                result1 = JSON.parseObject(result1).getString("hitokoto");
                break;
        }
        return result1;
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
                        Currency currency = JSON.parseObject(jo.toJSONString(), Currency.class);
                        configLocalDao.updateCurrency(currency, _context);
                        Log.e("sss", result);
                    }
                } catch (Exception e) {
                    Log.e("@@@@@@@@", e.toString());
                }
            }
        });
    }

}
