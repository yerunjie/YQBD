package com.yqbd.yqbd.usergroupFragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.beans.GroupInfoBean;

import java.util.List;

/**
 * Created by joy12 on 2017/7/25.
 */
public class GroupListAdapter extends ArrayAdapter<GroupInfoBean> {
    private int resourceId;
    private List<GroupInfoBean> data;

    public GroupListAdapter(Context context, int textViewResourceId, List<GroupInfoBean> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        data = objects;
    }

    public List<GroupInfoBean> getData() {
        return data;
    }

    public void setData(List<GroupInfoBean> data) {
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupInfoBean groupInfoBean = data.get(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView groupTitle = (TextView)view.findViewById(R.id.item_group_title);
        groupTitle.setText(groupInfoBean.getGroupTitle());
        return  view;
    }
}
