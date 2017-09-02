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

//1、显示group信息
//2、提交入组申请
public class ApplyJoinActivity extends BaseActivity<Integer> implements View.OnClickListener {
    private GroupActionImpl groupAction = new GroupActionImpl(this);
    private CompanyActionImpl companyAction = new CompanyActionImpl(this);

    private Integer groupId;
    private GroupInfoBean groupInfoBean = null;
    private CompanyInfoBean companyInfoBean = null;

    private TextView getGroupTitle() {
        return (TextView) findViewById(R.id.text_group_title);
    }
    private TextView getGroupDescription() { return (TextView) findViewById(R.id.text_group_description);
    }
    private TextView getCompanyName() {
        return (TextView) findViewById(R.id.text_company_name);
    }
    private TextView getMaxNum() {
        return (TextView) findViewById(R.id.text_max_people_num);
    }
    private TextView getCurrentNum() {
        return (TextView) findViewById(R.id.text_current_people_num);
    }

    @Override
    protected void onEventMainThread(BaseJson argument) {
        if (argument == null){
            Toast.makeText(this, "发现异常", Toast.LENGTH_SHORT).show();
        }else {
            if (groupInfoBean == null){
                try {
                    groupInfoBean = new GroupInfoBean(argument.getJSONObject());
                    getCompanyName().append(companyInfoBean.getCompanyName());
                    getGroupTitle().append(groupInfoBean.getGroupTitle());
                    getGroupDescription().append(groupInfoBean.getGroupDescription());
                    getMaxNum().append(groupInfoBean.getMaxPeopleNumber().toString());
                    getCurrentNum().append(groupInfoBean.getCurrentPeopleNumber().toString());;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, argument.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected BaseJson onEventAsync(Integer argument) {
        BaseJson baseJson = null;
        Log.d("SingleGroupActivity", "小组初始化 groupId=" + groupId);
        try {
            if (groupInfoBean == null){
                baseJson = groupAction.getGroupInfo(argument);
                Integer companyId = baseJson.getJSONObject().getInt("companyId");
                companyInfoBean = new CompanyInfoBean(companyAction.getCompanyInfoByCompanyId(companyId).getJSONObject());
            } else {
                baseJson = groupAction.applyJoinGroup(argument);

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
        setContentView(R.layout.activity_apply_join);

        findViewById(R.id.btn_apply_join).setOnClickListener(this);

        groupId = getIntent().getIntExtra("groupId", 0);
        Log.d("SingleGroupActivity","groupId=" + groupId);
        netPost(groupId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_apply_join:
                if (groupInfoBean != null)
                    netPost(groupInfoBean.getGroupId());
                else
                    Toast.makeText(this,"小组加载错误",Toast.LENGTH_SHORT).show();
                break;


        }
    }
}
