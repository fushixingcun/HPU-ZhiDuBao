package com.hpu.rule;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hpu.rule.bease.BaseActivity;

/**
 * 用于实现校长寄语
 * Created by hjs on 2015/11/12.
 */
public class HeadMaster extends BaseActivity {
    private ActionBar actionBar;
    private TextView zhouYouFeng;
    private TextView yangXiaoLin;
    private TextView actionbar_HeadMaster_Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.headmaster);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setTitle("返回");
        View actionBar_layout= LayoutInflater.from(this).inflate(R.layout.actionbar_layout,null);
        getActionBar().setCustomView(actionBar_layout);

        actionbar_HeadMaster_Text=(TextView)findViewById(R.id.actionbar_Text);
        actionbar_HeadMaster_Text.setText("校长寄语");

        zhouYouFeng=(TextView)findViewById(R.id.headmaster_text_content6);
        yangXiaoLin=(TextView)findViewById(R.id.headmaster_text_content7);
        //创建一个 SpannableString对象
        SpannableString sp1=new SpannableString("校党委书记：邹友峰");
        //最后一个参数的含义是前后都不包括
        sp1.setSpan(new URLSpan("http://baike.baidu.com/link?url=dqJaEXex1zJPaV-NN3k8ZRC7qOQiaGaSYV9L5tO4Vcm2SY7SwrpW2eflJ3I5t9nDfLoIwtz7ohVS_9cJRqztJK"), 6, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        zhouYouFeng.setText(sp1);
        zhouYouFeng.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString sp2=new SpannableString("校  长：杨小林");
        sp2.setSpan(new URLSpan("http://baike.baidu.com/link?url=yuyW3H5rJmpwCkZh1zMeV27rjljdDpwviBWCaJn5FVE6fp8xPDisH8Ux1vq_42uYEy_meoEW9S9e3Od7TrXDD0F7bBAwr7JBlSLrePZbmYW"),5,8,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        yangXiaoLin.setText(sp2);
        yangXiaoLin.setMovementMethod(LinkMovementMethod.getInstance());
    }
    //给返回键添加功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==android.R.id.home){
            finish();
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

}
