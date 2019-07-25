package com.study;

import android.app.Application;

import com.study.util.MLog;

/**
 * 作者：Tangren on 2019-07-25
 * 包名：com.study
 * 邮箱：996489865@qq.com
 * TODO:一句话描述
 */
public class App extends Application {
    private static App instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        MLog.setDebug(true);
    }

    public static App getInstance(){
        return instance;
    }
}
