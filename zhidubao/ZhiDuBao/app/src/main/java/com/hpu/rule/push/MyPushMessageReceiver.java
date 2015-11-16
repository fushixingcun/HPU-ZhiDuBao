package com.hpu.rule.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.bmob.push.PushConstants;

/**
 * 用于推送
 * Created by hjs on 2015/11/8.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)){

        }
    }
}
