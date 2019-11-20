package com.sendtion.poteviodemo.ui.activity;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sendtion.poteviodemo.R;
import com.sendtion.poteviodemo.ui.activity.video.IJKPlayerActivity;
import com.sendtion.poteviodemo.ui.activity.video.VLCPlayerActivity;
import com.sendtion.poteviodemo.ui.activity.video.VideoViewActivity;
import com.sendtion.poteviodemo.ui.adapter.BaseRecyclerAdapter;
import com.sendtion.poteviodemo.ui.adapter.RecyclerViewHolder;
import com.sendtion.poteviodemo.base.BaseActivity;
import com.sendtion.poteviodemo.util.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements BaseRecyclerAdapter.OnItemClickListener {

    @BindView(R.id.list_home)
    RecyclerView mListHome;

    private BaseRecyclerAdapter<String> mAdapter;
    private List<String> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        mItems = new ArrayList<>();
        mAdapter = new BaseRecyclerAdapter<String>(this, mItems) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.list_item_home;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, String item) {
                if (item != null) {
                    holder.setText(R.id.tv_article_title, item);
                    holder.getTextView(R.id.tv_article_content).setVisibility(View.GONE);
                    holder.getTextView(R.id.tv_article_url).setVisibility(View.GONE);
                }
            }
        };
        mListHome.setLayoutManager(new LinearLayoutManager(this));
        mListHome.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        List<String> dataList = new ArrayList<>();
        dataList.add("WanAndroid");
        dataList.add("IJKPlayer");
        dataList.add("VLCPlayer");
        dataList.add("VideoView");
        dataList.add("123");
        mAdapter.removeAll(mItems);
        mAdapter.addAll(dataList);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onItemClick(View itemView, int pos) {
        switch (pos) {
            case 0:
                ActivityUtils.startActivity(this, HomeActivity.class);
                break;
            case 1:
                ActivityUtils.startActivity(this, IJKPlayerActivity.class);
                break;
            case 2:
                ActivityUtils.startActivity(this, VLCPlayerActivity.class);
                break;
            case 3:
                ActivityUtils.startActivity(this, VideoViewActivity.class);
                break;
            default:
                break;
        }
    }

}
