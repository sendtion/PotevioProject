package com.sendtion.poteviodemo.util;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;

import com.sendtion.poteviodemo.entry.FileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DocumentUtils {

    /**
     * 读取数据库
     */
    public static ArrayList<FileInfo> getDocumentData(Context context, String selectType) {
        ArrayList<FileInfo> fileData = new ArrayList<>();

//        String[] columns = new String[]{MediaStore.Files.FileColumns._ID,
//                MediaStore.Files.FileColumns.MIME_TYPE, MediaStore.Files.FileColumns.SIZE,
//                MediaStore.Files.FileColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.DATA};
        String[] columns = new String[]{MediaStore.Files.FileColumns.DATA};
        String select = "";
        switch (selectType) {
            case ConstantUtils.FS_FILE_IMAGE: //image
                select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.png'" + " or " +
                        MediaStore.Files.FileColumns.DATA + " LIKE '%.jpg'" + " or " +
                        MediaStore.Files.FileColumns.DATA + " LIKE '%.gif'" + ")";
                break;
            case ConstantUtils.FS_FILE_WORD: //word
                select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.doc'" + " or " +
                        MediaStore.Files.FileColumns.DATA + " LIKE '%.docx'" + ")";
                break;
            case ConstantUtils.FS_FILE_XLS: //xls
                select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.xls'" + " or " +
                        MediaStore.Files.FileColumns.DATA + " LIKE '%.xlsx'" + ")";
                break;
            case ConstantUtils.FS_FILE_PPT: //ppt
                select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.ppt'" + " or " +
                        MediaStore.Files.FileColumns.DATA + " LIKE '%.pptx'" + ")";
                break;
            case ConstantUtils.FS_FILE_PDF: //pdf
                select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.pdf'" + ")";
                break;
            case ConstantUtils.FS_FILE_TXT: //txt
                select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.txt'" + ")";
                break;
            case ConstantUtils.FS_FILE_ZIP: //zip
                select = "(" + MediaStore.Files.FileColumns.DATA + " LIKE '%.zip'" + " or " +
                        MediaStore.Files.FileColumns.DATA + " LIKE '%.rar'" + ")";
                break;
        }
        ContentResolver contentResolver = context.getContentResolver();
        //Uri.parse(Environment.getExternalStorageDirectory() + "/tencent/QQfile_recv/")
        Cursor cursor = contentResolver.query(MediaStore.Files.getContentUri("external"), columns, select, null, null);

        if (cursor != null) {
            int columnIndexOrThrow_DATA = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA);
            while (cursor.moveToNext()) {
                String path = cursor.getString(columnIndexOrThrow_DATA);
                FileInfo document = FileUtils.getFileInfoFromFile(new File(path));
                fileData.add(document);
            }
            cursor.close();
        }

        return fileData;
    }

    /**
     * 加载图片，这种方式加载图片更快
     */
    public static ArrayList<FileInfo> getImages(Context context) {
        ArrayList<FileInfo> fileData = new ArrayList<>();

//        String[] projection = new String[]{MediaStore.Images.ImageColumns._ID,
//                MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns.DISPLAY_NAME};
        String[] projection = new String[]{MediaStore.Images.ImageColumns.DATA};
        // asc 按升序排列
        // desc 按降序排列
        // projection 是定义返回的数据，selection 通常的sql 语句，例如
        // selection=MediaStore.Images.ImageColumns.MIME_TYPE+"=? " 那么 selectionArgs=new String[]{"jpg"};
        ContentResolver mContentResolver = context.getContentResolver();
        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                null, null, MediaStore.Images.ImageColumns.DATE_MODIFIED + "  desc");

        if (cursor != null) {
            //String imageId;
            //String fileName;
            String filePath;
            while (cursor.moveToNext()) {
                //imageId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID));
                //fileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME));
                filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));

                //Log.e("photo", imageId + " -- " + fileName + " -- " + filePath);
                FileInfo fileInfo = FileUtils.getFileInfoFromFile(new File(filePath));
                fileData.add(fileInfo);
            }
            cursor.close();
        }

        return fileData;
    }

    /**
     * 扫描文件夹
     */
    public static ArrayList<FileInfo> scanFolder(String selectType) {
        ArrayList<FileInfo> fileData = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory().toString();

        LinkedList<File> list = new LinkedList<>();
        File dir = new File(path);
        File file[] = dir.listFiles();
        if (file == null) return null;
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory())
                list.add(file[i]);
            else {
                System.out.println(file[i].getAbsolutePath());
            }
        }
        File tmp;
        while (!list.isEmpty()) {
            tmp = list.removeFirst();//首个目录
            if (tmp.isDirectory()) {
                file = tmp.listFiles();
                if (file == null)
                    continue;
                for (int i = 0; i < file.length; i++) {
                    if (file[i].isDirectory())
                        list.add(file[i]);//目录则加入目录列表，关键
                    else {
                        FileInfo document = FileUtils.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                        switch (selectType) {
                            case ConstantUtils.FS_FILE_IMAGE: //image
                                if (file[i].getName().endsWith(".png") || file[i].getName().endsWith(".jpg") || file[i].getName().endsWith(".gif")) {
                                    fileData.add(document);
                                }
                                break;
                            case ConstantUtils.FS_FILE_WORD: //word
                                if (file[i].getName().endsWith(".doc") || file[i].getName().endsWith(".docx")) {
                                    fileData.add(document);
                                }
                                break;
                            case ConstantUtils.FS_FILE_XLS: //xls
                                if (file[i].getName().endsWith(".xls") || file[i].getName().endsWith(".xlsx")) {
                                    fileData.add(document);
                                }
                                break;
                            case ConstantUtils.FS_FILE_PPT: //ppt
                                if (file[i].getName().endsWith(".ppt") || file[i].getName().endsWith(".pptx")) {
                                    fileData.add(document);
                                }
                                break;
                            case ConstantUtils.FS_FILE_PDF: //pdf
                                if (file[i].getName().endsWith(".pdf")) {
                                    fileData.add(document);
                                }
                                break;
                            case ConstantUtils.FS_FILE_TXT: //txt
                                if (file[i].getName().endsWith(".txt")) {
                                    fileData.add(document);
                                }
                                break;
                        }
                    }
                }
            } else {
                System.out.println(tmp);
            }
        }

        return fileData;
    }

    /**
     * 扫描文件夹
     */
    public static Map<String, ArrayList<FileInfo>> scanFolderMore(String[] selectTypes) {
        Map<String, ArrayList<FileInfo>> fileMap = new HashMap<>();
        ArrayList<FileInfo> imageData = new ArrayList<>();
        ArrayList<FileInfo> wordData = new ArrayList<>();
        ArrayList<FileInfo> xlsData = new ArrayList<>();
        ArrayList<FileInfo> pptData = new ArrayList<>();
        ArrayList<FileInfo> pdfData = new ArrayList<>();
        ArrayList<FileInfo> txtData = new ArrayList<>();

        String path = Environment.getExternalStorageDirectory().toString();

        LinkedList<File> list = new LinkedList<>();
        File dir = new File(path);
        File file[] = dir.listFiles();
        if (file == null) return null;
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory())
                list.add(file[i]);
            else {
                System.out.println(file[i].getAbsolutePath());
            }
        }
        File tmp;
        while (!list.isEmpty()) {
            tmp = list.removeFirst();//首个目录
            if (tmp.isDirectory()) {
                file = tmp.listFiles();
                if (file == null)
                    continue;
                for (int i = 0; i < file.length; i++) {
                    if (file[i].isDirectory())
                        list.add(file[i]);//目录则加入目录列表，关键
                    else {
                        FileInfo document = FileUtils.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
                        if (file[i].getName().endsWith(".png") || file[i].getName().endsWith(".jpg") || file[i].getName().endsWith(".gif")) {
                            imageData.add(document);
                        } else if (file[i].getName().endsWith(".doc") || file[i].getName().endsWith(".docx")) {
                            wordData.add(document);
                        } else if (file[i].getName().endsWith(".xls") || file[i].getName().endsWith(".xlsx")) {
                            xlsData.add(document);
                        } else if (file[i].getName().endsWith(".ppt") || file[i].getName().endsWith(".pptx")) {
                            pptData.add(document);
                        } else if (file[i].getName().endsWith(".pdf")) {
                            pdfData.add(document);
                        } else if (file[i].getName().endsWith(".txt")) {
                            txtData.add(document);
                        }
//                        if (file[i].getName().endsWith(".png") || file[i].getName().endsWith(".jpg") || file[i].getName().endsWith(".gif")) {
//                            //往图片集合中 添加图片的路径
//                            FileInfo document = FileUtils.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
//                            imageData.add(document);
//                        } else if (file[i].getName().endsWith(".doc") || file[i].getName().endsWith(".docx")) {
//                            FileInfo document = FileUtils.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
//                            wordData.add(document);
//                        } else if (file[i].getName().endsWith(".xls") || file[i].getName().endsWith(".xlsx")) {
//                            //往图片集合中 添加图片的路径
//                            FileInfo document = FileUtils.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
//                            xlsData.add(document);
//                        } else if (file[i].getName().endsWith(".ppt") || file[i].getName().endsWith(".pptx")) {
//                            //往图片集合中 添加图片的路径
//                            FileInfo document = FileUtils.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
//                            pptData.add(document);
//                        } else if (file[i].getName().endsWith(".pdf")) {
//                            //往图片集合中 添加图片的路径
//                            FileInfo document = FileUtils.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
//                            pdfData.add(document);
//                        } else if (file[i].getName().endsWith(".txt")) {
//                            //往图片集合中 添加图片的路径
//                            FileInfo document = FileUtils.getFileInfoFromFile(new File(file[i].getAbsolutePath()));
//                            txtData.add(document);
//                        }
                    }
                }
            } else {
                System.out.println(tmp);
            }
        }

        if (Arrays.asList(selectTypes).contains(ConstantUtils.FS_FILE_IMAGE)){
            fileMap.put(ConstantUtils.FS_FILE_IMAGE, imageData);
        }
        if (Arrays.asList(selectTypes).contains(ConstantUtils.FS_FILE_WORD)){
            fileMap.put(ConstantUtils.FS_FILE_WORD, wordData);
        }
        if (Arrays.asList(selectTypes).contains(ConstantUtils.FS_FILE_XLS)){
            fileMap.put(ConstantUtils.FS_FILE_XLS, xlsData);
        }
        if (Arrays.asList(selectTypes).contains(ConstantUtils.FS_FILE_PPT)){
            fileMap.put(ConstantUtils.FS_FILE_PPT, pptData);
        }
        if (Arrays.asList(selectTypes).contains(ConstantUtils.FS_FILE_PDF)){
            fileMap.put(ConstantUtils.FS_FILE_PDF, pdfData);
        }
        if (Arrays.asList(selectTypes).contains(ConstantUtils.FS_FILE_TXT)){
            fileMap.put(ConstantUtils.FS_FILE_TXT, txtData);
        }

        return fileMap;
    }
}
