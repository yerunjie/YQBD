package com.yqbd.yqbd.usergroupFragments.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.yqbd.yqbd.beans.GroupInfoBean;
import com.yqbd.yqbd.usergroupFragments.GroupAppliedFragment;
import com.yqbd.yqbd.usergroupFragments.GroupJoinedFragment;

import java.util.List;

/**
 * Created by joy12 on 2017/7/25.
 */
public class UserGroupAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"已加入", "申请中"};

    private List<GroupInfoBean> joinedData;
    private List<GroupInfoBean> appliedData;

    public List<GroupInfoBean> getJoinedData() {
        return joinedData;
    }

    public void setJoinedData(List<GroupInfoBean> joinedData) {
        this.joinedData = joinedData;
    }

    public List<GroupInfoBean> getAppliedData() {
        return appliedData;
    }

    public void setAppliedData(List<GroupInfoBean> appliedData) {
        this.appliedData = appliedData;
    }


    public UserGroupAdapter(FragmentManager fm, List<GroupInfoBean> join,  List<GroupInfoBean> apply) {

        super(fm);

        joinedData = join;
        appliedData = apply;

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1){
            return GroupAppliedFragment.newInstance(appliedData);
        }else {
            return GroupJoinedFragment.newInstance(joinedData);
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
