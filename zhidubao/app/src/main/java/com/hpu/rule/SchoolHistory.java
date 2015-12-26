package com.hpu.rule;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hpu.rule.adapter.MyPagerAdapter;
import com.hpu.rule.bease.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class SchoolHistory extends BaseActivity {
    private TextView actionbar_SchoolHistory_Text;
    //小白点
    private ImageView[] dots1;
    //小白点的id
    private int[] views1={R.id.history_iv1,R.id.history_iv2,R.id.history_iv3,R.id.history_iv4,R.id.history_iv5};
    private ViewPager mViewPager;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
            //mHandler.sendEmptyMessageDelayed(0,3000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_history);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setTitle("返回");
        View actionBar_layout = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        getActionBar().setCustomView(actionBar_layout);
        actionbar_SchoolHistory_Text = (TextView) findViewById(R.id.actionbar_Text);
        actionbar_SchoolHistory_Text.setText("悠悠校园");

        mViewPager=(ViewPager)findViewById(R.id.school_history_viewPager);
        //初始化小白点
        initDots();
        //把需要显示图片放到list集合中
        List<Integer> viewPagerList=new ArrayList<Integer>();
        viewPagerList.add(R.mipmap.ligong_nihao);
        viewPagerList.add(R.mipmap.hpu1);
        viewPagerList.add(R.mipmap.hpu4);
        viewPagerList.add(R.mipmap.hpu2);
        viewPagerList.add(R.mipmap.hpu3);
        //为viewpager设置adapter
        MyPagerAdapter myPagerAdapter=new MyPagerAdapter(SchoolHistory.this,viewPagerList);
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int mPosition) {
                //加载小白点的图片资源
                for (int i=0;i<5;i++){
                    dots1[i].setImageResource(R.mipmap.white);
                }
                //设置当前的image
                int positon = mPosition % views1.length;
                dots1[positon].setImageResource(R.mipmap.blue);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mHandler.sendEmptyMessageDelayed(0, 3000);

    }
    //初始化小白点
    private void initDots(){
        dots1=new ImageView[views1.length];
        for (int i=0;i<5;i++){
            dots1[i]=(ImageView)findViewById(views1[i]);
        }
    }

    //给返回键添加功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
