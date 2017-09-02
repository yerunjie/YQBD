package com.yqbd.yqbd.activities.group;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.yqbd.yqbd.BaseActivity;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.actions.GroupActionImpl;
import com.yqbd.yqbd.beans.GroupInfoBean;
import com.yqbd.yqbd.utils.BaseJson;
import org.json.JSONException;

import java.io.IOException;

public class CreateGroupActivity extends BaseActivity<GroupInfoBean> implements View.OnClickListener{

    private GroupActionImpl groupAction = new GroupActionImpl(this);

    @Override
    protected void onEventMainThread(BaseJson argument) {
        if (argument != null){
            Toast.makeText(this,argument.getErrorMessage(),Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"发现异常",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected BaseJson onEventAsync(GroupInfoBean argument) {
        BaseJson baseJson = null;
        try {
            baseJson = groupAction.createGroup(argument);
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
        setContentView(R.layout.activity_create_group);
        initializeTop(true,"创建小组");
        findViewById(R.id.btn_group_create).setOnClickListener(this);

    }

    private EditText getGroupTitle() {
        return (EditText) findViewById(R.id.group_title);
    }

    private EditText getGroupDescription() {
        return (EditText) findViewById(R.id.group_description);
    }

    private EditText getMaxPeopleNumber() { return (EditText) findViewById(R.id.max_people_number); }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_group_create:
                String maxnumStr = getMaxPeopleNumber().getText().toString();
                if (maxnumStr != null){
                    GroupInfoBean groupData = new GroupInfoBean(getGroupTitle().getText().toString(),
                            getGroupDescription().getText().toString(),
                            Integer.parseInt(maxnumStr));
                    netPost(groupData);
                } else {
                    Toast.makeText(this, "请填写最大人数", Toast.LENGTH_SHORT);
                }
                break;
        }
    }
}
