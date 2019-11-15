package com.sendtion.poteviodemo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.multidex.MultiDex;

import com.sendtion.poteviodemo.ui.activity.MainActivity;
import com.sendtion.poteviodemo.ui.activity.LoadMultiDexActivity;
import com.sendtion.poteviodemo.ui.activity.SplashActivity;
import com.sendtion.poteviodemo.util.SystemUtils;
import com.sendtion.poteviodemo.util.Utils;

import java.io.File;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        Log.d(TAG, "attachBaseContext-getPackageName: " + base.getPackageName());
        Log.d(TAG, "attachBaseContext-getProcessName: " + SystemUtils.getProcessName(base));

        boolean isMainProcess = SystemUtils.isMainProcess(base);
        Log.d(TAG, "attachBaseContext-isMainProcess: " + isMainProcess);

        //主进程并且vm不支持多dex的情况下才使用 MultiDex，主要针对5.0以下系统，低端手机启动会ANR问题
        if (isMainProcess && !SystemUtils.isVMMultidexCapable()){
            Log.e(TAG, "attachBaseContext-loadMultiDex");
            loadMultiDex(base);
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!SystemUtils.isMainProcess(this)){
            Log.d(TAG, "onCreate: 非主进程，return");
            return;
        }

        Utils.init(this);
        Log.d(TAG, "主进程 onCreate: 一些初始化操作");
    }

    private void loadMultiDex(Context context) {
        newTempFile(context); //创建临时文件

        //启动另一个进程去加载MultiDex
        Intent intent = new Intent(context, LoadMultiDexActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        //检查MultiDex是否安装完（安装完会删除临时文件）
        checkUntilLoadDexSuccess(context);

        //另一个进程以及加载 MultiDex，有缓存了，所以主进程再加载就很快了。
        //为什么主进程要再加载，因为每个进程都有一个ClassLoader
        long startTime = System.currentTimeMillis();
        MultiDex.install(context);
        Log.d(TAG, "第二次 MultiDex.install 结束，耗时: " + (System.currentTimeMillis() - startTime));

        preNewActivity();
    }

    private void preNewActivity() {
        long startTime = System.currentTimeMillis();
        SplashActivity splashActivity = new SplashActivity();
        MainActivity mainActivity = new MainActivity();
        Log.d(TAG, "preNewActivity 耗时: " + (System.currentTimeMillis() - startTime));
    }

    //创建一个临时文件，MultiDex install 成功后删除
    private void newTempFile(Context context) {
        try {
            File file = new File(context.getCacheDir().getAbsolutePath(), "load_dex.tmp");
            if (!file.exists()) {
                Log.d(TAG, "newTempFile: ");
                file.createNewFile();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /**
     * 检查MultiDex是否安装完,通过判断临时文件是否被删除
     */
    private void checkUntilLoadDexSuccess(Context context) {
        File file = new File(context.getCacheDir().getAbsolutePath(), "load_dex.tmp");
        int i = 0;
        int waitTime = 100; //睡眠时间
        try {
            Log.d(TAG, "checkUntilLoadDexSuccess: >>> ");
            while (file.exists()) {
                Thread.sleep(waitTime);
                Log.d(TAG, "checkUntilLoadDexSuccess: sleep count = " + ++i);
                if (i > 40) {
                    Log.d(TAG, "checkUntilLoadDexSuccess: 超时，等待时间： " + (waitTime * i));
                    break;
                }
            }

            Log.d(TAG, "checkUntilLoadDexSuccess: 轮循结束，等待时间 " +(waitTime * i));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
