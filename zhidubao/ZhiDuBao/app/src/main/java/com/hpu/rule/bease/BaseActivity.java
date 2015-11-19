package com.hpu.rule.bease;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.hpu.rule.bean.Count_pian1_zhang;
import com.hpu.rule.dao.Zhang1Dao;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * baseActivity
 * Created by hjs on 2015/11/7.
 */
public class BaseActivity extends Activity {
    public static String APPID = "a81a11c43e05047b50c03cb067e8401c";
    public Zhang1Dao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        // 初始化BmobSDK
        Bmob.initialize(this, APPID);
        dao = new Zhang1Dao(this);
        //判断数据库是否已经有数据
        if (!dao.hasInfo()) {
            acquireDataZhang();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void acquireDataZhang() {
        //查询章
        BmobQuery<Count_pian1_zhang> query = new BmobQuery<>();
        query.addWhereContains("zhang_name", "章");
        query.order("createdAt");
        query.findObjects(this, new FindListener<Count_pian1_zhang>() {
            @Override
            public void onSuccess(List<Count_pian1_zhang> list) {
                for (Count_pian1_zhang zhang : list) {
                    dao.insert(zhang);
                }
            }

            @Override
            public void onError(int i, String s) {
                toast("网络有问题哦！");
            }
        });
    }
}
