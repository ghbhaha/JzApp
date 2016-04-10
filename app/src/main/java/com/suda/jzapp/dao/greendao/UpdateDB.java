package com.suda.jzapp.dao.greendao;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by ghbha on 2016/4/7.
 */
public class UpdateDB {
    public static void updateDb(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
        if (oldVersion < 2) {
            UserDao.createTable(db, false);
        }
        if (oldVersion < 3) {
            ConfigDao.createTable(db, false);
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE ACCOUNT ADD OBJECT_ID TEXT");
            db.execSQL("ALTER TABLE RECORD ADD OBJECT_ID TEXT");
            db.execSQL("ALTER TABLE ACCOUNT_TYPE ADD OBJECT_ID TEXT");
        }
        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE RECORD ADD YEAR INTEGER");
            db.execSQL("ALTER TABLE RECORD ADD MONTH INTEGER");
            db.execSQL("ALTER TABLE RECORD ADD DAY INTEGER");
        }
    }
}
