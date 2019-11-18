package com.sendtion.poteviodemo.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sendtion.poteviodemo.R;
import com.sendtion.poteviodemo.entry.EventBusEntry;
import com.sendtion.poteviodemo.entry.FileInfo;
import com.sendtion.poteviodemo.util.ConstantUtils;
import com.sendtion.poteviodemo.util.EventBusConstants;
import com.sendtion.poteviodemo.util.FileSelectHelper;
import com.sendtion.poteviodemo.util.FileUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 使用遍历文件夹的方式
 * Created by sendtion on 2019/02/12.
 */

public class FileInfoListAdapter extends RecyclerView.Adapter<FileInfoListAdapter.ViewHolder>
        implements View.OnClickListener {

    private Context mContext;
    private List<FileInfo> fileList;
    private String fileType;

    public FileInfoListAdapter(Context mContext, List<FileInfo> data, String fileType) {
        this.mContext = mContext;
        this.fileList = data;
        this.fileType = fileType;
    }

    public void setFileList(List<FileInfo> fileList) {
        this.fileList = fileList;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_file_info, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FileInfo fileInfo = fileList.get(position);
        holder.itemView.setTag(position);

        try {
            holder.tv_file_name.setText(fileInfo.getFileName());
            holder.tv_file_size.setText(FileUtils.formatFileSize(fileInfo.getFileSize()));
            holder.tv_file_time.setText(fileInfo.getTime());

            List<FileInfo> checkFiles = ConstantUtils.checkFiles.get(fileType);
            if (checkFiles == null) {
                checkFiles = new ArrayList<>();
            }
            if (checkFiles.contains(fileInfo)) {
                holder.cb_file_select.setChecked(true);
            } else {
                holder.cb_file_select.setChecked(false);
            }

            if (ConstantUtils.FS_FILE_IMAGE.equals(fileType)) {
                Glide.with(mContext).load(fileInfo.getFilePath()).into(holder.iv_type_icon);
            } else {
                int typeIcon = FileUtils.getFileTypeImageId(mContext, fileInfo.getFilePath());
                Glide.with(mContext).load(typeIcon).into(holder.iv_type_icon);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return fileList == null ? 0 : fileList.size();
    }

    @Override
    public void onClick(View v) {
        try {
            int count = 0;
            Map<String, List<FileInfo>> checkFilesMap = ConstantUtils.checkFiles;
            for (Map.Entry<String, List<FileInfo>> mapEntry : checkFilesMap.entrySet()) {
                count += mapEntry.getValue().size();
            }

            Integer position = (Integer) v.getTag();
            List<Integer> checkItems = ConstantUtils.checkItems.get(fileType);
            if (checkItems == null) {
                checkItems = new ArrayList<>();
            }
            List<FileInfo> checkFiles = ConstantUtils.checkFiles.get(fileType);
            if (checkFiles == null) {
                checkFiles = new ArrayList<>();
            }
            CheckBox cb_file_select = (CheckBox) v.findViewById(R.id.cb_file_select);
            if (cb_file_select.isChecked()) {
                cb_file_select.setChecked(false);
                checkItems.remove(position);
                checkFiles.remove(fileList.get(position));
                count --;
            } else {
                //超出最大选择数量判断
                if (count < FileSelectHelper.getInstance().getMaxCount()) {
                    cb_file_select.setChecked(true);
                    checkItems.add(position);
                    checkFiles.add(fileList.get(position));
                    count ++;
                } else {
                    Toast.makeText(mContext, FileSelectHelper.getInstance().getMaxTip(), Toast.LENGTH_LONG).show();
                }
            }
            ConstantUtils.checkItems.put(fileType, checkItems);
            ConstantUtils.checkFiles.put(fileType, checkFiles);

            EventBusEntry eventBusEntry = new EventBusEntry(EventBusConstants.FILE_SELECT_UPDATE);
            eventBusEntry.setMsg(String.valueOf(count));
            EventBus.getDefault().post(eventBusEntry);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_file_name;
        TextView tv_file_size;
        TextView tv_file_time;
        ImageView iv_type_icon;
        CheckBox cb_file_select;

        ViewHolder(View itemView) {
            super(itemView);
            tv_file_name = (TextView) itemView.findViewById(R.id.tv_file_name);
            tv_file_size = (TextView) itemView.findViewById(R.id.tv_file_size);
            tv_file_time = (TextView) itemView.findViewById(R.id.tv_file_time);
            iv_type_icon = (ImageView) itemView.findViewById(R.id.iv_type_icon);
            cb_file_select = (CheckBox) itemView.findViewById(R.id.cb_file_select);
        }
    }

}