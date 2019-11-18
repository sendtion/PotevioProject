package com.sendtion.poteviodemo.util;

import com.sendtion.poteviodemo.entry.FileInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstantUtils {
    public static Map<String, List<FileInfo>> checkFiles = new HashMap<>();
    public static Map<String, List<Integer>> checkItems = new HashMap<>();

    public static final String FS_SELECTED_FILE = "fs_selected_file";
    public static final int FS_REQUEST_CODE = 1199;

    public static final String FS_FILE_WORD = "WORD";
    public static final String FS_FILE_XLS = "XLS";
    public static final String FS_FILE_PPT = "PPT";
    public static final String FS_FILE_PDF = "PDF";
    public static final String FS_FILE_TXT = "TXT";
    public static final String FS_FILE_IMAGE = "IMAGE";
    public static final String FS_FILE_ZIP = "ZIP/RAR";

    public static int MAX_COUNT = 5; //最大选择数量
    public static String MAX_TIP = "最多选择5个文件"; //超过最大选择数量提示信息

}
