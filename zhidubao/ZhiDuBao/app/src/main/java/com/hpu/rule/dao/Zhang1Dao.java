package com.hpu.rule.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hpu.rule.bean.count_pian_zhang_gai;
import com.hpu.rule.db.SQLHelperPian;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库帮助类
 */
public class Zhang1Dao {
    private SQLHelperPian mOpenHelper; // 数据库的帮助类对象

    public Zhang1Dao(Context context) {
        mOpenHelper = new SQLHelperPian(context);
    }

    /**
     * 添加到表一条数据
     */
    public void insert(count_pian_zhang_gai countPian1Zhang) {
        SQLiteDatabase query = mOpenHelper.getReadableDatabase();
        if (query.isOpen()) {
            String[] columns = {"_id", "zhang_name", "content"}; // 需要的列
            String selection = null; // 选择条件, 给null查询所有
            String[] selectionArgs = null; // 选择条件的参数, 会把选择条件中的? 替换成数据中的值
            String groupBy = null; // 分组语句 group by name
            String having = null; // 过滤语句
            String orderBy = null; // 排序
            Cursor cursor = query.query("pian1", columns, selection,
                    selectionArgs, groupBy, having, orderBy);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(1).equals(countPian1Zhang.getZhang_name())) {
                        cursor.close();
                        query.close();
                        return;
                    }
                }
            }
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            if (db.isOpen()) { // 如果数据库打开, 执行添加的操作
                ContentValues values = new ContentValues();
                values.put("zhang_name", countPian1Zhang.getZhang_name());
                values.put("content", countPian1Zhang.getContent());
                values.put("pian_name", countPian1Zhang.getPian_name());
                long id = db.insert("pian1", null, values);
                db.close(); // 数据库关闭
            }
        }
    }

    // 查询表中所有
    public List<count_pian_zhang_gai> queryAll() {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase(); // 获得一个只读的数据库对象
        if (db.isOpen()) {
            String[] columns = {"_id", "zhang_name", "content", "pian_name"}; // 需要的列
            String selection = null; // 选择条件, 给null查询所有
            String[] selectionArgs = null; // 选择条件的参数, 会把选择条件中的? 替换成数据中的值
            String groupBy = null; // 分组语句 group by name
            String having = null; // 过滤语句
            String orderBy = null; // 排序
            Cursor cursor = db.query("pian1", columns, selection,
                    selectionArgs, groupBy, having, orderBy);

            String zhang_name;
            String content;
            String pian_name;
            if (cursor != null && cursor.getCount() > 0) {
                List<count_pian_zhang_gai> personList = new ArrayList<>();
                while (cursor.moveToNext()) {
                    // 向下移一位, 直到最后一位, 不可以往下移动了, 停止.
                    zhang_name = cursor.getString(1);
                    content = cursor.getString(2);
                    pian_name = cursor.getString(3);
                    personList.add(new count_pian_zhang_gai(zhang_name, content, pian_name));
                }
                cursor.close();
                db.close();
                return personList;
            }
            db.close();
        }

        return null;
    }

    /**
     * 判断数据库是否有信息
     */
    public boolean hasInfo() {
        boolean result = false;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase(); // 获得一个只读的数据库对象
        if (db.isOpen()) {
            String[] columns = {"_id", "zhang_name", "content", "pian_name"}; // 需要的列
            String selection = null; // 选择条件, 给null查询所有
            String[] selectionArgs = null; // 选择条件的参数, 会把选择条件中的? 替换成数据中的值
            String groupBy = null; // 分组语句 group by name
            String having = null; // 过滤语句
            String orderBy = null; // 排序
            Cursor cursor = db.query("pian1", columns, selection,
                    selectionArgs, groupBy, having, orderBy);
            if (cursor != null && cursor.getCount() > 0) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * 删除全部信息
     */
    public void delAll() {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete("pian1", null, null);
        }
        db.close();
    }
}
