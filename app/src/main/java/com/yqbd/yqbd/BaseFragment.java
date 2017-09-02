package com.yqbd.yqbd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;
import com.yqbd.yqbd.utils.BaseJson;
import com.yqbd.yqbd.utils.EventBusUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by 11022 on 2017/7/13.
 */
public abstract class BaseFragment<T1> extends Fragment {
    private EventBus eventBus;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        eventBus = new EventBus();
        eventBus.register(this);
        EventBusUtils.register();
        //new Thread(new PersonalThread()).start();
    }

    protected void localPost(Object o){
        eventBus.post(o);
    }

    protected void netPost(T1 o){
        eventBus.post(new NetRequestArgument(o));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(UIArgument argument){
        onEventMainThread(argument.object);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Toast toast){
        toast.show();
    }

    protected abstract void onEventMainThread(BaseJson argument);

    protected abstract BaseJson onEventAsync(T1 argument);

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventAsync(NetRequestArgument argument){
        BaseJson baseJson=onEventAsync(argument.object);
        if(baseJson==null){
            localPost(Toast.makeText(getContext(),"连接错误",Toast.LENGTH_SHORT));
        }else {
            localPost(new UIArgument(baseJson));
        }
    }

    public class NetRequestArgument{
        T1 object;

        public NetRequestArgument(T1 object) {
            this.object = object;
        }
    }

    public class UIArgument{
        BaseJson object;

        public UIArgument(BaseJson object) {
            this.object = object;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }
}
