package com.yqbd.yqbd.activities.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.yqbd.yqbd.BaseActivity;
import com.yqbd.yqbd.R;
import com.yqbd.yqbd.actions.UserActionImpl;
import com.yqbd.yqbd.activities.main.MainActivity;
import com.yqbd.yqbd.utils.BaseJson;

public class UserLoginActivity extends BaseActivity<Integer> implements View.OnClickListener {
    private UserActionImpl userAction = new UserActionImpl(this);
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        findViewById(R.id.login).setOnClickListener(this);
    }

    private EditText getUserAccount() {
        return (EditText) findViewById(R.id.user_account);
    }

    private EditText getUserPassword() {
        return (EditText) findViewById(R.id.user_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                //TODO implement
                new Thread(new LoginThread()).start();
                break;
        }
    }

    @Override
    protected void onEventMainThread(BaseJson argument) {
        
    }

    @Override
    protected BaseJson onEventAsync(Integer argument) {
        return null;
    }

    public class LoginThread implements Runnable {
        @Override
        public void run() {
             String message = "登录成功";
            try {
                String accountNumberString = getUserAccount().getText().toString();
                String userPasswordString = getUserPassword().getText().toString();
                BaseJson baseJson = userAction.login(accountNumberString, userPasswordString);
                Log.d("ReturnCode", baseJson.getReturnCode());
                switch (baseJson.getReturnCode()) {
                    case "2.0":
                        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                        editor.putInt("userID", baseJson.getSingleIntegerResult());
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(UserLoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        message = baseJson.getErrorMessage();
                        break;
                }
            } catch (Exception e) {
                message = "连接错误";

            }

            //localPost(Toast.makeText(UserLoginActivity.this, message, Toast.LENGTH_SHORT));

        }
    }

}
