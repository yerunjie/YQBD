package com.yqbd.yqbd.actions;

import android.app.Activity;
import com.yqbd.yqbd.beans.TaskBean;
import com.yqbd.yqbd.beans.TypeBean;
import com.yqbd.yqbd.utils.BaseJson;
import com.yqbd.yqbd.utils.HttpUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 11022 on 2017/7/17.
 */
public class TaskActionImpl extends BaseActionImpl {
    public TaskActionImpl(Activity activity) {
        super(activity);
    }

    public BaseJson getAllTypes() throws IOException, JSONException {
        Map<String, String> map = new HashMap<>();
        String result = HttpUtils.httpConnectByPost("task/getAllTypes", map);
        return new BaseJson(result);
    }

    public BaseJson publishTaskByUser(TaskBean object) throws IOException, JSONException {
        Map<String, String> map = new HashMap<>();
        map.put("userId", getCurrentUserID().toString());
        map.put("companyId", getCurrentCompanyID().toString());
        map.put("taskTitle", object.getTaskTitle());
        map.put("taskDescription", object.getTaskDescription());
        map.put("taskAddress", object.getTaskAddress());
        map.put("maxPeopleNumber",object.getMaxPeopleNumber().toString());
        map.put("deadline",object.getDeadline().toString());
        map.put("pay",object.getPay().toString());
        map.put("simpleDrawingAddress",object.getSimpleDrawingAddress());
        map.put("groupId","0");
        StringBuilder stringBuilder = new StringBuilder( );
        for (TypeBean typeBean : object.getTypeBeans()) {
            stringBuilder.append(typeBean.getTypeId()).append(",");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        map.put("typeBeans", stringBuilder.toString());
        String result = HttpUtils.httpConnectByPost("task/publishTask", map);
        return new BaseJson(result);
    }

    public BaseJson publishLongTermTask(TaskBean object) throws IOException, JSONException {
        Map<String, String> map = new HashMap<>();
        map.put("userId", getCurrentUserID().toString());
        map.put("companyId", getCurrentCompanyID().toString());
        map.put("taskTitle", object.getTaskTitle());
        map.put("taskDescription", object.getTaskDescription());
        map.put("taskAddress", object.getTaskAddress());
        map.put("maxPeopleNumber",object.getMaxPeopleNumber().toString());
        map.put("deadline",object.getDeadline().toString());
        map.put("pay",object.getPay().toString());
        map.put("simpleDrawingAddress",object.getSimpleDrawingAddress());
        map.put("groupId",object.getIsGroup().toString());
        StringBuilder stringBuilder = new StringBuilder();
        for (TypeBean typeBean : object.getTypeBeans()) {
            stringBuilder.append(typeBean.getTypeId()).append(",");
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        map.put("typeBeans", stringBuilder.toString());
        String result = HttpUtils.httpConnectByPost("task/publishTask", map);
        return new BaseJson(result);
    }
}
