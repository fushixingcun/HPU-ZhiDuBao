package com.hpu.rule.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hpu.rule.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 用于验证用户身份的adapter
 * Created by hjs on 2016/1/2.
 */
public class Authentication_adapter extends BaseAdapter {
    // 填充数据的list
    private ArrayList<String> list;
    // 用来控制CheckBox的选中状况
    public  static HashMap<Integer,Boolean> isSelected;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;

    // 构造器
    public Authentication_adapter(ArrayList<String> list, Context context) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        isSelected = new HashMap<Integer, Boolean>();
        // 初始化数据
        initDate();
    }
    // 初始化isSelected的数据
    private void initDate(){
        for(int i=0; i<list.size();i++) {
            getIsSelected().put(i,false);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(view==null){
            // 获得ViewHolder对象
            holder=new ViewHolder();
            // 导入布局并赋值给view
            view=inflater.inflate(R.layout.authentication_item,null);

            holder.authen_textView=(TextView)view.findViewById(R.id.authentication_textView);
            holder.authen_box=(CheckBox)view.findViewById(R.id.authentication_checkBox);
            //为view设置标签
            view.setTag(holder);
        }else {
            // 取出holder
            holder = (ViewHolder) view.getTag();
        }
       // 设置list中TextView的显示
        holder.authen_textView.setText(list.get(i));
        // 根据isSelected来设置checkbox的选中状况
        holder.authen_box.setChecked(getIsSelected().get(i));

        return view;
    }

 public class ViewHolder {
    TextView authen_textView;
    public CheckBox authen_box;
}
    public static HashMap<Integer,Boolean> getIsSelected(){
        return isSelected;
    }
    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
        Authentication_adapter.isSelected = isSelected;
    }
}
