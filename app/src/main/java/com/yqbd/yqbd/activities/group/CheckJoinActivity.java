package com.yqbd.yqbd.activities.group;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.yqbd.yqbd.BaseActivity;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.actions.GroupActionImpl;
import com.yqbd.yqbd.actions.UserActionImpl;
import com.yqbd.yqbd.beans.ApplicationBean;
import com.yqbd.yqbd.beans.GroupInfoBean;
import com.yqbd.yqbd.beans.UserInfoBean;
import com.yqbd.yqbd.utils.BaseJson;
import org.json.JSONException;

import java.io.IOException;


public class CheckJoinActivity extends BaseActivity<Integer> implements View.OnClickListener{

    private ApplicationBean applicationBean = null;

    private GroupActionImpl groupAction = new GroupActionImpl(this);

    @Override
    protected void onEventMainThread(BaseJson argument) {
        if (argument == null){
            Toast.makeText(this, "发现异常", Toast.LENGTH_SHORT).show();
        }else {
            if (argument.getReturnCode().equals("intialize")){
                getGroupApply().append(applicationBean.getGroupTitle());
                getCandidateNameApply().append(applicationBean.getRealName());
            } else {
                Toast.makeText(this, argument.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected BaseJson onEventAsync(Integer argument) {
        BaseJson baseJson = null;
        try{
            switch (argument){
                case 0://显示数据初始化
                    baseJson = new BaseJson();
                    baseJson.setObj(applicationBean);
                    baseJson.setReturnCode("intialize");
                    baseJson.setErrorMessage("初始化...");
                    break;
                case 1://通过
                    baseJson = groupAction.checkJoinApplication(applicationBean.getGroupId(),applicationBean.getUserId(),true);
                    break;
                case -1://拒绝
                    baseJson = groupAction.checkJoinApplication(applicationBean.getGroupId(),applicationBean.getUserId(),false);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return baseJson;
    }

    private TextView getCandidateNameApply() {
        return (TextView) findViewById(R.id.text_candidate_name_apply);
    }
    private TextView getGroupApply() {
        return (TextView) findViewById(R.id.text_group_apply);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_join);
//        groupId = getIntent().getIntExtra("groupId", 0);
//        candidateId = getIntent().getIntExtra("candidateId", 0);
        applicationBean = getIntent().getParcelableExtra("applicationBean");

        Log.d("CheckJoinActivity","groupId=" + applicationBean.getGroupId() + "  candidateId=" + applicationBean.getUserId());

        findViewById(R.id.btn_pass_join).setOnClickListener(this);
        findViewById(R.id.btn_reject_join).setOnClickListener(this);

        netPost(0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pass_join:
                netPost(1);
                break;
            case R.id.btn_reject_join:
                netPost(-1);
                break;
        }
    }
}
