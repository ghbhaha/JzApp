package com.suda.jzapp.dao.local;

import android.content.Context;

import com.suda.jzapp.dao.greendao.DaoMaster;
import com.suda.jzapp.dao.greendao.DaoSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public abstract class BaseLocalDao {


    protected static final <T> T getSingleData(List<T> result) {
        return (result == null || result.size() == 0) ? null : result.get(0);
    }

    protected DaoSession getDaoSession(Context ctx) {
        if (_daoSession == null) {
            _daoSession = getDaoMaster(ctx.getApplicationContext()).newSession();
        }

        return _daoSession;
    }

    private DaoMaster getDaoMaster(Context ctx) {
        if (_daoMaster == null) {
            synchronized (BaseLocalDao.class) {
                if (_daoMaster == null) {
                    DaoMaster.OpenHelper helper =new DaoMaster.DevOpenHelper(ctx, USER_TABLE, null);
                    _daoMaster = new DaoMaster(helper.getWritableDatabase());
                }
            }
        }
        return _daoMaster;
    }

    protected static String fmDate(Date date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    protected final static String USER_TABLE = "zhangdan.db";

    private static volatile DaoMaster _daoMaster;
    private static DaoSession _daoSession;


}
