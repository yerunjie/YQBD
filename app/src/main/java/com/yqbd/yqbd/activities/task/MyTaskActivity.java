package com.yqbd.yqbd.activities.task;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.yqbd.yqbd.Adapter.MyTaskAdapter;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.actions.BaseActionImpl;
import com.yqbd.yqbd.beans.TaskBean;
import com.yqbd.yqbd.utils.BaseJson;
import com.yqbd.yqbd.utils.HttpUtils;
import com.yqbd.yqbd.utils.MyJsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyTaskActivity extends AppCompatActivity {
    private static Handler handler = new Handler();
    private BaseActionImpl baseAction = new BaseActionImpl(this);
    private List<TaskBean> myTasks=new ArrayList<>();
    private RecyclerView myTaskList;
    private MyTaskAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_task);
        Intent intent=getIntent();
        title=(TextView) findViewById(R.id.task_list_title);
        title.setText(intent.getStringExtra("title"));
        myTaskList = (RecyclerView)findViewById(R.id.myTaskList);
        linearLayoutManager=new LinearLayoutManager(this);
        myTaskList.setLayoutManager(linearLayoutManager);
        adapter = new MyTaskAdapter(myTasks,this);
        myTaskList.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyTaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent1=new Intent(MyTaskActivity.this,SingleTaskActivity.class);
                int taskId=myTasks.get(position).getTaskId();
                Bundle bundle=new Bundle();
                bundle.putInt("taskId",taskId);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        });
        new Thread(new Mythread()).start();

    }



    public class Mythread implements Runnable{
        @Override
        public void run(){
            try{
                BaseJson baseJson=getMyTasks(baseAction.getCurrentUserID());
                List<TaskBean> newTasksList = new ArrayList<>();
                JSONArray jsonArray = baseJson.getJSONArray();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tempJsonObject = jsonArray.getJSONObject(i);
                    TaskBean taskBean = MyJsonObject.toBean(tempJsonObject, TaskBean.class);
                    newTasksList.add(taskBean);
                }
                // pageNumber++;
                myTasks.addAll(newTasksList);

            }
            catch (Exception e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MyTaskActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
        public BaseJson getMyTasks(int userId)throws IOException, JSONException {
            Map<String,String>map=new HashMap<>();
            map.put("userId",String.valueOf(userId));

            Bundle bundle=getIntent().getExtras();
            int type=bundle.getInt("tasksType");
            String result;
            if(type==0)
                result= HttpUtils.httpConnectByPost("/task/myTask",map);
            else
                result=HttpUtils.httpConnectByPost("/task/myTaken",map);

            return  new BaseJson(result);
        }
    }
}

