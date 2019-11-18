package com.sendtion.poteviodemo.ui.comm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.ThemedSpinnerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sendtion.poteviodemo.R;
import com.sendtion.poteviodemo.base.BaseActivity;
import com.sendtion.poteviodemo.entry.EventBusEntry;
import com.sendtion.poteviodemo.entry.FileInfo;
import com.sendtion.poteviodemo.ui.adapter.FileInfoListAdapter;
import com.sendtion.poteviodemo.util.ConstantUtils;
import com.sendtion.poteviodemo.util.DocumentUtils;
import com.sendtion.poteviodemo.util.EventBusConstants;
import com.sendtion.poteviodemo.util.FileSelectHelper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


/**
 * 文件选择
 */
public class FileSelectActivity extends BaseActivity {

    @BindView(R.id.rv_list_file_select)
    RecyclerView mListOneSelect;
    @BindView(R.id.tv_file_select_confirm)
    TextView mSelectConfirm;

    private ProgressDialog loadingDialog;
    private FileInfoListAdapter mAdapter;
    private ArrayList<FileInfo> data;
    private String fileType = ConstantUtils.FS_FILE_IMAGE;
    private static String[] fileTypes;

    private MyHandler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<FileSelectActivity> weakReference;

        MyHandler(FileSelectActivity activity){
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference != null){
                FileSelectActivity activity = weakReference.get();
                if (activity != null) {
                    if (msg.what == 12) {
                        if (activity.loadingDialog != null && activity.loadingDialog.isShowing()){
                            activity.loadingDialog.dismiss();
                        }
                        activity.mAdapter.setFileList(activity.data);
                        activity.mAdapter.setFileType(activity.fileType);
                        activity.mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);
    }

    @Override
    protected void initView() {
        mSelectConfirm.setOnClickListener(v -> {
            ArrayList<String> fileList = new ArrayList<>();
            Map<String, List<FileInfo>> checkFiles = ConstantUtils.checkFiles;
            for (Map.Entry<String, List<FileInfo>> mapEntry : checkFiles.entrySet()){
                for (FileInfo fileInfo : mapEntry.getValue()){
                    fileList.add(fileInfo.getFilePath());
                }
            }
            Intent intent = new Intent();
            intent.putStringArrayListExtra(ConstantUtils.FS_SELECTED_FILE, fileList);
            setResult(RESULT_OK, intent);
            finish();
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mListOneSelect.setLayoutManager(linearLayoutManager);
        mAdapter = new FileInfoListAdapter(this, data, fileType);
        mListOneSelect.setAdapter(mAdapter);

        loadingDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
        loadingDialog.setMessage("加载中...");
        loadingDialog.setCanceledOnTouchOutside(false);

        fileTypes = FileSelectHelper.getInstance().getFileTypes();

        AppCompatSpinner spinner = findViewById(R.id.spinner_file_type);
        spinner.setAdapter(new MyAdapter(this, fileTypes));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fileType = fileTypes[position];

                loadingDialog.show();
                new Thread() {
                    @Override
                    public void run() {
                        getFolderData(fileType);
                    }
                }.start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void loadData() {

    }

    public void getFolderData(String fileType) {
        if (fileType.equals(ConstantUtils.FS_FILE_IMAGE)){
            data = DocumentUtils.getImages(this);
        } else {
            data = DocumentUtils.getDocumentData(this, fileType);
        }
        handler.sendEmptyMessage(12);
    }

    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Resources.Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Resources.Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    @Override
    protected boolean isBindEventBus() {
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void threadMode(EventBusEntry eventBusEntry) {
        try {
            if (eventBusEntry != null) {
                String status = eventBusEntry.getStatus();
                if (EventBusConstants.FILE_SELECT_UPDATE.equals(status)) { //更新文件选择数量
                    mSelectConfirm.setText("确认 (" + eventBusEntry.getMsg() + ")");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(12);

        ConstantUtils.checkItems.clear();
        ConstantUtils.checkFiles.clear();

        //恢复默认参数
        FileSelectHelper.getInstance()
                .setMaxCount(ConstantUtils.MAX_COUNT)
                .setMaxTip(ConstantUtils.MAX_TIP)
                .setFileTypes(new String[]{ConstantUtils.FS_FILE_WORD, ConstantUtils.FS_FILE_XLS,
                        ConstantUtils.FS_FILE_PPT, ConstantUtils.FS_FILE_PDF, ConstantUtils.FS_FILE_TXT,
                        ConstantUtils.FS_FILE_IMAGE, ConstantUtils.FS_FILE_ZIP});
    }
}
