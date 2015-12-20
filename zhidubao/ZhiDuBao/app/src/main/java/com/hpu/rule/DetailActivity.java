package com.hpu.rule;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.hpu.rule.bease.BaseActivity;
import com.hpu.rule.view.MyWebView;

public class DetailActivity extends BaseActivity {
    //自定义的webview
    private MyWebView mWebView;

    private ProgressBar pbProgress;
    //要显示的内容地址
    private String url;
    //是否收藏
    private boolean hasCollect;

    private String pian_name;
    //保存的位置
    private int curPos = 0;
    private LocationManager myLocationManager = null;
    private static final String APP_CACAHE_DIRNAME = "/webcache";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setTitle("返回");

        initview();
        //初始化数据库数据
        initData();
    }

    private void initview() {
        url = getIntent().getStringExtra("url");
        pian_name = getIntent().getStringExtra("pian_name");
        String s = getIntent().getStringExtra("search");
        mWebView = (MyWebView) findViewById(R.id.wv_web);

        initWebView();

        //得到当前的位置
        myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        pbProgress = (ProgressBar) findViewById(R.id.pb_progress);
        mWebView.loadUrl(url);// 加载网页

    }

    //初始化webview的设置
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);// 表示支持js
        settings.setBuiltInZoomControls(true);// 显示放大缩小按钮
        settings.setUseWideViewPort(true);// 支持双击缩放
        //提高渲染的优先级
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        // 开启 DOM storage API 功能
        settings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        settings.setDatabaseEnabled(true);
        //设置缓存
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        //设置数据库缓存路径
        settings.setDatabasePath(cacheDirPath);
        //设置  Application Caches 缓存目录
        settings.setAppCachePath(cacheDirPath);
        //开启 Application Caches 功能
        settings.setAppCacheEnabled(true);
        //设置滚动位置监听
        mWebView.setOnOveredScroll(new MyWebView.onOveredScroll() {
            @Override
            public void onOvered(int scrollY) {
                curPos = scrollY;
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {

            /**
             * 网页开始加载
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbProgress.setVisibility(View.VISIBLE);
            }

            /**
             * 网页加载结束
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbProgress.setVisibility(View.GONE);
                //延时0.1秒跳转到上次的位置
                handler.sendEmptyMessageDelayed(0,100);
            }

            /**
             * 所有跳转的链接都会在此方法中回调
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {

            /**
             * 进度发生变化
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            /**
             * 获取网页标题
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

    private void initData() {
        hasCollect = urldao.querryUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.size:
                //显示选择对话框
                showChooseDialog();
                break;
            case R.id.collect:
                //判断是否已经收藏
                hasCollect = urldao.querryUrl(url);
                if (hasCollect) {
                    item.setIcon(R.mipmap.ic_action_important);
                    urldao.del(url);
                    toast("取消收藏!");
                } else {
                    item.setIcon(R.mipmap.ic_action_favor_on_pressed);
                    urldao.insert(url, pian_name, curPos);
                    toast("已经收藏了!");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int mCurrentChooseItem;// 记录当前选中的item, 点击确定前
    private int mCurrentItem = 2;// 记录当前选中的item, 点击确定后

    /**
     * 选择字体大小
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体",
                "超小号字体"};
        builder.setTitle("字体设置");
        builder.setSingleChoiceItems(items, mCurrentItem,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCurrentChooseItem = which;
                    }
                });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = mWebView.getSettings();
                switch (mCurrentChooseItem) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;

                    default:
                        break;
                }

                mCurrentItem = mCurrentChooseItem;
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem item = menu.getItem(1);
        if (hasCollect) {
            item.setIcon(R.mipmap.ic_action_favor_on_pressed);
        } else {
            item.setIcon(R.mipmap.ic_action_important);
        }
        return super.onCreateOptionsMenu(menu);
    }
    //在收藏中自动滚动到上次滑动的位置
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            curPos = getIntent().getIntExtra("position", 0);
            //自动滚动到上次的位置
            mWebView.scrollTo(0, curPos);
        }
    };
}
