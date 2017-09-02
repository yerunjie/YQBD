package com.yqbd.yqbd.actions;

import android.app.Activity;
import android.util.Log;
import com.yqbd.yqbd.beans.GroupInfoBean;
import com.yqbd.yqbd.beans.GroupMemberBean;
import com.yqbd.yqbd.utils.BaseJson;
import com.yqbd.yqbd.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by joy12 on 2017/7/22.
 */
public class GroupActionImpl extends BaseActionImpl  {
    public GroupActionImpl(Activity activity) {super(activity);}

    public BaseJson createGroup(GroupInfoBean object) throws IOException, JSONException {
        BaseJson baseJson;
        Map<String, String> map = new HashMap<>();
        Integer companyId = getCurrentCompanyID();
        Log.d("GroupActionImpl","companyId = " + companyId);
        if (companyId != null){
            //if (companyId == 0) companyId = 1;
            map.put("companyId", companyId.toString());
            map.put("groupTitle", object.getGroupTitle());
            map.put("groupDescription", object.getGroupDescription());
            map.put("maxPeopleNumber",object.getMaxPeopleNumber().toString());
            String result = HttpUtils.httpConnectByPost("group/createGroup", map);
            baseJson = new BaseJson(result);
        } else {
            baseJson = new BaseJson();
            baseJson.setObj(null);
            baseJson.setReturnCode("companyId not found");
            baseJson.setErrorMessage("非企业账号，无法进行组员招募");
        }
        return baseJson;
    }

    public BaseJson deleteGroup(Integer groupId) throws IOException, JSONException {
        BaseJson baseJson;
        Map<String, String> map = new HashMap<>();
        Integer companyId = getCurrentCompanyID();
        Log.d("GroupActionImpl","companyId = " + companyId);
        if (companyId != null){
            //if (companyId == 0) companyId = 1;
            map.put("companyId", companyId.toString());
            map.put("groupId", groupId.toString());
            String result = HttpUtils.httpConnectByPost("group/deleteGroup", map);
            baseJson = new BaseJson(result);
        } else {
            baseJson = new BaseJson();
            baseJson.setObj(null);
            baseJson.setReturnCode("companyId not found");
            baseJson.setErrorMessage("非企业账号，无法删除小组");
        }
        return baseJson;
    }

    public BaseJson getGroupInfo(Integer groupId) throws IOException, JSONException {
        BaseJson baseJson = null;
        Map<String, String> map = new HashMap<>();
        map.put("groupId",groupId.toString());
        if (groupId > 0){
            String result = HttpUtils.httpConnectByPost("group/getGroupByGroupId", map);
            baseJson = new BaseJson(result);
        }
        return baseJson;
    }

    public BaseJson getCompanyGroups() throws IOException, JSONException {
        Map<String, String> map = new HashMap<>();
        Integer companyId = getCurrentCompanyID();
        map.put("companyId",companyId.toString());
        String result = HttpUtils.httpConnectByPost("group/getCompanyGroups", map);
        return new BaseJson(result);
    }

    public BaseJson getJoinedGroups() throws IOException, JSONException {
        BaseJson baseJson;
        Integer userId = getCurrentUserID();
        if (userId > 0){
            Map<String, String> map = new HashMap<>();
            map.put("userId",userId.toString());
            map.put("status","0");
            String result = HttpUtils.httpConnectByPost("group/getGroupsByUserIdAndStatus", map);
            baseJson = new BaseJson(result);
        }else {
            baseJson = new BaseJson();
            baseJson.setReturnCode("userId not found");
            baseJson.setErrorMessage("请先登录个人账号");
        }
        return baseJson;
    }

    public BaseJson getAppliedGroups() throws IOException, JSONException {
        BaseJson baseJson;
        Integer userId = getCurrentUserID();
        if (userId > 0){
            Map<String, String> map = new HashMap<>();
            map.put("userId",userId.toString());
            map.put("status","1");
            String result = HttpUtils.httpConnectByPost("group/getGroupsByUserIdAndStatus", map);
            baseJson = new BaseJson(result);
        }else {
            baseJson = new BaseJson();
            baseJson.setReturnCode("userId not found");
            baseJson.setErrorMessage("请先登录个人账号");
        }
        return baseJson;
    }

    public BaseJson applyJoinGroup(Integer groupId) throws IOException, JSONException {
        BaseJson baseJson;
        Integer userId = getCurrentUserID();
        if (userId > 0){
            Map<String, String> map = new HashMap<>();
            map.put("groupId",groupId.toString());
            map.put("userId",userId.toString());
            String result = HttpUtils.httpConnectByPost("group/joinApply", map);
            baseJson = new BaseJson(result);
        }else {
            baseJson = new BaseJson();
            baseJson.setReturnCode("userId not found");
            baseJson.setErrorMessage("请先登录个人账号");
        }
        return baseJson;
    }

    public BaseJson cancelJoinGroup(Integer groupId) throws IOException, JSONException {
        BaseJson baseJson;
        Integer userId = getCurrentUserID();
        if (userId > 0){
            Map<String, String> map = new HashMap<>();
            map.put("groupId",groupId.toString());
            map.put("userId",userId.toString());
            String result = HttpUtils.httpConnectByPost("group/joinCancel", map);
            baseJson = new BaseJson(result);
        }else {
            baseJson = new BaseJson();
            baseJson.setReturnCode("userId not found");
            baseJson.setErrorMessage("请先登录个人账号");
        }
        return baseJson;
    }

    public BaseJson applyQuitGroup(Integer groupId) throws IOException, JSONException {
        BaseJson baseJson;
        Integer userId = getCurrentUserID();
        if (userId > 0){
            Map<String, String> map = new HashMap<>();
            map.put("groupId",groupId.toString());
            map.put("userId",userId.toString());
            String result = HttpUtils.httpConnectByPost("group/quitApply", map);
            baseJson = new BaseJson(result);
        }else {
            baseJson = new BaseJson();
            baseJson.setReturnCode("userId not found");
            baseJson.setErrorMessage("请先登录个人账号");
        }
        return baseJson;
    }

    public BaseJson getUncheckedApplicationsByStatus(Integer status) throws IOException, JSONException {
        BaseJson baseJson;
        Integer companyId = getCurrentCompanyID();
        if (companyId > 0){
            Map<String, String> map = new HashMap<>();
            map.put("companyId",companyId.toString());
            map.put("status",status.toString());
            String result = HttpUtils.httpConnectByPost("group/getUncheckedApplications", map);
            baseJson = new BaseJson(result);
        } else {
            baseJson = new BaseJson();
            baseJson.setObj(null);
            baseJson.setReturnCode("companyId not found");
            baseJson.setErrorMessage("非企业账号，无法进行申请审核");
        }

        return baseJson;
    }

    public BaseJson checkJoinApplication(Integer groupId, Integer candidateId, Boolean isPass) throws IOException, JSONException {
        BaseJson baseJson;
        Integer companyId = getCurrentCompanyID();
        if (companyId > 0){
            Map<String, String> map = new HashMap<>();
            map.put("groupId",groupId.toString());
            map.put("userId",candidateId.toString());
            map.put("isPass",isPass.toString());
            String result = HttpUtils.httpConnectByPost("group/joinCheck", map);
            baseJson = new BaseJson(result);
        } else {
            baseJson = new BaseJson();
            baseJson.setObj(null);
            baseJson.setReturnCode("companyId not found");
            baseJson.setErrorMessage("非企业账号，无法进行申请审核");
        }

        return baseJson;
    }

    public BaseJson checkQuitApplication(Integer groupId, Integer candidateId, Boolean isPass) throws IOException, JSONException {
        BaseJson baseJson;
        Integer companyId = getCurrentCompanyID();
        if (companyId > 0){
            Map<String, String> map = new HashMap<>();
            map.put("groupId",groupId.toString());
            map.put("userId",candidateId.toString());
            map.put("isPass",isPass.toString());
            String result = HttpUtils.httpConnectByPost("group/quitCheck", map);
            baseJson = new BaseJson(result);
        } else {
            baseJson = new BaseJson();
            baseJson.setObj(null);
            baseJson.setReturnCode("companyId not found");
            baseJson.setErrorMessage("非企业账号，无法进行申请审核");
        }

        return baseJson;
    }


}
