package com.yqbd.yqbd.companygroupFragments.adapters;

import android.support.v4.app.*;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.ViewGroup;
import com.yqbd.yqbd.beans.ApplicationBean;
import com.yqbd.yqbd.beans.GroupInfoBean;
import com.yqbd.yqbd.companygroupFragments.OwnedGroupFragment;
import com.yqbd.yqbd.companygroupFragments.UncheckedJoinFragment;
import com.yqbd.yqbd.companygroupFragments.UncheckedQuitFragment;

import java.util.List;
/**
 * Created by joy12 on 2017/7/25.
 */
public class CompanyGroupAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"开设的小组", "加入申请", "退出申请"};

    private List<GroupInfoBean> ownedGroupData;
    private List<ApplicationBean> joinApplicationData;
    private List<ApplicationBean> quitApplicationData;

    public CompanyGroupAdapter(FragmentManager fm, List<GroupInfoBean> ownedGroupData, List<ApplicationBean> joinApplicationData, List<ApplicationBean> quitApplicationData) {
        super(fm);
        this.ownedGroupData = ownedGroupData;
        this.joinApplicationData = joinApplicationData;
        this.quitApplicationData = quitApplicationData;
    }

    public void setOwnedGroupData(List<GroupInfoBean> ownedGroupData) {
        this.ownedGroupData = ownedGroupData;
    }

    public void setJoinApplicationData(List<ApplicationBean> joinApplicationData) {
        this.joinApplicationData = joinApplicationData;
    }

    public void setQuitApplicationData(List<ApplicationBean> quitApplicationData) {
        this.quitApplicationData = quitApplicationData;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return OwnedGroupFragment.newInstance(ownedGroupData);
            case 1:
                return UncheckedJoinFragment.newInstance(joinApplicationData);
            case 2:
                return UncheckedQuitFragment.newInstance(quitApplicationData);
            default:
                return OwnedGroupFragment.newInstance(ownedGroupData);
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
//        Log.d("CompanyGroupAdapter", "initial position="+position);
        switch (position) {
            case 1:
                UncheckedJoinFragment uncheckedJoinFragment = (UncheckedJoinFragment) super.instantiateItem(container, position);
                uncheckedJoinFragment.setApplicationsJoin(joinApplicationData);
                return uncheckedJoinFragment;
            case 2:
                UncheckedQuitFragment uncheckedQuitFragment = (UncheckedQuitFragment) super.instantiateItem(container, position);
                uncheckedQuitFragment.setApplicationsQuit(quitApplicationData);
                return uncheckedQuitFragment;
            default:
                OwnedGroupFragment fragment = (OwnedGroupFragment) super.instantiateItem(container, position);
                fragment.setGroupsOwned(ownedGroupData);
                return fragment;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
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
