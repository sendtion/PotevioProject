package com.sendtion.poteviodemo.util;


import android.app.Activity;
import android.content.Intent;

import com.sendtion.poteviodemo.ui.comm.FileSelectActivity;

public class FileSelectHelper {
    private static FileSelectHelper fileSelectHelper;
    private int maxCount = ConstantUtils.MAX_COUNT;
    private String maxTip = ConstantUtils.MAX_TIP;
    private String[] fileTypes = new String[]{
            ConstantUtils.FS_FILE_WORD,
            ConstantUtils.FS_FILE_XLS,
            ConstantUtils.FS_FILE_PPT,
            ConstantUtils.FS_FILE_PDF,
            ConstantUtils.FS_FILE_TXT,
            ConstantUtils.FS_FILE_IMAGE,
            ConstantUtils.FS_FILE_ZIP
    };

    public static FileSelectHelper getInstance(){
        if (fileSelectHelper == null){
            synchronized (FileSelectHelper.class){
                if (fileSelectHelper == null){
                    fileSelectHelper = new FileSelectHelper();
                }
            }
        }
        return fileSelectHelper;
    }

    public void start(Activity activity){
        Intent intent = new Intent();
        intent.setClass(activity, FileSelectActivity.class);
        activity.startActivityForResult(intent, ConstantUtils.FS_REQUEST_CODE);
    }

    public FileSelectHelper setMaxCount(int maxCount){
        this.maxCount = maxCount;
        this.maxTip = "最多选择"+ maxCount +"个文件";
        return this;
    }

    public FileSelectHelper setMaxTip(String maxTip){
        this.maxTip = maxTip;
        return this;
    }

    public FileSelectHelper setFileTypes(String[] fileTypes){
        this.fileTypes = fileTypes;
        return this;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public String getMaxTip() {
        return maxTip;
    }

    public String[] getFileTypes() {
        return fileTypes;
    }
}
