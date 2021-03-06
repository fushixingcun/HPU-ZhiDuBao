package com.hpu.rule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hpu.rule.adapter.TreeViewAdapter;
import com.hpu.rule.bean.Update;
import com.hpu.rule.bean.count_pian_gai;
import com.hpu.rule.bean.count_pian_zhang_gai;
import com.hpu.rule.bease.BaseActivity;
import com.hpu.rule.utils.DBUtils;
import com.hpu.rule.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class SchoolRule extends BaseActivity implements ExpandableListView.OnChildClickListener {
    private ExpandableListView expandableListView;
    private TreeViewAdapter adapter;
    //用于加粗改字体
    private TextView school_TextView;
    //用于放置所有篇的名字，本地的
    public List<String> groups = new ArrayList<>();
    //所有篇的名字，查询得到的
    private final static int NETGROUP = 0;
    private List<count_pian_zhang_gai> count_pian1_zhangs;
    //实体信息
    private List<TreeViewAdapter.TreeNode> treeNode;
    private TextView actionbar_SchoolRule_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setTitle("返回");
        View actionBar_layout = LayoutInflater.from(this).inflate(R.layout.actionbar_layout, null);
        getActionBar().setCustomView(actionBar_layout);
        actionbar_SchoolRule_Text = (TextView) findViewById(R.id.actionbar_Text);
        actionbar_SchoolRule_Text.setText("学生手册");

        //加粗字体
        school_TextView = (TextView) findViewById(R.id.school_TextView);
        TextPaint tp = school_TextView.getPaint();
        tp.setFakeBoldText(true);

        //产看时候有文档更新
        update();
        //请求数据
        acquireData();
    }

    private void update() {
        BmobQuery<Update> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", "zsV7AAAg");
        query.findObjects(this, new FindListener<Update>() {
            @Override
            public void onSuccess(List<Update> list) {
                for (Update update : list) {
                    if (update.isWord_update()) {
                        toast("校规已经更新了，请点击右上角更新哦！");
                    }

                }
            }

            @Override
            public void onError(int i, String s) {
            }
        });
    }

    //读取数据库的信息
    private void fillDate() {
        //判断网络状态，选择不同的数据库信息
        boolean isConnected = NetUtils.getNetStatus(this);
        if (isConnected) {
            count_pian1_zhangs = dao.queryAll();
        } else {
            DBUtils utils = new DBUtils();
            count_pian1_zhangs = utils.getOffInfo();
        }

    }

    //获取篇章数据
    private void acquireData() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("加载中...");
        pd.show();
        //查询篇
        BmobQuery<count_pian_gai> query = new BmobQuery<>();
        query.addWhereNotEqualTo("objectId", "ud");
        //缓存1天
        query.order("createdAt");
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
        boolean isCache = query.hasCachedResult(this, count_pian_gai.class);
        if (isCache) {
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        } else {
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        //获取所有篇的表
        query.findObjects(this, new FindListener<count_pian_gai>() {
            @Override
            public void onSuccess(List<count_pian_gai> list) {
                pd.dismiss();
                //netgroups用于放篇的名字
                List<String> netgroups = new ArrayList<>();
                for (count_pian_gai pian : list) {
                    netgroups.add(pian.getContent());
                }
                Message msg = Message.obtain();
                msg.obj = netgroups;
                msg.what = NETGROUP;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(int i, String s) {
                pd.dismiss();
                //如果没有联网，则手动添加数据
                List<String> netgroups = new ArrayList<>();
                netgroups.add("第一篇  学籍与教学管理");
                netgroups.add("第二篇  学生管理");
                netgroups.add("第三篇  学生奖励");
                netgroups.add("第四篇  素质拓展");
                netgroups.add("第五篇  资助·就业·医疗");
                Message msg = Message.obtain();
                msg.obj = netgroups;
                msg.what = NETGROUP;
                handler.sendMessage(msg);

            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NETGROUP:
                    List<String> netgroups = (List<String>) msg.obj;
                    for (String group : netgroups) {
                        groups.add(group);
                    }
                    break;
            }
            //数据获取完毕以后再查询
            initView();
        }
    };

    //初始化树状图
    private void initView() {
        //查看本地是否有数据
        fillDate();
        expandableListView = (ExpandableListView) findViewById(R.id.expand_list);
        adapter = new TreeViewAdapter(this);
        treeNode = adapter.getTreeNode();
        for (int i = 0; i < groups.size(); i++) {
            TreeViewAdapter.TreeNode node = new TreeViewAdapter.TreeNode();
            node.parent = groups.get(i);
            //如果从数据库查询的篇的名字和group的相等的话，就获取章的名字和内容
            if (count_pian1_zhangs != null) {
                for (count_pian_zhang_gai zhang : count_pian1_zhangs) {
                    if (zhang.getPian_name().equals(groups.get(i))) {
                        node.childs.add(zhang.getZhang_name());
                        node.childsurl.add(zhang.getContent());
                    }
                }
            } else {
                Toast.makeText(SchoolRule.this, "获取离线数据失败", Toast.LENGTH_SHORT).show();
            }
            treeNode.add(node);
        }
        adapter.updateTreeNode(treeNode);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnChildClickListener(this);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Intent i = new Intent(this, DetailActivity.class);
        i.putExtra("url", adapter.getChildUrl(groupPosition, childPosition));
        i.putExtra("pian_name", adapter.getChild(groupPosition, childPosition));
        startActivity(i);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                dao.delAll();
                //重新请求数据
                acquireDataZhang();
                break;
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void acquireDataZhang() {
        //查询章
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("重新获取数据中...");
        pd.show();
        BmobQuery<count_pian_zhang_gai> query = new BmobQuery<>();
        query.addWhereNotEqualTo("objectId", "cBeq");
        query.order("createdAt");
        query.findObjects(this, new FindListener<count_pian_zhang_gai>() {
            @Override
            public void onSuccess(List<count_pian_zhang_gai> list) {
                for (count_pian_zhang_gai zhang : list) {
                    dao.insert(zhang);
                }
                toast("刷新成功!");
                pd.dismiss();
            }

            @Override
            public void onError(int i, String s) {
                //删除表
                pd.dismiss();
                dao.delAll();
                Toast.makeText(SchoolRule.this, "网络有问题哦！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.school_rule, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
