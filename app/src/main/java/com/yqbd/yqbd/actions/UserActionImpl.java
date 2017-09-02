package com.yqbd.yqbd.actions;

import android.app.Activity;
import com.yqbd.yqbd.utils.BaseJson;
import com.yqbd.yqbd.utils.HttpUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 11022 on 2017/7/9.
 */
public class UserActionImpl extends BaseActionImpl {
    public UserActionImpl(Activity activity) {
        super(activity);
    }

    public BaseJson login(String accountNumber, String userPassword) throws IOException, JSONException {
        Map<String, String> map = new HashMap<>();
        map.put("accountNumber", accountNumber);
        map.put("userPassword", userPassword);
        String result = HttpUtils.httpConnectByPost("user/login", map);
        return new BaseJson(result);
    }


    public BaseJson register(String accountNumber, String userPassword, String realName, String telephone) throws IOException, JSONException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("accountNumber", accountNumber);
        map.put("userPassword", userPassword);
        map.put("telephone", telephone);
        map.put("realName",realName);
        String result = HttpUtils.httpConnectByPost("user/register", map);
        return new BaseJson(result);
    }


    public BaseJson getUserInfoByUserID(int userID) throws IOException, JSONException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userID", String.valueOf(userID));
        String result = HttpUtils.httpConnectByPost("user/getUserInfoByUserID", map);
        return new BaseJson(result);
    }


    public int validateRealName(String accountNumber, String realName) throws IOException {
        return 0;
    }


    public int validateTeleNum(String teleNum, String validateNumber) throws IOException {
        return 0;
    }

}
