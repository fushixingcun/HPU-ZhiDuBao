package com.hpu.rule.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

/**
 * 自定义的webview，用于监听滑动的位置
 */
public class MyWebView extends WebView {

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.i("DetailActivity", "当前:" + t + "以前" + oldt);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        //设置监听
        if (onOveredScroll != null) {
            onOveredScroll.onOvered(scrollY);
        }
    }


    //自定义一个滚动完毕的监听接口
    onOveredScroll onOveredScroll = null;

    public interface onOveredScroll {
        void onOvered(int scrollY);
    }

    public void setOnOveredScroll(MyWebView.onOveredScroll onOveredScroll) {
        this.onOveredScroll = onOveredScroll;
    }
}
