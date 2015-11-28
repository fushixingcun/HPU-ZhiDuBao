package com.hpu.rule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hpu.rule.adapter.TreeViewAdapter;
import com.hpu.rule.bean.count_pian_gai;
import com.hpu.rule.bean.count_pian_zhang_gai;
import com.hpu.rule.bease.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class SchoolRule extends BaseActivity implements ExpandableListView.OnChildClickListener {
    private ExpandableListView expandableListView;
    private TreeViewAdapter adapter;

    public List<String> groups = new ArrayList<>();
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
        actionbar_SchoolRule_Text.setText("校规校纪");
        fillDate();
        acquireData();

    }

    //读取数据库的信息
    private void fillDate() {
        count_pian1_zhangs = dao.queryAll();
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
        query.findObjects(this, new FindListener<count_pian_gai>() {
            @Override
            public void onSuccess(List<count_pian_gai> list) {
                pd.dismiss();
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
                Toast.makeText(SchoolRule.this, i + s, Toast.LENGTH_SHORT).show();
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
        expandableListView = (ExpandableListView) findViewById(R.id.expand_list);
        adapter = new TreeViewAdapter(this);
        treeNode = adapter.getTreeNode();
        for (int i = 0; i < groups.size(); i++) {
            TreeViewAdapter.TreeNode node = new TreeViewAdapter.TreeNode();
            node.parent = groups.get(i);
            for (count_pian_zhang_gai zhang : count_pian1_zhangs) {
                if (zhang.getPian_name().equals(groups.get(i))) {
                    node.childs.add(zhang.getZhang_name());
                    node.childsurl.add(zhang.getContent());
                }
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
