package com.hpu.rule.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelperCollect extends SQLiteOpenHelper {
    public SQLHelperCollect(Context context) {
        super(context, "collect.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 操作数据库
        String sql = "create table collect1(_id integer primary key,collect_url string,pian_name string);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
