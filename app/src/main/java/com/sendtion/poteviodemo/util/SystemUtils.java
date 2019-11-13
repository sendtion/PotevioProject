package com.sendtion.poteviodemo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemUtils {

    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(SystemUtils.getProcessName(context));
    }

    public static String getProcessName(Context context){
        try {
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                    .getRunningAppProcesses();

            int myPid = Process.myPid();

            if(appProcesses == null || appProcesses.size() == 0){
                return null;
            }

            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(context.getPackageName())) {
                    if (appProcess.pid == myPid){
                        return appProcess.processName;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isVMMultidexCapable(){
        return isVMMultidexCapable(System.getProperty("java.vm.version"));
    }

    //MultiDex 拷出来的的方法，判断VM是否支持多dex
    private static boolean isVMMultidexCapable(String versionString) {
        boolean isMultidexCapable = false;
        try {
            if (versionString != null) {
                Matcher matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(versionString);
                if (matcher.matches()) {
                    try {
                        int major = Integer.parseInt(matcher.group(1));
                        int minor = Integer.parseInt(matcher.group(2));
                        isMultidexCapable = major > 2 || major == 2 && minor >= 1;
                    } catch (NumberFormatException var5) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("MultiDex", "VM with version " + versionString + (isMultidexCapable ? " has multidex support" : " does not have multidex support"));
        return isMultidexCapable;
    }
}
