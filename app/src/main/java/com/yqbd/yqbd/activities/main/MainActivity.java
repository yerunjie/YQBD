package com.yqbd.yqbd.activities.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.yqbd.yqbd.BaseActivity;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.actions.BaseActionImpl;
import com.yqbd.yqbd.fragments.PersonalFragment;
import com.yqbd.yqbd.fragments.TestFragment;
import com.yqbd.yqbd.fragments.filter.FilterFragment;
import com.yqbd.yqbd.utils.BaseJson;

import java.util.ArrayList;

public class MainActivity extends BaseActivity<Integer> implements BottomNavigationBar.OnTabSelectedListener {
    private Fragment oldFragment, fragment;
    private BaseActionImpl baseAction=new BaseActionImpl(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void initView() {
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        BadgeItem numberBadgeItem = new BadgeItem()
                .setBorderWidth(4)
                .setBackgroundColor(Color.RED)
                .setText("3")
                .setHideOnSelect(true);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.bottom_my, "主页").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.bottom_my, "历史订单").setActiveColorResource(R.color.colorPrimary))
                .addItem(new BottomNavigationItem(R.drawable.bottom_my, "个人信息").setActiveColorResource(R.color.colorPrimary))
                .setFirstSelectedPosition(0)
                .initialise();

        //fragments = getFragments();
        //setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);
        onTabSelected(0);
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(TestFragment.newInstance());
        fragments.add(FilterFragment.newInstance());
        fragments.add(PersonalFragment.newInstance(1));
        return fragments;
    }

    @Override
    protected void onEventMainThread(BaseJson argument) {

    }

    @Override
    protected BaseJson onEventAsync(Integer argument) {
        return null;
    }

    @Override
    public void onTabSelected(int position) {
        /*if (fragments != null) {
            if (position < fragments.size()) {*/
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        //Fragment fragment=null; //= fragments.get(position);
        oldFragment = fragment;
        switch (position) {
            case 0:
                fragment = TestFragment.newInstance();
                break;
            case 1:
                fragment = FilterFragment.newInstance();
                break;
            case 2:
                fragment = PersonalFragment.newInstance(baseAction.getCurrentUserID());
                break;
        }
        //fragments.remove(position);
        //fragments.add(position,fragment);
        if (fragment.isAdded()) {
            ft.replace(R.id.layFrame, fragment);
        } else {
            ft.add(R.id.layFrame, fragment);
        }
        ft.commitAllowingStateLoss();
         /*   }
        }*/

    }

    @Override
    public void onTabUnselected(int position) {
        /*if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }*/
        if (oldFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.remove(oldFragment);
            ft.commitAllowingStateLoss();
        }

    }

    @Override
    public void onTabReselected(int position) {

    }
}
