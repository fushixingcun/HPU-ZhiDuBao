package com.hpu.rule;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hpu.rule.adapter.HomeAdapter;
import com.hpu.rule.adapter.MyPagerAdapter;
import com.hpu.rule.bean.Update;
import com.hpu.rule.bease.BaseActivity;
import com.hpu.rule.service.DownLoadService;
import com.hpu.rule.view.Home;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.HttpHandler;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity {
    //主页的ListView
    private ListView listView;
    //viewpager
    private ViewPager mPager;
    //用handler来处理图片的自动滑动
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };
    //初始化主页的ListView
    private List<Home> homeList = new ArrayList<>();
    //小白点
    private ImageView[] dots;
    //小白点的id
    private int[] views = {R.id.iv1, R.id.iv2, R.id.iv3, R.id.iv4, R.id.iv5};
    //更新的路径
    private String apkUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        getActionBar().setDisplayShowHomeEnabled(false);
        setOverflowShowingAlways();
        //查检更新
        update("unclick");

        //得到viewpager的实例
        mPager = (ViewPager) findViewById(R.id.viewPager);
        //初始化小白点
        initDots();
        //定义一个list，用于viewpager图片的显示
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.ligong_nihao);
        list.add(R.mipmap.hpu1);
        list.add(R.mipmap.hpu4);
        list.add(R.mipmap.hpu3);
        list.add(R.mipmap.hpu2);
        //得到pageradapter的实例
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(MainActivity.this, list);
        //设置adapter
        mPager.setAdapter(myPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int mPosition) {
                for (int i = 0; i < 5; i++) {
                    dots[i].setImageResource(R.mipmap.white);
                }
                int positon = mPosition % views.length;
                dots[positon].setImageResource(R.mipmap.blue);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mHandler.sendEmptyMessageDelayed(0, 3000);
        //初始化主页的list数据
        initHome();
        //初始化主页的适配器
        HomeAdapter homeAdapter = new HomeAdapter(MainActivity.this, R.layout.home_item, homeList);
        //初始化ListView
        listView = (ListView) findViewById(R.id.main_listView);
        listView.setAdapter(homeAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //跳转到不同的activity
                Intent itemIntent = new Intent();
                switch (position) {
                    case 0:
                        itemIntent.setClass(getApplicationContext(), HeadMaster.class);
                        MainActivity.this.startActivity(itemIntent);
                        break;
                    case 1:
                        itemIntent.setClass(getApplicationContext(), Authentication.class);
                        MainActivity.this.startActivity(itemIntent);
                        break;
                    case 2:
                        itemIntent.setClass(getApplicationContext(), SchoolHistory.class);
                        MainActivity.this.startActivity(itemIntent);
                        break;
                    case 3:
                        itemIntent.setClass(getApplicationContext(), MyFavorite.class);
                        MainActivity.this.startActivity(itemIntent);
                        break;
                }

            }
        });
    }


    //自动查检是否有最新版本
    private void update(final String click) {
        BmobQuery<Update> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", "zsV7AAAg");
        query.findObjects(this, new FindListener<Update>() {
            @Override
            public void onSuccess(List<Update> list) {
                //比new Message更加节省内存
                Message msg = Message.obtain();
                for (Update update : list) {
                    //getVersionName()表示得到本地的应用程序的版本
                    String versionName = update.getVersionName();
                    if (!versionName.equals(getVersionName())) {
                        apkUrl = update.getPath();
                        msg.obj = true;
                    } else {
                        msg.obj = false;
                    }
                }
                //如果是最新版本，并且手动检查更新
                if (click.equals("click")) {
                    if (!(boolean) msg.obj) {
                        toast("已经是最新版本了");
                    }
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onError(int i, String s) {
                toast("查询更新失败!");
            }
        });
    }

    //初始化小白点
    private void initDots() {
        dots = new ImageView[views.length];
        for (int i = 0; i < views.length; i++) {
            dots[i] = (ImageView) findViewById(views[i]);
        }
    }

    //向listV添加数据
    private void initHome() {
        Home my_favorite = new Home("校长寄语", R.mipmap.headmaster);
        homeList.add(my_favorite);
        Home school_rule = new Home("学生手册", R.mipmap.school_rule);
        homeList.add(school_rule);
        Home school_history = new Home("悠悠校园", R.mipmap.school);
        homeList.add(school_history);
        Home school_master = new Home("我的收藏", R.mipmap.my_favorite);
        homeList.add(school_master);
    }

    private long exitTime = 0;

    //再按一次退出
    // 双击退出
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再次点击退出",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                // 将应用程序在后台运行
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update:
                update("click");
                break;
            case R.id.contact:
                Intent contact_intent = new Intent(getApplicationContext(), ActSendFeedback.class);
                startActivity(contact_intent);
                break;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //让overflow中的选项显示图标
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    //由于手机的不同，ActionBar最右边的overflow按钮有时候显示，有时候不显示，解决办法
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration configuration = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(configuration, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到应用程序的版本名称
     */
    private String getVersionName() {
        // 用来管理手机的APK
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(MainActivity.this, DownLoadService.class);
        stopService(i);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            boolean result = (boolean) msg.obj;
            if (result) {
                showDialgo();
            }
        }
    };

    //提醒更新对话框
    private void showDialgo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // builder.setCancelable(false); 强制升级
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();// 关闭对话框
            }
        });
        builder.setTitle("发现新版本");
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                pd.setMessage("下载进度:" + "0%");
                pd.setButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pd.dismiss();
                    }
                });
                pd.show();
                String url = null;
                // 下载APK，并且替换安装
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    url = Environment
                            .getExternalStorageDirectory()
                            .getAbsolutePath() + "/制度宝.apk";
                } else {
                    url = Environment.getRootDirectory().getAbsolutePath() + "/制度宝.apk";
                }
                // sdcard存在
                FinalHttp fh = new FinalHttp();
                // 调用dowmload开始下载
                HttpHandler hadnler = fh.download(apkUrl, url
                        , new AjaxCallBack<File>() {

                    @Override
                    public void onFailure(Throwable t, int errorNo,
                                          String strMsg) {
                        super.onFailure(t, errorNo, strMsg);
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "对不起下载失败,请稍后再试", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                    @Override
                    public void onLoading(long count, long current) {
                        super.onLoading(count, current);
                        // 当前下载百分比
                        int progress = (int) (current * 100 / count);
                        pd.setMessage("下载进度:" + progress + "%");
                    }

                    @Override
                    public void onSuccess(File t) {
                        super.onSuccess(t);
                        pd.dismiss();
                        installAPK(t);

                    }

                    /**
                     * @param t
                     *            安装APK
                     */
                    private void installAPK(File t) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(t),
                                "application/vnd.android.package-archive");
                        startActivity(intent);
                    }

                });

            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

}
