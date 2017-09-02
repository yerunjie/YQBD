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
import com.yqbd.yqbd.actions.CompanyActionImpl;
import com.yqbd.yqbd.activities.main.MainActivity;
import com.yqbd.yqbd.utils.BaseJson;

public class CompanyLoginActivity extends BaseActivity<Integer> implements View.OnClickListener {
    private CompanyActionImpl companyAction = new CompanyActionImpl(this);
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_login);
        sharedPreferences = this.getSharedPreferences("companyInfo", Context.MODE_PRIVATE);
        findViewById(R.id.company_login).setOnClickListener(this);
    }

    private EditText getCompanyAccount() {
        return (EditText) findViewById(R.id.company_account);
    }

    private EditText getCompanyPassword() {
        return (EditText) findViewById(R.id.company_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.company_login:
                //TODO implement
                new Thread(new CompanyLoginThread()).start();
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

    public class CompanyLoginThread implements Runnable {
        @Override
        public void run() {
            String message = "登录成功";
            try {
                String accountNumberString = getCompanyAccount().getText().toString();
                String userPasswordString = getCompanyPassword().getText().toString();
                BaseJson baseJson = companyAction.companylogin(accountNumberString, userPasswordString);
                Log.d("ReturnCode", baseJson.getReturnCode());
                switch (baseJson.getReturnCode()) {
                    case "3.0":
                        SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                        Log.d("CompanyLoginActivity","getCompanyId=" + baseJson.getSingleIntegerResult());
                        editor.putInt("companyID", baseJson.getSingleIntegerResult());
                        editor.commit();
                        Intent intent = new Intent();
                        intent.setClass(CompanyLoginActivity.this, MainActivity.class);
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
//            localPost(Toast.makeText(UserLoginActivity.this, message, Toast.LENGTH_SHORT));
        }
    }

}

