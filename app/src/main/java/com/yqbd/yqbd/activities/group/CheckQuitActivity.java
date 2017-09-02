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


public class CheckQuitActivity extends BaseActivity<Integer> implements View.OnClickListener{
//    private Integer groupId;
//    private Integer candidateId;
//
//    private GroupInfoBean groupInfoBean;
//    private UserInfoBean userInfoBean;

    private ApplicationBean applicationBean = null;

//    private UserActionImpl userAction = new UserActionImpl(this);
    private GroupActionImpl groupAction = new GroupActionImpl(this);

    @Override
    protected void onEventMainThread(BaseJson argument) {
        if (argument == null){
            Toast.makeText(this, "发现异常", Toast.LENGTH_SHORT).show();
        }else {
            if (argument.getReturnCode().equals("intialize")){
                getGroupQuit().append(applicationBean.getGroupTitle());
                getCandidateNameQuit().append(applicationBean.getRealName());
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
                case 0:
                    baseJson = new BaseJson();
                    baseJson.setObj(applicationBean);
                    baseJson.setReturnCode("intialize");
                    baseJson.setErrorMessage("初始化...");
                    break;
                case 1:
                    baseJson = groupAction.checkQuitApplication(applicationBean.getGroupId(),applicationBean.getUserId(),true);
                    break;
                case -1:
                    baseJson = groupAction.checkQuitApplication(applicationBean.getGroupId(),applicationBean.getUserId(),false);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return baseJson;
    }

    private TextView getCandidateNameQuit() {
        return (TextView) findViewById(R.id.text_candidate_name_quit);
    }
    private TextView getGroupQuit() {
        return (TextView) findViewById(R.id.text_group_quit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_quit);
//        groupId = getIntent().getIntExtra("groupId", 0);
//        candidateId = getIntent().getIntExtra("candidateId", 0);
        applicationBean = getIntent().getParcelableExtra("applicationBean");

        Log.d("CheckQuitActivity","groupId=" + applicationBean.getGroupId() + "  candidateId=" + applicationBean.getUserId());


        findViewById(R.id.btn_pass_quit).setOnClickListener(this);
        findViewById(R.id.btn_reject_quit).setOnClickListener(this);

        netPost(0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pass_quit:
                netPost(1);
                break;
            case R.id.btn_reject_quit:
                netPost(-1);
                break;
        }
    }
}
