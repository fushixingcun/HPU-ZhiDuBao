package com.hpu.rule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.hpu.rule.bean.count_pian_zhang_gai;
import com.hpu.rule.dao.Zhang1Dao;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/*
用于实现引导界面
 */
public class Gui extends Activity {
    private TextView guiText;
    private int count = 3;
    private Animation mAnimation;
    public Zhang1Dao dao;
    //app在bmob上的id
    public static String APPID = "a81a11c43e05047b50c03cb067e8401c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.gui);
        Bmob.initialize(this, APPID);
        guiText = (TextView) findViewById(R.id.gui_text);
        //加载animation
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_text);
        //判断数据库是否已经有数据
        dao = new Zhang1Dao(this);
        if (!dao.hasInfo()) {
            acquireDataZhang();
        }
        //发送一个value为0的空消息，并且延时1秒
        handler.sendEmptyMessageDelayed(0, 1000);
    }

    private Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                guiText.setText(getCount() + "");
                handler.sendEmptyMessageDelayed(0, 1000);
                mAnimation.reset();
                guiText.startAnimation(mAnimation);
            }
        }
    };

    private int getCount() {
        count--;
        //当count等于0时，进入主界面
        if (count == 0) {
            Intent intent = new Intent(Gui.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return count;
    }

    private void acquireDataZhang() {
        //查询章
        BmobQuery<count_pian_zhang_gai> query = new BmobQuery<>();
        query.addWhereNotEqualTo("objectId", "cmq");
        //按创建的时间排序
        query.order("createdAt");
        query.findObjects(this, new FindListener<count_pian_zhang_gai>() {
            @Override
            public void onSuccess(List<count_pian_zhang_gai> list) {
                //表示遍历list这个集合
                for (count_pian_zhang_gai zhang : list) {
                    dao.insert(zhang);
                }
            }

            @Override
            public void onError(int i, String s) {
                //删除表，如果数据请求一半出现问题，就把数据给清空
                dao.delAll();
                Toast.makeText(Gui.this, "网络有问题哦！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
