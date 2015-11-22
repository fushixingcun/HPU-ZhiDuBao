package com.hpu.rule;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hpu.rule.adapter.TreeViewAdapter;
import com.hpu.rule.bean.count_pian;
import com.hpu.rule.bean.count_pian_zhang;
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
    private List<count_pian_zhang> count_pian1_zhangs;
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
        BmobQuery<count_pian> query = new BmobQuery<>();
        query.addWhereNotEqualTo("objectId", "ud2V11V");
        //缓存1天
        query.order("createdAt");
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
        boolean isCache = query.hasCachedResult(this, count_pian.class);
        if (isCache) {
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        } else {
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
        }
        query.findObjects(this, new FindListener<count_pian>() {
            @Override
            public void onSuccess(List<count_pian> list) {
                pd.dismiss();
                List<String> netgroups = new ArrayList<>();
                for (count_pian pian : list) {
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
            for (count_pian_zhang zhang : count_pian1_zhangs) {
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
        startActivity(i);
        return false;
    }
}
