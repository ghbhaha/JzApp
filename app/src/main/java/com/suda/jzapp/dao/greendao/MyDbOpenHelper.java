package com.suda.jzapp.dao.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by Suda on 2015/9/16.
 */
public class MyDbOpenHelper extends DaoMaster.OpenHelper {

    public MyDbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {

        }


    }
}
