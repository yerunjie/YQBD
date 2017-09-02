package com.yqbd.yqbd.activities.group;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.yqbd.yqbd.BaseActivity;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.actions.GroupActionImpl;
import com.yqbd.yqbd.beans.GroupInfoBean;
import com.yqbd.yqbd.utils.BaseJson;
import org.json.JSONException;

import java.io.IOException;

public class ApplyQuitActivity extends BaseActivity<Integer> implements View.OnClickListener{

    private GroupActionImpl groupAction = new GroupActionImpl(this);
    private GroupInfoBean groupInfoBean = null;
    private Integer groupId = 0;

    private TextView getGroupToQuitTitle() {
        return (TextView) findViewById(R.id.text_groupToQuit_title);
    }
    private TextView getGroupToQuitDescription() { return (TextView) findViewById(R.id.text_groupToQuit_description);
    }
    private TextView getGroupToQuitMaxNum() {
        return (TextView) findViewById(R.id.text_groupToQuit_maxPeopleNum);
    }
    private TextView getGroupToQuitCurrentNum() { return (TextView) findViewById(R.id.text_groupToQuit_currentPeopleNum);}


    @Override
    protected void onEventMainThread(BaseJson argument) {
        if (argument == null){
            Toast.makeText(this, "发现异常", Toast.LENGTH_SHORT).show();
        }else {
            if (groupInfoBean == null){
                try {
                    groupInfoBean = new GroupInfoBean(argument.getJSONObject());
                    getGroupToQuitTitle().append(groupInfoBean.getGroupTitle());
                    getGroupToQuitDescription().append(groupInfoBean.getGroupDescription());
                    getGroupToQuitMaxNum().append(groupInfoBean.getMaxPeopleNumber().toString());
                    getGroupToQuitCurrentNum().append(groupInfoBean.getCurrentPeopleNumber().toString());
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
            } else {
                baseJson = groupAction.applyQuitGroup(argument);
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
        setContentView(R.layout.activity_apply_quit);
        findViewById(R.id.btn_group_quit).setOnClickListener(this);

        groupId = getIntent().getIntExtra("groupId", 0);
        Log.d("QuitGroupActivity","groupId=" + groupId);
        netPost(groupId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_group_quit:
                netPost(groupId);
                break;
        }
    }
}
