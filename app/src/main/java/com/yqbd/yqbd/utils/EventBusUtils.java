package com.yqbd.yqbd.utils;

import android.util.Log;
import org.greenrobot.eventbus.EventBus;

import java.util.Map;

/**
 * Created by 11022 on 2017/7/13.
 */
public class EventBusUtils {
    private static Map<Class,EventBus> map;
    public static void register(){
        StackTraceElement[] stackTraceElements=Thread.currentThread().getStackTrace();
        Log.d("Class",stackTraceElements[3].getClassName());
    }
}
