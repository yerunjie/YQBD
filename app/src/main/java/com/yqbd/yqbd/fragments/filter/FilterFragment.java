package com.yqbd.yqbd.fragments.filter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.yqbd.yqbd.Adapter.MyTaskAdapter;
import com.yqbd.yqbd.BaseFragment;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.actions.BaseActionImpl;
import com.yqbd.yqbd.beans.TaskBean;
import com.yqbd.yqbd.beans.TypeBean;
import com.yqbd.yqbd.fragments.filter.view.FilterPopupWindow;
import com.yqbd.yqbd.fragments.filter.view.PricePopup;
import com.yqbd.yqbd.fragments.filter.vo.Vo;
import com.yqbd.yqbd.utils.BaseJson;
import com.yqbd.yqbd.utils.HttpUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterFragment extends BaseFragment<TypeBean> {
    private static Handler handler = new Handler();
    private View main;
    private LinearLayout priceLayout;
    private LinearLayout filterLayout;
    private PricePopup pricePopup;
    private FilterPopupWindow popupWindow;
    private JSONArray jsonArray;

    @Override
    protected void onEventMainThread(BaseJson argument) {

    }

    @Override
    protected BaseJson onEventAsync(TypeBean argument) {
        return null;
    }

    private TextView textView;
    private BaseActionImpl baseAction = new BaseActionImpl(getActivity());
    private List<TaskBean> tasks = new ArrayList<>();
    private RecyclerView taskList;
    private MyTaskAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Vo> data = new ArrayList<Vo>();

    public FilterFragment() {
        // Required empty public constructor
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(List<String> strings) {
        //Toast.makeText(getActivity(),strings.toString(),Toast.LENGTH_LONG).show();
        Log.d("FilterFragment", strings.toString());
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
        localPost(new String("初始搜索"));
        filterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow = new FilterPopupWindow(getActivity(), jsonArray);
                popupWindow.showFilterPopup(main);
            }
        });

        taskList = (RecyclerView) getActivity().findViewById(R.id.taskList);
        linearLayoutManager = new LinearLayoutManager(this.getActivity());
        taskList.setLayoutManager(linearLayoutManager);
        adapter = new MyTaskAdapter(tasks, getActivity());
        taskList.setAdapter(adapter);
        localPost(new String("初始界面"));
        //       new Thread(new MyThread()).start();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(String temp) {
        switch (temp) {
            case "初始界面":
                try {
                    BaseJson baseJson = getAllTasks();
                    List<TaskBean> newTasksList = new ArrayList<>();
                    JSONArray jsonArray = baseJson.getJSONArray();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                        newTasksList.add(TaskBean.newInstance(tempJsonObject));
                    }
                    // pageNumber++;
                    tasks.addAll(newTasksList);
                    localPost(new Integer(1));
                } catch (Exception e) {
                    localPost(new Integer(2));
                }
                break;
            case "初始搜索":
                try {
                    BaseJson baseJson =  getAllSearchTypes();
                    System.out.print(baseJson);
                    jsonArray=baseJson.getJSONArray();
                } catch (Exception e) {
                    localPost(new Integer(2));
                }
                break;
            default:
                try {
                    BaseJson baseJson = getSearch(temp);
                    List<TaskBean> newTasksList = new ArrayList<>();
                    JSONArray jsonArray = baseJson.getJSONArray();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                        newTasksList.add(TaskBean.newInstance(tempJsonObject));
                    }
                    // pageNumber++;
                    tasks.clear();
                    tasks.addAll(newTasksList);
                    localPost(new Integer(1));
                } catch (Exception e) {
                    localPost(new Integer(2));
                }


        }
    }

    public BaseJson getAllTasks() throws IOException, JSONException {
        Map<String, String> map = new HashMap<>();
        String result = HttpUtils.httpConnectByPost("/task/getAllTasks", map);
        return new BaseJson(result);
    }

    public BaseJson getAllSearchTypes() throws IOException, JSONException {
        Map<String, String> map = new HashMap<>();
        String result = HttpUtils.httpConnectByPost("/task/getSearchTypes", map);
        return new BaseJson(result);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Integer integer) {
        switch (integer) {
            case 1:
                adapter.notifyDataSetChanged();
                break;
            case 2:
                Toast.makeText(getActivity(), "连接错误", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public BaseJson getSearch(String  str) throws IOException, JSONException {
        Map<String,String>map = new HashMap<>();

        map.put("map",str);
        System.out.println(str);
        String result = HttpUtils.httpConnectByPost("/task/getSearch",map);
        return new BaseJson(result);
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
    public static void postSearch(List<String> list)
    {
        String tmp="";
        int cas=1;
        for(String kkk:list)
        {
            if(cas!=1)
                tmp+=",";
            tmp+=kkk;
            cas++;
        }
        EventBus.getDefault().post(tmp);
        // System.out.println("fragment:"+map);
    }
}
