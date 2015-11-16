package com.hpu.rule;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hpu.rule.bease.BaseActivity;
import com.hpu.rule.bease.Feedback;

import cn.bmob.v3.listener.SaveListener;

/**
 * 用于用户的反馈
 * Created by hjs on 2015/11/8.
 */
public class ActSendFeedback extends BaseActivity implements View.OnClickListener {
    private EditText information_edt;
    private EditText content_edt;
    static String msg1;
    static String msg2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sendfeedback);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);
        information_edt = (EditText) findViewById(R.id.information_edt);
        content_edt = (EditText) findViewById(R.id.content_edt);
    }

    @Override
    public void onClick(View view) {
        String information_String = information_edt.getText().toString();
        String content_String = content_edt.getText().toString();
        if (TextUtils.isEmpty(information_String)) {
            Toast.makeText(this, "请输入你的联系方式", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(content_String)) {
            Toast.makeText(this, "请输入您的建议", Toast.LENGTH_SHORT).show();
        } else {
            if (information_String.equals(msg1) && content_String.equals(msg2)) {
                Toast.makeText(this, "请勿重复提交反馈", Toast.LENGTH_SHORT).show();
            } else {
                msg1 = information_String;
                msg2 = content_String;
                //发送信息给开发者
//               sendMessage(information_String,content_String);
                //发送信息给服务器
                saveFeedbackMsg(msg1, msg2);
                Toast.makeText(this, "您的信息已经发送，谢谢您的参与", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*
    反馈信息发送给服务器
     */
    private void saveFeedbackMsg(String message1, String message2) {
        String message = message1 + message2;
        Feedback feedback = new Feedback();
        feedback.setContent(message);
        feedback.save(this, new SaveListener() {
            @Override
            public void onSuccess() {
                Log.e("feedback.save", "success");
            }

            @Override
            public void onFailure(int i, String s) {
                Log.e("feedback.save", "failure");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //如果点击返回键，后退
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
