package com.yqbd.yqbd.actions;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.yqbd.yqbd.utils.BaseJson;


/**
 * Created by 11022 on 2017/5/14.
 */
public class BaseActionImpl {
    protected static final String path = "http://10.0.3.2:8080/";
    protected Activity activity;
    protected BaseJson baseJson;

    public BaseActionImpl(Activity activity) {
        this.activity = activity;
    }

    public Integer getCurrentUserID() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("userID", 0);
    }

    public Integer getCurrentCompanyID() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("companyInfo", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("companyID", 0);
    }

}
