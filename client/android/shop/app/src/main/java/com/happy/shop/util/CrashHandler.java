package com.happy.shop.util;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler mInstance = new CrashHandler();

    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private Map<String,String> infos = new HashMap<>();


    public void init(Context context){
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置当前线程默认的UncaughtExceptionHandler
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {

    }

    private CrashHandler(){

    }

    public static CrashHandler getInstance(){
        return mInstance;
    }
}
