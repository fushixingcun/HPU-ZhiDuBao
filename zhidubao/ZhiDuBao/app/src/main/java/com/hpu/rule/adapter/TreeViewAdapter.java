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

    public static final int ItemHeight = 48;
    public static final int PaddingLeft = 38;
    private int myPaddingLeft = 0;

    static public class TreeNode {
        public Object parent;
        public List<Object> childs = new ArrayList<>();
    }

    List<TreeNode> treeNodes = new ArrayList<>();
    Context parentContext;

    public TreeViewAdapter(Context context, int myPaddingLeft) {
        parentContext = context;
        this.myPaddingLeft = myPaddingLeft;

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
    public Object getChild(int groupPosition, int childPosition) {
        return treeNodes.get(groupPosition).childs.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parentContext, R.layout.group_view, null);
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

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
