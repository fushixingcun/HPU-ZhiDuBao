package com.hpu.rule.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hpu.rule.R;
import com.hpu.rule.bean.count_pian_zhang_gai;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询界面的适配器
 */
public class SearchAdapter extends BaseAdapter {
    private List<count_pian_zhang_gai> data;
    private Context context;

    public SearchAdapter(Context context, ArrayList<count_pian_zhang_gai> data) {
        this.context = context;
        this.data = data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
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
        tv.setText(data.get(position).getZhang_name());
        return convertView;
    }
}
