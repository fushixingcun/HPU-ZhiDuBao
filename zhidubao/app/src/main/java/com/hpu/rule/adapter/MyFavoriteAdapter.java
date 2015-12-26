package com.hpu.rule.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hpu.rule.R;
import com.hpu.rule.bean.Collect;

import java.util.List;

public class MyFavoriteAdapter extends BaseAdapter {
    private Context context;
    private List<Collect> collects;

    public MyFavoriteAdapter(Context context, List<Collect> collects) {
        this.context = context;
        this.collects = collects;
    }

    @Override
    public int getCount() {
        return collects.size();
    }

    @Override
    public Object getItem(int position) {
        return collects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.childs_view, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText(collects.get(position).getPian_name());
        return convertView;
    }
}
