package com.hpu.rule.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 本地数据库
 */
public class SQLHelperPian extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteOpenHelper";

    /**
     * 数据库构造函数
     *
     * @param context
     */
    public SQLHelperPian(Context context) {
        super(context, "zhidubao.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 操作数据库
        String sql = "create table pian1(_id integer primary key, zhang_name string, content string,pian_name string);";
        db.execSQL(sql);
    }

    /**
     * 数据库的版本号更新时回调此方法,
     * 更新数据库的内容(删除表, 添加表, 修改表)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table pian1");
        String sql = "create table pian1(_id integer primary key, zhang_name string, content string,pian_name string);";
        db.execSQL(sql);
    }
}
