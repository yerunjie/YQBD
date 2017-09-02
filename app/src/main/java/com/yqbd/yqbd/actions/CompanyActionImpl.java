package com.yqbd.yqbd.actions;

import android.app.Activity;
import com.yqbd.yqbd.utils.BaseJson;
import com.yqbd.yqbd.utils.HttpUtils;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by joy12 on 2017/7/22.
 */
public class CompanyActionImpl  extends BaseActionImpl {
    public CompanyActionImpl(Activity activity) {
        super(activity);
    }

    //这个可以连
    public BaseJson companylogin(String companyAccount, String companyPassword) throws IOException, JSONException {
        Map<String, String> map = new HashMap<>();
        map.put("companyAccount", companyAccount);
        map.put("companyPassword", companyPassword);
        String result = HttpUtils.httpConnectByPost("company/companyLogin", map);
        return new BaseJson(result);
    }



    public BaseJson getCompanyInfoByCompanyId(int companyId) throws IOException, JSONException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("companyId", String.valueOf(companyId));
        String result = HttpUtils.httpConnectByPost("company/getCompanyInfoByCompanyId", map);
        return new BaseJson(result);
    }


}
