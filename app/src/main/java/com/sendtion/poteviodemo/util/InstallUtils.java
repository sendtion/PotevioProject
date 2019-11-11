package com.sendtion.poteviodemo.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import java.io.File;

import es.dmoral.toasty.Toasty;

public class InstallUtils {

    /**
     * 安装APK
     */
    public static void installAPK(Context context, String apkPath) {
        if (!SafetyUtils.checkFile(apkPath)) {
            return;
        }
        if (!SafetyUtils.checkPackageName(context, apkPath)) {
            Toasty.normal(Utils.getApp(), "升级包被恶意软件篡改 请重新升级下载安装").show();
            return;
        }
        switch (SafetyUtils.checkPackageSign(Utils.getApp(), apkPath)) {
            case SafetyUtils.SUCCESS:
                try {
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    Uri uri = Uri.parse("file://" + downloadPath);
//                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
//                    //在服务中开启activity必须设置flag,后面解释
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    this.startActivity(intent);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    File apkFile = new File(apkPath);
                    //兼容7.0
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", apkFile);
                        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                        //兼容8.0
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            boolean hasInstallPermission = context.getPackageManager().canRequestPackageInstalls();
                            if (!hasInstallPermission) {
                                startInstallPermissionSettingActivity(context, apkPath);
                                return;
                            }
                        }
                    } else {
                        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    }
                    if (context.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                        SPUtils.getInstance().put("newAppUrl", "");
                        context.startActivity(intent);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                break;
            case SafetyUtils.SIGNATURES_INVALIDATE:
                Toasty.normal(Utils.getApp(), "升级包安全校验失败 请重新升级").show();
                break;
            case SafetyUtils.VERIFY_SIGNATURES_FAIL:
                Toasty.normal(Utils.getApp(), "升级包为盗版应用 请重新升级").show();
                break;
            default:
                break;
        }
    }

    /**
     * 跳转到设置-允许安装未知来源-页面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void startInstallPermissionSettingActivity(Context context, String apkPath) {
        try {
            SPUtils.getInstance().put("newAppUrl", apkPath);
            //注意这个是8.0新API
            Uri selfPackageUri = Uri.parse("package:" + context.getPackageName());
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, selfPackageUri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
