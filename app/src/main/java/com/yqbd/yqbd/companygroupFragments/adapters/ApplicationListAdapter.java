package com.yqbd.yqbd.companygroupFragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.beans.ApplicationBean;
import com.yqbd.yqbd.beans.GroupInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joy12 on 2017/7/26.
 */
public class ApplicationListAdapter extends BaseAdapter {
    private int resourceId;
    private Context context;
    private List<ApplicationBean> applicationList;

    public ApplicationListAdapter(Context context, int textViewResourceId, List<ApplicationBean> objects) {
        this.context = context;
        resourceId = textViewResourceId;
        applicationList = objects;
    }

    @Override
    public int getCount() {
        return applicationList.size();
    }

    @Override
    public Object getItem(int position) {
        return applicationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ApplicationBean applicationBean = (ApplicationBean) getItem(position);
        View view = LayoutInflater.from(context).inflate(resourceId,parent,false);
        TextView groupTitle = (TextView)view.findViewById(R.id.item_application_group);
        TextView candidate = (TextView)view.findViewById(R.id.item_application_candidate);
        groupTitle.append(applicationBean.getGroupTitle());
        candidate.append(applicationBean.getRealName());
        return  view;
    }
}