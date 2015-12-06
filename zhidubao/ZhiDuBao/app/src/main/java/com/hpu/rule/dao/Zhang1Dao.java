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
 * 思路：在启动界面判断本地是否有数据库，通过hasinfo()方法
 * 在insert这个方法中先判断本地数据库里面的数据和服务器里面的数据是否一致，不一致直接重新插入数据，一致的话，直接终止该方法
 */
public class Zhang1Dao {
    private SQLHelperPian mOpenHelper; // 数据库的帮助类对象

    public Zhang1Dao(Context context) {
        mOpenHelper = new SQLHelperPian(context);
    }

    /**
     * 添加到表一条数据
     *从count_pian_zhang_gai表中查询数据，添加到表里
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
            //从本地数据库查询数据
            Cursor cursor = query.query("pian1", columns, selection,
                    selectionArgs, groupBy, having, orderBy);
            //避免空指针，要先判断cusor是否为空指针并且数量是否大于0
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (cursor.getString(1).equals(countPian1Zhang.getZhang_name())) {
                        cursor.close();
                        query.close();
                        return;
                    }
                }
            }
            //得到服务器的数据并且插入到pian1表中
            SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            if (db.isOpen()) { // 如果数据库打开, 执行添加的操作
                ContentValues values = new ContentValues();
                values.put("zhang_name", countPian1Zhang.getZhang_name());
                values.put("content", countPian1Zhang.getContent());
                values.put("pian_name", countPian1Zhang.getPian_name());
                //id用于表示第几行
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
            db.execSQL("drop table pian1");
            String sql = "create table pian1(_id integer primary key, zhang_name string, content string,pian_name string);";
            db.execSQL(sql);
        }
        db.close();
    }
}
