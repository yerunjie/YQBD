package com.yqbd.yqbd.beans;

import com.yqbd.yqbd.tagview.widget.Tag;
import com.yqbd.yqbd.utils.MyJsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 11022 on 2017/7/17.
 */
public class TaskBean {
    private Integer taskId;
    private Integer userId;
    private Integer companyId;
    private String taskTitle;
    private String taskDescription;
    private String taskAddress;
    private Double pay;
    private Integer taskStatus;
    private Long publishTime;
    private Long completeTime;
    private Integer maxPeopleNumber;
    private Long deadline;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String simpleDrawingAddress;
    private Integer isGroup;
    private ArrayList<TypeBean> typeBeans;
    private String name;

    public TaskBean(String taskTitle, String taskDescription, String taskAddress, Double pay, Integer maxPeopleNumber, Date date, List<Tag> tags) {
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskAddress = taskAddress;
        this.maxPeopleNumber = maxPeopleNumber;
        this.pay = pay;
        this.deadline = date.getTime();
        typeBeans = new ArrayList<>();
        for (Tag tag : tags) {
            if(tag.getOr()){
                typeBeans.add(new TypeBean(tag));
            }
        }
    }
    public TaskBean(){}
    public static TaskBean newInstance(JSONObject jsonObject) throws  JSONException{
       TaskBean taskBean = MyJsonObject.toBean(jsonObject, TaskBean.class);
       taskBean.setTypeBeans(new ArrayList<TypeBean>());
        JSONArray jsonArray = jsonObject.getJSONArray("typeBeans");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tempJsonObject = jsonArray.getJSONObject(i);
            taskBean.getTypeBeans().add(new TypeBean(tempJsonObject));
        }
        return taskBean;
    }
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskAddress() {
        return taskAddress;
    }

    public void setTaskAddress(String taskAddress) {
        this.taskAddress = taskAddress;
    }

    public Double getPay() {
        return pay;
    }

    public void setPay(Double pay) {
        this.pay = pay;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Long publishTime) {
        this.publishTime = publishTime;
    }

    public Long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Long completeTime) {
        this.completeTime = completeTime;
    }

    public ArrayList<TypeBean> getTypeBeans() {
        return typeBeans;
    }

    public void setTypeBeans(ArrayList<TypeBean> typeBeans) {
        this.typeBeans = typeBeans;
    }

    public Integer getMaxPeopleNumber() {
        return maxPeopleNumber;
    }

    public void setMaxPeopleNumber(Integer maxPeopleNumber) {
        this.maxPeopleNumber = maxPeopleNumber;
    }

    public Long getDeadline() {
        return deadline;
    }

    public void setDeadline(Long deadline) {
        this.deadline = deadline;
    }

    public String getSimpleDrawingAddress() {
        return simpleDrawingAddress;
    }

    public void setSimpleDrawingAddress(String simpleDrawingAddress) {
        this.simpleDrawingAddress = simpleDrawingAddress;
    }

    public Integer getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Integer isGroup) {
        this.isGroup = isGroup;
    }
}
