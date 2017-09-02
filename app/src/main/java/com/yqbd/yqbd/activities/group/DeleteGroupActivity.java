package com.yqbd.yqbd.activities.group;

import android.support.v7.app.AppCompatActivity;
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

public class DeleteGroupActivity extends BaseActivity<Integer> implements View.OnClickListener {

    private GroupActionImpl groupAction = new GroupActionImpl(this);
    private GroupInfoBean groupInfoBean = null;
    private Integer groupId = 0;

    private TextView getGroupToDelTitle() {
        return (TextView) findViewById(R.id.text_groupToDel_title);
    }
    private TextView getGroupToDelDescription() { return (TextView) findViewById(R.id.text_groupToDel_description);
    }
    private TextView getGroupToDelMaxNum() {
        return (TextView) findViewById(R.id.text_groupToDel_maxPeopleNum);
    }
    private TextView getGroupToDelCurrentNum() { return (TextView) findViewById(R.id.text_groupToDel_currentPeopleNum);}

    @Override
    protected void onEventMainThread(BaseJson argument) {
        if (argument == null){
            Toast.makeText(this, "发现异常", Toast.LENGTH_SHORT).show();
        }else {
            if (groupInfoBean == null){
                try {
                    groupInfoBean = new GroupInfoBean(argument.getJSONObject());
                    getGroupToDelTitle().append(groupInfoBean.getGroupTitle());
                    getGroupToDelDescription().append(groupInfoBean.getGroupDescription());
                    getGroupToDelMaxNum().append(groupInfoBean.getMaxPeopleNumber().toString());
                    getGroupToDelCurrentNum().append(groupInfoBean.getCurrentPeopleNumber().toString());
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
                baseJson = groupAction.deleteGroup(argument);
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
        setContentView(R.layout.activity_delete_group);
        findViewById(R.id.btn_group_delete).setOnClickListener(this);

        groupId = getIntent().getIntExtra("groupId", 0);
        Log.d("DeleteGroupActivity","groupId=" + groupId);
        netPost(groupId);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_group_delete:
                netPost(groupId);
                break;
        }
    }
}
