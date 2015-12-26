package com.hpu.rule.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hpu.rule.bean.count_pian_zhang_gai;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取离线数据的工具类
 */
public class DBUtils {
    public  List<count_pian_zhang_gai> getOffInfo() {
        String path = "data/data/com.hpu.rule/files/zhidubao_off.db";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        List<count_pian_zhang_gai> personList = new ArrayList<>();
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
        }
        return personList;
    }
}
