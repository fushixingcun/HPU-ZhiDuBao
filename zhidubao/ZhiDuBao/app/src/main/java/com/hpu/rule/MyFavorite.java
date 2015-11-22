package com.hpu.rule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hpu.rule.bease.BaseActivity;

public class MyFavorite extends BaseActivity{
    private TextView actionbar_MyFavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setTitle("返回");
        View actionBar_layout= LayoutInflater.from(this).inflate(R.layout.actionbar_layout,null);
        getActionBar().setCustomView(actionBar_layout);
        actionbar_MyFavorite=(TextView)findViewById(R.id.actionbar_Text);
        actionbar_MyFavorite.setText("我的收藏");
    }
}
