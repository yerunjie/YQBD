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
import com.yqbd.yqbd.beans.ApplicationBean;
import com.yqbd.yqbd.beans.GroupInfoBean;
import com.yqbd.yqbd.companygroupFragments.OwnedGroupFragment;
import com.yqbd.yqbd.companygroupFragments.UncheckedJoinFragment;
import com.yqbd.yqbd.companygroupFragments.UncheckedQuitFragment;
import com.yqbd.yqbd.companygroupFragments.adapters.CompanyGroupAdapter;
import com.yqbd.yqbd.utils.BaseJson;
import com.yqbd.yqbd.utils.DialogUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompanyGroupActivity extends BaseActivity<Integer> implements OwnedGroupFragment.OnFragmentInteractionListener,UncheckedJoinFragment.OnFragmentInteractionListener,UncheckedQuitFragment.OnFragmentInteractionListener{
    private GroupActionImpl groupAction = new GroupActionImpl(this);
    private List<GroupInfoBean> ownedGroupList = null;
    private List<ApplicationBean> joinList = null;
    private List<ApplicationBean> quitList = null;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private CompanyGroupAdapter companyGroupAdapter;
    private TabLayout.Tab one;
    private TabLayout.Tab two;
    private TabLayout.Tab three;

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
        if (argument == null){
            Toast.makeText(this, "发现异常", Toast.LENGTH_SHORT).show();
        }else {
            Log.d("CompanyGroupActivity","UI...");
//            companyGroupAdapter = new CompanyGroupAdapter(getSupportFragmentManager(),ownedGroupList,joinList,quitList);
            companyGroupAdapter.setOwnedGroupData(ownedGroupList);
            companyGroupAdapter.setJoinApplicationData(joinList);
            companyGroupAdapter.setQuitApplicationData(quitList);
            companyGroupAdapter.notifyDataSetChanged();
            Toast.makeText(this, argument.getErrorMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected BaseJson onEventAsync(Integer argument) {
        Log.d("CompanyGroupActivity","后台获取数据..." + "arg=" + argument);
        BaseJson baseJson = null;
        try {
            if (argument == 0){
                baseJson = groupAction.getCompanyGroups();
                ownedGroupList = new ArrayList<>();
                JSONArray jsonArray = baseJson.getJSONArray();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                    ownedGroupList.add(new GroupInfoBean(tempJsonObject));
                }
                Log.d("CompanyGroupActivity","ownedGroupList size=" + ownedGroupList.size());
            } else {
                baseJson = groupAction.getUncheckedApplicationsByStatus(argument);
                if (argument == 1){
                    joinList = new ArrayList<>();
                    JSONArray jsonArray = baseJson.getJSONArray();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                        joinList.add(new ApplicationBean(tempJsonObject));
                    }
                    Log.d("CompanyGroupActivity","joinList size=" + joinList.size());
                }else if (argument == 2){
                    quitList = new ArrayList<>();
                    JSONArray jsonArray = baseJson.getJSONArray();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                        quitList.add(new ApplicationBean(tempJsonObject));
                    }
                    Log.d("CompanyGroupActivity","quitList size=" + quitList.size());
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
        setContentView(R.layout.activity_company_group);

        //初始化
        netPost(0);
        netPost(1);
        netPost(2);
        waitLoading();
        initView();
    }


    protected void waitLoading(){
//        loadingDialog = DialogUtils.createLoadingDialog(CompanyGroupActivity.this, "加载中...");
//        loadingDialog.show();
        while (ownedGroupList == null || joinList == null || quitList == null);
        // Log.d("wait", "waiting");
//        mHandler.sendEmptyMessageDelayed(1, 0);
    }

    protected void initView() {
        //使用适配器将ViewPager与Fragment绑定在一起
        mViewPager= (ViewPager) findViewById(R.id.viewpager_companygroup);
        companyGroupAdapter = new CompanyGroupAdapter(getSupportFragmentManager(),ownedGroupList,joinList,quitList);
        mViewPager.setAdapter(companyGroupAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                if (isPageChanged){
//                    isPageChanged = false;
                if (position == 0) ownedGroupList = null;
                else if (position == 1) joinList = null;
                else if (position == 2) quitList = null;
                    netPost(position);
                    waitLoading();

//                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_SETTLING){

                }
            }
        });

        //将TabLayout与ViewPager绑定在一起
        mTabLayout = (TabLayout) findViewById(R.id.tab_companygroup);
        mTabLayout.setupWithViewPager(mViewPager);

        //指定Tab的位置
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
