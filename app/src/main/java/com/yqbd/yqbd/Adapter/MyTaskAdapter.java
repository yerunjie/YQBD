package com.yqbd.yqbd.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.beans.TaskBean;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by 佳乐 on 2017/7/17.
 */
public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.ViewHolder> implements View.OnClickListener{
    private List<TaskBean> myTaskList;
    private Activity activity;
    private OnItemClickListener mOnItemClickListener = null;
    static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView companyImage;
        private TextView nameText;
        private TextView descriptionText;
        private TextView timeText;
        private TextView statusText;
        private Button commentButton;
        private View view;
        public ViewHolder(View view) {
            super(view);

            companyImage = (ImageView) view.findViewById(R.id.companyImage);
            nameText = (TextView) view.findViewById(R.id.nameText);
            descriptionText = (TextView) view.findViewById(R.id.DescriptionText);
            timeText = (TextView) view.findViewById(R.id.timeText);
            statusText = (TextView) view.findViewById(R.id.statusText);
            commentButton = (Button) view.findViewById(R.id.commentButton);
            this.view=view;
        }

    }

    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public MyTaskAdapter(List<TaskBean> myTaskList, Activity activity) {
        this.myTaskList = myTaskList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_task_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TaskBean taskItem = myTaskList.get(position);
        System.out.println("name:"+taskItem.getTaskTitle());
        holder.view.setTag(position);
        System.out.println("position:"+position);
        holder.descriptionText.setText(taskItem.getTaskDescription());
        holder.nameText.setText(taskItem.getTaskTitle());
        String time=new Timestamp(taskItem.getPublishTime()).toString();
        System.out.println(time);
        holder.timeText.setText(time);
        switch(taskItem.getTaskStatus()) {
            case 0:
                holder.statusText.setText("未完成");
                holder.commentButton.setVisibility(View.GONE);
                break;
            case 1:
                holder.statusText.setText("进行中");
                holder.commentButton.setVisibility(View.GONE);
                break;
            case 2:
                holder.statusText.setText("已完成,待评价");
                holder.commentButton.setText("去评价");
                break;
            default:
                holder.statusText.setText("已结束");
                holder.commentButton.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return myTaskList.size();
    }

}
