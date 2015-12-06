package com.hpu.rule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.hpu.rule.adapter.SearchAdapter;
import com.hpu.rule.bean.count_pian_zhang_gai;
import com.hpu.rule.bease.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class SearchActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText edit;
    private Button btn;
    private ListView list;
    //查询到的数据
    private ArrayList<count_pian_zhang_gai> items;
    //搜索的关键字
    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setTitle("返回");

        initView();
    }

    private void initView() {
        edit = (EditText) findViewById(R.id.edit_query);
        btn = (Button) findViewById(R.id.btn_search);
        list = (ListView) findViewById(R.id.list);
        btn.setOnClickListener(this);
        list.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        s = edit.getText().toString().trim();
        if (s.length() <= 1) {
            toast("输入的太少啦！");
            return;
        } else {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setMessage("查询中请稍候...");
            pd.show();
            BmobQuery<count_pian_zhang_gai> query = new BmobQuery<>();
            query.addWhereContains("detail", s);
            query.findObjects(this, new FindListener<count_pian_zhang_gai>() {
                @Override
                public void onSuccess(List<count_pian_zhang_gai> list) {
                    items = new ArrayList<>();
                    for (count_pian_zhang_gai item : list) {
                        items.add(item);
                    }
                    //在主线程中加载
                    handler.sendEmptyMessage(0);
                    pd.dismiss();
                }

                @Override
                public void onError(int i, String s) {
                    pd.dismiss();
                    toast("查询失败，请稍候再试!");
                }
            });
        }

    }

    //查询到的数据
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (items.size() == 0) {
                toast("对不起，没有搜索到相关内容哦！");
                return;
            }
            SearchAdapter adapter = new SearchAdapter(SearchActivity.this, items);
            list.setAdapter(adapter);
            toast("为您搜索到" + items.size() + "项有关内容");
        }
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("url", items.get(position).getContent());
        i.putExtra("search", s);
        startActivity(i);
    }
}
