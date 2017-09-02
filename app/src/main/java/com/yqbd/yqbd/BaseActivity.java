package com.yqbd.yqbd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.yqbd.yqbd.utils.BaseJson;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by 11022 on 2017/5/13.
 */
public abstract class BaseActivity<T1> extends AppCompatActivity {
    protected TextView toolBarTitle;
    private EventBus eventBus;


    protected void localPost(Object o) {
        eventBus.post(o);
    }

    protected void netPost(T1 o) {
        eventBus.post(new NetRequestArgument(o));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIArgument argument) {
        if(!argument.cls.equals(BaseActivity.this.getClass())){
            return;
        }
        onEventMainThread(argument.object);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String toast) {
        Toast.makeText(this,toast,Toast.LENGTH_SHORT).show();
    }

    protected abstract void onEventMainThread(BaseJson argument);

    protected abstract BaseJson onEventAsync(T1 argument);

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(NetRequestArgument argument) {
        if(!argument.cls.equals(BaseActivity.this.getClass())){
            return;
        }
        BaseJson baseJson = onEventAsync(argument.object);
        if (baseJson == null) {
            localPost("连接错误");
        } else {
            localPost(new UIArgument(baseJson));
        }
    }

    public class NetRequestArgument {
        Class cls;
        T1 object;

        public NetRequestArgument(T1 object) {
            this.cls = BaseActivity.this.getClass();
            this.object = object;
        }
    }

    public class UIArgument {
        Class cls;
        BaseJson object;

        public UIArgument(BaseJson object) {
            this.cls = BaseActivity.this.getClass();
            this.object = object;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventBus = new EventBus();
        eventBus.register(this);
        //EventBusUtils.register();
        //toolBarTitle = (TextView) findViewById(R.id.toolBar).findViewById(R.id.topToolBarTitle);
    }

    protected void initView() {
        toolBarTitle = (TextView) findViewById(R.id.toolBar).findViewById(R.id.topToolBarTitle);
    }

    protected void setToolBarTitleText(String titleText) {
        toolBarTitle.setText(titleText);
    }

    public void initializeTop(boolean isShownReturnButton, String title) {
        android.support.v7.widget.Toolbar toolBar = (android.support.v7.widget.Toolbar) (findViewById(R.id.toolBar));
        TextView topToolBarTitle = (TextView) findViewById(R.id.toolBar).findViewById(R.id.topToolBarTitle);
        if (isShownReturnButton) {
            toolBar.setTitle(title);
            topToolBarTitle.setText("");
            setSupportActionBar(toolBar);
            //activity.getSupportActionBar().setTitle(title);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //toolBar.setNavigationIcon(R.drawable.return_img);
            toolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            toolBar.setTitle("");
            topToolBarTitle.setText(title);
            setSupportActionBar(toolBar);
        }
    }

}
