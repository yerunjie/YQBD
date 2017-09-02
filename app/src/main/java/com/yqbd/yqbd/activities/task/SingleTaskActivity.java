package com.yqbd.yqbd.activities.task;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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
import java.util.HashMap;
import java.util.Map;

public class SingleTaskActivity extends AppCompatActivity {
    private static Handler handler = new Handler();
    private BaseActionImpl baseAction=new BaseActionImpl(SingleTaskActivity.this);
    private Button taskBtn;
    private TextView taskTitle;
    private TextView taskDescription;
    private TaskBean taskBean=null;
    private boolean isMyPublished=false;
    private boolean isMyTaken=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_task);
        new Thread(new MyThread()).start();
        taskTitle=(TextView)findViewById(R.id.task_title);
        taskDescription=(TextView)findViewById(R.id.task_description);
        taskBtn=(Button)findViewById(R.id.task_button);
        taskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMyPublished==true){
                    new Thread(new CancelPublishThread()).start();
                    Intent intent=new Intent(SingleTaskActivity.this,MyTaskActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putInt("tasksType",0);
                    intent.putExtras(bundle);
                    intent.putExtra("title","我发布的任务");
                    startActivity(intent);
                }
                else if(isMyTaken==true){
                    new Thread(new CancelTakeThread()).start();
                    isMyTaken=false;
                    taskBtn.setText("接受任务");
                }
                else{
                    new Thread(new TakeThread()).start();
                    isMyTaken=true;
                    taskBtn.setText("取消接受");
                }
            }
        });
    }

    public class MyThread implements Runnable {
        @Override
        public void run() {
            BaseJson baseJson = getTask();
            JSONObject jsonObject=baseJson.getJSONObject();
            taskBean = MyJsonObject.toBean(jsonObject, TaskBean.class);
            if(taskBean.getUserId()==baseAction.getCurrentUserID()){
                isMyPublished=true;
            }
            isMyTaken=isMyTaken(baseAction.getCurrentUserID(),taskBean.getTaskId());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    taskTitle.setText(taskBean.getTaskTitle());
                    taskDescription.setText(taskBean.getTaskDescription());
                    if(isMyPublished==true) taskBtn.setText("取消发布");
                    else if(isMyTaken==true) taskBtn.setText("取消接受");
                    else taskBtn.setText("接受任务");                }
            });

        }
    }

    public class CancelPublishThread implements Runnable{
        @Override
        public void run(){
            Map<String,String> map=new HashMap<>();
            map.put("taskId",String.valueOf(taskBean.getTaskId()));
            try {
                String result=HttpUtils.httpConnectByPost("/task/cancelPublishedTask",map);
            } catch (IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SingleTaskActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public class CancelTakeThread implements Runnable{
        @Override
        public void run(){
            Map<String,String> map=new HashMap<>();
            map.put("taskId",String.valueOf(taskBean.getTaskId()));
            map.put("userId",String.valueOf(baseAction.getCurrentUserID()));
            try {
                String result=HttpUtils.httpConnectByPost("/task/cancelTaken",map);
            } catch (IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SingleTaskActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public class TakeThread implements Runnable{
        @Override
        public void run(){
            Map<String,String> map=new HashMap<>();
            map.put("taskId",String.valueOf(taskBean.getTaskId()));
            map.put("userId",String.valueOf(baseAction.getCurrentUserID()));
            try {
                String result=HttpUtils.httpConnectByPost("/task/takeTask",map);
            } catch (IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SingleTaskActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public BaseJson getTask() {
        Intent intent = getIntent();
        int taskId = intent.getExtras().getInt("taskId");
        Map<String, String> map = new HashMap<>();
        map.put("taskId", String.valueOf(taskId));
        String result = null;
        try {
            result = HttpUtils.httpConnectByPost("/task/getTaskById", map);
        } catch (IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SingleTaskActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
                }
            });
        }
        BaseJson baseJson=null;
        try {
            baseJson=new BaseJson(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return baseJson;
    }

    public boolean isMyTaken(int userId,int taskId){
        boolean isTaken=false;
        Map<String,String> map=new HashMap<>();
        map.put("userId",String.valueOf(userId));
        map.put("taskId",String.valueOf(taskId));
        String result=null;
        try {
            result=HttpUtils.httpConnectByPost("/task/isTaken",map);
        } catch (IOException e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SingleTaskActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
                }
            });
        }
        System.out.println(result);
        if(result.equals("true")) isTaken=true;
        else isTaken=false;
        return isTaken;
    }
}
