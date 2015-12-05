package com.hpu.rule.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hpu.rule.bean.Collect;
import com.hpu.rule.db.SQLHelperCollect;

import java.util.ArrayList;
import java.util.List;

public class CollectDao {
    private SQLHelperCollect mOpenHelper;

    public CollectDao(Context context) {
        mOpenHelper = new SQLHelperCollect(context);
    }

    /**
     * 添加一条数据
     */
    public void insert(String url, String pian_name, int position) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("collect_url", url);
            values.put("pian_name", pian_name);
            values.put("position", position);
            db.insert("collect1", null, values);
            db.close();
        }

    }

    /**
     * 删除一条数据
     */

    public void del(String url) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        if (db.isOpen()) {
            String whereClause = "collect_url = ?";
            String[] whereArgs = {url};
            db.delete("collect1", whereClause, whereArgs);
            db.close();
        }
    }

    /**
     * 查询url是否存在
     *
     * @return
     */
    public boolean querryUrl(String url) {
        boolean result = false;
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        if (db.isOpen()) {
            String[] columns = {"collect_url"}; // 需要的列
            String selection = "collect_url=?"; // 选择条件, 给null查询所有
            String[] selectionArgs = {url}; // 选择条件的参数, 会把选择条件中的? 替换成数据中的值
            String groupBy = null; // 分组语句 group by name
            String having = null; // 过滤语句
            String orderBy = null; // 排序

            Cursor cursor = db.query("collect1", columns, selection,
                    selectionArgs, groupBy, having, orderBy);
            if (cursor != null && cursor.moveToFirst()) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * 查询所有收藏
     *
     * @return
     */
    public List<Collect> qurreyAll() {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase(); // 获得一个只读的数据库对象
        List<Collect> collects = new ArrayList<>();
        if (db.isOpen()) {
            String[] columns = {"_id", "collect_url", "pian_name", "position"}; // 需要的列
            String selection = null; // 选择条件, 给null查询所有
            String[] selectionArgs = null; // 选择条件的参数, 会把选择条件中的? 替换成数据中的值
            String groupBy = null; // 分组语句 group by name
            String having = null; // 过滤语句
            String orderBy = null; // 排序
            Cursor cursor = db.query("collect1", columns, selection,
                    selectionArgs, groupBy, having, orderBy);

            String collect_url;
            String pian_name;
            int position;
            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    // 向下移一位, 直到最后一位, 不可以往下移动了, 停止.
                    collect_url = cursor.getString(1);
                    pian_name = cursor.getString(2);
                    position = cursor.getInt(3);
                    collects.add(new Collect(collect_url, pian_name, position));
                }
                cursor.close();
                db.close();
                return collects;
            }
            db.close();
        }

        return collects;
    }
}
