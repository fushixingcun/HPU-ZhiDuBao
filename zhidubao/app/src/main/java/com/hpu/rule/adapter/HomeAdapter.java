package com.hpu.rule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hpu.rule.R;
import com.hpu.rule.view.Home;

import java.util.List;

/**
 * 为主页提供一个自定义的adapter
 * Created by hjs on 2015/11/7.
 */
public class HomeAdapter extends ArrayAdapter<Home> {
    private int resource;
    public HomeAdapter(Context context, int resource, List<Home> objects) {
        super(context, resource, objects);
        this.resource=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Home home=getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resource,null);
            viewHolder=new ViewHolder();
            viewHolder.homeImage=(ImageView)view.findViewById(R.id.home_image_left);
            viewHolder.homeText=(TextView)view.findViewById(R.id.home_textView);
            view.setTag(viewHolder);
        }else {
            view=convertView;
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.homeImage.setImageResource(home.getImageId());
        viewHolder.homeText.setText(home.getName());
        return view;
    }
    class ViewHolder{
        ImageView homeImage;
        TextView homeText;
    }
}
