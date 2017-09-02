package com.yqbd.yqbd.fragments.filter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.fragments.filter.view.FilterPopupWindow;
import com.yqbd.yqbd.fragments.filter.view.PricePopup;
import com.yqbd.yqbd.fragments.filter.vo.Vo;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class FilterFragment extends Fragment {

    private View main;
    private LinearLayout priceLayout;
    private LinearLayout filterLayout;
    private PricePopup pricePopup;
    private FilterPopupWindow popupWindow;
    private TextView textView;
    private List<Vo> data = new ArrayList<Vo>();

    public FilterFragment() {
        // Required empty public constructor
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(List<String> strings){
        //Toast.makeText(getActivity(),strings.toString(),Toast.LENGTH_LONG).show();
        Log.d("FilterFragment",strings.toString());
        textView.setText(strings.toString());
    }

    public static FilterFragment newInstance() {
        FilterFragment fragment = new FilterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        main = getActivity().findViewById(R.id.main);
        priceLayout = (LinearLayout) getActivity().findViewById(R.id.ranking_price);
        filterLayout = (LinearLayout) getActivity().findViewById(R.id.ranking_filter);
        textView = (TextView) getActivity().findViewById(R.id.textView);
        Vo vo1 = new Vo();
        vo1.setStr1("i3");
        vo1.setStr2("双核双线程");
        Vo vo2 = new Vo();
        vo2.setChecked(true);
        vo2.setStr1("i5");
        vo2.setStr2("双核四线程");
        Vo vo3 = new Vo();
        vo3.setStr1("i7");
        vo3.setStr2("四核八线程");
        data.add(vo1);
        data.add(vo2);
        data.add(vo3);
        // 价格点击监听
        priceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pricePopup = new PricePopup(getActivity(), data);
                pricePopup.showPricePopup(view, data);
            }
        });
        // 筛选点击监听
        filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow = new FilterPopupWindow(getActivity());
                popupWindow.showFilterPopup(main);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
