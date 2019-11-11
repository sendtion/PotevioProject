package com.sendtion.poteviodemo;

import android.app.Application;

import com.sendtion.poteviodemo.util.Utils;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
