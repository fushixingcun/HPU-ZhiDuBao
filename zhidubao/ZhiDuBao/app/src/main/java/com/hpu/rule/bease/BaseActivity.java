package com.hpu.rule.bease;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.hpu.rule.dao.Zhang1Dao;

import cn.bmob.v3.Bmob;

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


}
