package com.hpu.rule.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hpu.rule.R;

import java.util.ArrayList;
import java.util.List;

public class TreeViewAdapter extends BaseExpandableListAdapter {


    static public class TreeNode {
        public Object parent;
        public List<String> childs = new ArrayList<>();
        public List<String> childsurl = new ArrayList<>();
    }

    List<TreeNode> treeNodes = new ArrayList<>();
    Context parentContext;

    public TreeViewAdapter(Context context) {
        parentContext = context;

    }

    public List<TreeNode> getTreeNode() {
        return treeNodes;
    }

    public void updateTreeNode(List<TreeNode> nodes) {
        treeNodes = nodes;
    }

    public void removeAll() {
        treeNodes.clear();
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        return treeNodes.get(groupPosition).childs.get(childPosition);
    }
    //获取url,即是内容
    public String getChildUrl(int groupPosition, int childPosition) {
        return treeNodes.get(groupPosition).childsurl.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parentContext, R.layout.childs_view, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText((String) (treeNodes.get(groupPosition).childs.get(childPosition)));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return treeNodes.get(groupPosition).childs.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return treeNodes.get(groupPosition).parent;
    }

    @Override
    public int getGroupCount() {
        return treeNodes.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parentContext, R.layout.group_view, null);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv);
        tv.setText((String) (treeNodes.get(groupPosition).parent));
        return convertView;
    }
    //是否指定分组视图及其子视图的ID对应的后台数据改变也会保持该ID.
    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
