package com.sendtion.poteviodemo.ui.comm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.FileUtils;
import com.sendtion.poteviodemo.R;
import com.sendtion.poteviodemo.base.BaseActivity;
import com.sendtion.poteviodemo.widget.TitleBarView;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

import butterknife.BindView;

/**
 * 预览文件
 */
public class PreviewFileActivity extends BaseActivity implements TbsReaderView.ReaderCallback {

    @BindView(R.id.title_bar_web_view)
    TitleBarView mTitleBar;
    @BindView(R.id.rl_tbsView)
    RelativeLayout rl_tbsView;

    private TbsReaderView mTbsReaderView;
    private String mFileUrl = "";
    private String mFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_file);
    }

    @Override
    protected void initView() {
        mTitleBar.setTitleContent("文件预览");
        //mTitleBar.setIconRight(R.mipmap.icon_title_right_more);

        mTbsReaderView = new TbsReaderView(this, this);
        rl_tbsView.addView(mTbsReaderView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mFileUrl = bundle.getString("file_url");
            String fileName = FileUtils.getFileNameNoExtension(mFileUrl);
            //Log.e(TAG, "fileUrl: " + mFileUrl );
            mTitleBar.setTitleContent(fileName);
            //mWebView.loadUrl("https://view.officeapps.live.com/op/view.aspx?src=" + fileUrl);
            //mWebView.loadUrl("file:///android_asset/pdfjs/index.html?" + fileUrl);//pdfUrl可以是本地文件，也可以是网页链接
        }
    }

    @Override
    protected void loadData() {
        if ((mFileUrl == null) || (mFileUrl.length() <= 0)) {
            showToast("获取文件url出错了");
            return;
        }
        mFileName = parseName(mFileUrl);
        if (new File(mFileUrl).exists()) {
            displayFile();
        } else {
            showToastLong("文件的url地址不合法，无法进行下载");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    /**
     * 利用文件url转换出文件名
     */
    private String parseName(String url) {
        String fileName = null;
        try {
            fileName = url.substring(url.lastIndexOf("/") + 1);
        } finally {
            if (TextUtils.isEmpty(fileName)) {
                fileName = String.valueOf(System.currentTimeMillis());
            }
        }
        return fileName;
    }

    /**
     * 加载显示文件内容
     */
    private void displayFile() {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("filePath", mFileUrl);
            bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
            String format = parseFormat(mFileName);
            boolean result = mTbsReaderView.preOpen(format, false);
            if (result) {
                mTbsReaderView.openFile(bundle);
            } else {
                File file = new File(mFileUrl);
                if (file.exists()) {
                    Intent openIntent = new Intent();
                    openIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    openIntent.setAction(Intent.ACTION_VIEW);
                    // 设置intent的data和Type属性。
                    openIntent.setDataAndType(Uri.fromFile(file), getMIMEType(file));
                    startActivity(openIntent);
                    finish();
                }
            }
        } catch (Exception e) {
            showToast("无法打开该格式文件");
            e.printStackTrace();
        }
    }

    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private String getMIMEType(File file) {

        String type = "*/*";
        try {
            String fName = file.getName();
            // 获取后缀名前的分隔符"."在fName中的位置。
            int dotIndex = fName.lastIndexOf(".");
            if (dotIndex < 0) {
                return type;
            }
            /* 获取文件的后缀名 */
            String end = fName.substring(dotIndex).toLowerCase();
            if (TextUtils.isEmpty(end))
                return type;
            // 在MIME和文件类型的匹配表中找到对应的MIME类型。
            for (String[] aMIME_MapTable : MIME_MapTable) {
                if (end.equals(aMIME_MapTable[0]))
                    type = aMIME_MapTable[1];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return type;
    }

    private final String[][] MIME_MapTable = {
            // {后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"}, {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"}, {".rtf", "application/rtf"},
            {".sh", "text/plain"}, {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"}, {".txt", "text/plain"},
            {".wav", "audio/x-wav"}, {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"}, {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"}, {"", "*/*"}};

}
