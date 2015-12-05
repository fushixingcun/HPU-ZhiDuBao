package com.hpu.rule;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hpu.rule.adapter.MyFavoriteAdapter;
import com.hpu.rule.bean.Collect;
import com.hpu.rule.bease.BaseActivity;

import java.util.List;

public class MyFavorite extends BaseActivity implements AdapterView.OnItemClickListener {
    private TextView actionbar_MyFavorite;
    private ListView list;
    private List<Collect> collects;
    private MyFavoriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setTitle("返回");
        View actionBar_layout = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        getActionBar().setCustomView(actionBar_layout);
        actionbar_MyFavorite = (TextView) findViewById(R.id.actionbar_Text);
        actionbar_MyFavorite.setText("我的收藏");
        list = (ListView) findViewById(R.id.list_collect);

    }

    private void initData() {
        collects = urldao.qurreyAll();
        initview(collects);
    }

    private void initview(List<Collect> collects) {
        adapter = new MyFavoriteAdapter(MyFavorite.this, collects);
        list.setAdapter(adapter);
        //判断是否有数据
        if (collects.size() > 0) {
            list.setOnItemClickListener(this);
        } else {
            toast("暂时没有收藏的数据哦!");
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(MyFavorite.this, DetailActivity.class);
        i.putExtra("url", collects.get(position).getUrl());
        i.putExtra("pian_name", collects.get(position).getPian_name());
        i.putExtra("position", collects.get(position).getPosition());
        startActivity(i);
    }

    /**
     * 再次看到当前界面时，刷新数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
}
