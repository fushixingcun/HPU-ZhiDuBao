package com.hpu.rule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
用于判断用户的身份
 */

/*
本来想的是用listview+checkbox实现，发现处理记住所点击的checkbox有困难，也很麻烦
后来发现可以继承PreferenceActivity来实现，但是有个bug，昨天晚上搞到2点也没搞定
 */
public class Authentication extends PreferenceActivity {
    Context mContext = null;
    private Button checkbox_button;
    private TextView actionbar_Authen_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkbox_button);
        addPreferencesFromResource(R.xml.checkbox);
        mContext = this;

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setTitle("返回");
        View actionBar_layout = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        getActionBar().setCustomView(actionBar_layout);
        actionbar_Authen_Text = (TextView) findViewById(R.id.actionbar_Text);
        actionbar_Authen_Text.setText("用户验证");

        checkbox_button = (Button) findViewById(R.id.cb_button);
        final CheckBoxPreference cb1 = (CheckBoxPreference) findPreference("cb1");
        final CheckBoxPreference cb2 = (CheckBoxPreference) findPreference("cb2");
        final CheckBoxPreference cb3 = (CheckBoxPreference) findPreference("cb3");
        final CheckBoxPreference cb4 = (CheckBoxPreference) findPreference("cb4");
        final CheckBoxPreference cb5 = (CheckBoxPreference) findPreference("cb5");
        final CheckBoxPreference cb6 = (CheckBoxPreference) findPreference("cb6");
        final CheckBoxPreference cb7 = (CheckBoxPreference) findPreference("cb7");
        final CheckBoxPreference cb8 = (CheckBoxPreference) findPreference("cb8");

        //以后会自动进入
        if (cb1.isChecked() && cb3.isChecked() && cb5.isChecked() && cb7.isChecked() && (!cb2.isChecked()) && (!cb4.isChecked()) && (!cb6.isChecked()) && (!cb8.isChecked())) {
            Intent intent = new Intent(getApplicationContext(), SchoolRule.class);
            startActivity(intent);
            finish();
        }
        //第一次需要手动点击
        checkbox_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb1.isChecked() && cb3.isChecked() && cb5.isChecked() && cb7.isChecked() && (!cb2.isChecked()) && (!cb4.isChecked()) && (!cb6.isChecked()) && (!cb8.isChecked())) {
                    Intent intent = new Intent(getApplicationContext(), SchoolRule.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "亲，您的回答有误，睁大眼睛瞧仔细哦！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //    private ActionBar actionBar;
//    private TextView actionbar_HeadMaster_Text;
//    private Button auth_button;
//    private ListView auth_listView;
//    private Authentication_adapter auth_adapter;
//    private ArrayList<String> auth_ArrayList;
//    //private String[] restaurant=new String[]{"学苑餐厅","学院餐厅","学府餐厅","学福餐厅","学士餐厅","学识餐厅","学子餐厅","学字餐厅"};
//
//    private CheckBox cb1,cb2,cb3,cb4,cb5,cb6,cb7,cb8;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.authentication);
//
//        getActionBar().setHomeButtonEnabled(true);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setDisplayShowHomeEnabled(false);
//        getActionBar().setDisplayShowCustomEnabled(true);
//        getActionBar().setTitle("返回");
//        View actionBar_layout= LayoutInflater.from(this).inflate(R.layout.actionbar_layout,null);
//        getActionBar().setCustomView(actionBar_layout);
//        actionbar_HeadMaster_Text=(TextView)findViewById(R.id.actionbar_Text);
//        actionbar_HeadMaster_Text.setText("用户验证");
//
//        cb1=(CheckBox)findViewById(R.id.cb1);
//        cb2=(CheckBox)findViewById(R.id.cb2);
//        cb3=(CheckBox)findViewById(R.id.cb3);
//        cb4=(CheckBox)findViewById(R.id.cb4);
//        cb5=(CheckBox)findViewById(R.id.cb5);
//        cb6=(CheckBox)findViewById(R.id.cb6);
//        cb7=(CheckBox)findViewById(R.id.cb7);
//        cb8=(CheckBox)findViewById(R.id.cb8);
//
//        auth_button=(Button)findViewById(R.id.auth_Button);
//        //auth_listView=(ListView)findViewById(R.id.authentication_listView);
//       // auth_ArrayList=new ArrayList<String>();
//        //为Adapter准备数据
//       // initDate();
//        //实例化adapter
//       // auth_adapter=new Authentication_adapter(auth_ArrayList,this);
//        // 绑定Adapter
////        auth_listView.setAdapter(auth_adapter);
//
////        auth_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                //取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的checkbox实例的步骤
////                Authentication_adapter.ViewHolder holder = (Authentication_adapter.ViewHolder) view.getTag();
////                // 改变CheckBox的状态
////                holder.authen_box.toggle();
////                // 将CheckBox的选中状况记录下来
////                Authentication_adapter.getIsSelected().put(i, holder.authen_box.isChecked());
////            }
////        });
//
//        auth_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            //取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的checkbox实例的步骤
//            // Authentication_adapter.ViewHolder holder = (Authentication_adapter.ViewHolder) view.getTag();
//             // Boolean hh=holder.authen_box.isChecked();
//                if(cb1.isChecked()&&cb3.isChecked()&&cb5.isChecked()&&cb7.isChecked()&&(!cb2.isChecked())&&(!cb4.isChecked())&&(!cb6.isChecked())&&(!cb8.isChecked())){
//                    Intent intent=new Intent(getApplicationContext(),SchoolRule.class);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    Toast.makeText(getApplicationContext(),"亲，您的回答有误，睁大眼睛瞧仔细哦！",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
////实例化数据
////    private void initDate() {
////        for(int i=0;i<restaurant.length;i++){
////            auth_ArrayList.add(restaurant[i]);
////        }
////    }
    //给返回键添加功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}