package com.hpu.rule.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 判断是否联网的工具类
 */
public class NetUtils {
    public static boolean getNetStatus(Context context) {
        boolean result = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo = manager.getAllNetworkInfo();
        if (networkInfo != null && networkInfo.length > 0)
        {
            for (NetworkInfo aNetworkInfo : networkInfo) {
                // 判断当前网络状态是否为连接状态
                if (aNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    result = true;
                }
            }
        }
        return result;
    }
}
