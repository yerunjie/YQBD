package com.yqbd.yqbd.activities.group;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.yqbd.yqbd.BaseActivity;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.actions.CompanyActionImpl;
import com.yqbd.yqbd.actions.GroupActionImpl;
import com.yqbd.yqbd.beans.CompanyInfoBean;
import com.yqbd.yqbd.beans.GroupInfoBean;
import com.yqbd.yqbd.utils.BaseJson;
import org.json.JSONException;

import java.io.IOException;

public class CancelJoinActivity extends BaseActivity<Integer> implements View.OnClickListener {
    private GroupActionImpl groupAction = new GroupActionImpl(this);
    private CompanyActionImpl companyAction = new CompanyActionImpl(this);
    private Integer groupId = 0;

    private GroupInfoBean groupInfoBean = null;
    private CompanyInfoBean companyInfoBean = null;

    private TextView getGroupToCancelTitle() {
        return (TextView) findViewById(R.id.text_groupToCancel_title);
    }
    private TextView getGroupToCancelDescription() { return (TextView) findViewById(R.id.text_groupToCancel_description);
    }
    private TextView getGroupToCancelCompanyName() {
        return (TextView) findViewById(R.id.text_companyToCancel_name);
    }
    private TextView getGroupToCancelMaxNum() {
        return (TextView) findViewById(R.id.text_groupToCancel_max_people_num);
    }
    private TextView getGroupToCancelCurrentNum() {
        return (TextView) findViewById(R.id.text_groupToCancel_current_people_num);
    }

    @Override
    protected void onEventMainThread(BaseJson argument) {
        if (argument == null){
            Toast.makeText(this, "发现异常", Toast.LENGTH_SHORT).show();
        }else {
            if (groupInfoBean == null){
                try {
                    groupInfoBean = new GroupInfoBean(argument.getJSONObject());
                    getGroupToCancelTitle().append(groupInfoBean.getGroupTitle());
                    getGroupToCancelDescription().append(groupInfoBean.getGroupDescription());
                    getGroupToCancelCompanyName().append(companyInfoBean.getCompanyName());
                    getGroupToCancelMaxNum().append(groupInfoBean.getMaxPeopleNumber().toString());
                    getGroupToCancelCurrentNum().append(groupInfoBean.getCurrentPeopleNumber().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, argument.getErrorMessage(), Toast.LENGTH_SHORT).show();
                //跳转到列表Activity
            }
        }
    }

    @Override
    protected BaseJson onEventAsync(Integer argument) {
        BaseJson baseJson = null;
        try {
            if (groupInfoBean == null){
                baseJson = groupAction.getGroupInfo(argument);
                Integer companyId = baseJson.getJSONObject().getInt("companyId");
                companyInfoBean = new CompanyInfoBean(companyAction.getCompanyInfoByCompanyId(companyId).getJSONObject());
            } else {
                baseJson = groupAction.cancelJoinGroup(argument);

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return baseJson;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_join);

        findViewById(R.id.btn_cancel_join).setOnClickListener(this);

        groupId = getIntent().getIntExtra("groupId", 0);
        Log.d("CancelJoinActivity","groupId=" + groupId);
        netPost(groupId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_cancel_join:
                netPost(groupId);
                break;
        }

    }
}
