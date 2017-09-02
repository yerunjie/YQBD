package com.yqbd.yqbd.activities.group;

import android.app.Dialog;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.yqbd.yqbd.BaseActivity;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.actions.GroupActionImpl;
import com.yqbd.yqbd.beans.GroupInfoBean;
import com.yqbd.yqbd.usergroupFragments.GroupAppliedFragment;
import com.yqbd.yqbd.usergroupFragments.GroupJoinedFragment;
import com.yqbd.yqbd.usergroupFragments.adapters.UserGroupAdapter;
import com.yqbd.yqbd.utils.BaseJson;
import com.yqbd.yqbd.utils.DialogUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserGroupActivity extends BaseActivity<Integer> implements GroupAppliedFragment.OnFragmentInteractionListener, GroupJoinedFragment.OnFragmentInteractionListener{
    private GroupActionImpl groupAction = new GroupActionImpl(this);
    private List<GroupInfoBean> joinedList = null;
    private List<GroupInfoBean> appliedList = null;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private UserGroupAdapter userGroupAdapter;
    private TabLayout.Tab one;
    private TabLayout.Tab two;

    private Dialog loadingDialog;
    private boolean isPageChanged = false;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    DialogUtils.closeDialog(loadingDialog);
                    break;
            }
        }
    };

    @Override
    protected void onEventMainThread(BaseJson argument) {
        Log.d("UserGroupActivity","UI...");
        if (argument == null){
            Toast.makeText(this, "发现异常", Toast.LENGTH_SHORT).show();
        }else {
            userGroupAdapter.setJoinedData(joinedList);
            userGroupAdapter.setAppliedData(appliedList);
            userGroupAdapter.notifyDataSetChanged();
            Toast.makeText(this, argument.getErrorMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected BaseJson onEventAsync(Integer argument) {
        Log.d("UserGroupActivity","后台获取数据...");
        BaseJson baseJson = null;
        try {
            if (argument == 0){
                baseJson = groupAction.getJoinedGroups();
                joinedList = new ArrayList<>();
                JSONArray jsonArray = baseJson.getJSONArray();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                    joinedList.add(new GroupInfoBean(tempJsonObject));
                }
            } else if (argument == 1){
                baseJson = groupAction.getAppliedGroups();
                appliedList = new ArrayList<>();
                JSONArray jsonArray = baseJson.getJSONArray();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                    appliedList.add(new GroupInfoBean(tempJsonObject));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        baseJson.setObj(argument);

        return baseJson;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_group);
        //初始化
        netPost(0);
        netPost(1);
        waitLoading();
        initView();
    }

    protected void waitLoading(){
        loadingDialog = DialogUtils.createLoadingDialog(UserGroupActivity.this, "加载中...");
        while (joinedList == null || appliedList == null);
        mHandler.sendEmptyMessageDelayed(1, 0);
    }

    protected void initView() {
        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager= (ViewPager) findViewById(R.id.viewpager_usergroup);
        userGroupAdapter = new UserGroupAdapter(getSupportFragmentManager(),joinedList,appliedList);
        mViewPager.setAdapter(userGroupAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (isPageChanged){
                    isPageChanged = false;
                    netPost(position);
                    waitLoading();
//                    mViewPager.setAdapter(userGroupAdapter);
//                    mViewPager.setCurrentItem(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_SETTLING){
                    isPageChanged = true;
                }
            }
        });

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.tab_usergroup);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
