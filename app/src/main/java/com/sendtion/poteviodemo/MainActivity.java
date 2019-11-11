package com.sendtion.poteviodemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sendtion.poteviodemo.adapter.BaseRecyclerAdapter;
import com.sendtion.poteviodemo.adapter.RecyclerViewHolder;
import com.sendtion.poteviodemo.entry.TestDataEntry;
import com.sendtion.poteviodemo.http.HttpService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity implements BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.list_home)
    RecyclerView mListHome;

    private BaseRecyclerAdapter<TestDataEntry.DataBean.DatasBean> mAdapter;
    private List<TestDataEntry.DataBean.DatasBean> mItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initStatusBar() {

    }

    @Override
    protected void initView() {
        mItems = new ArrayList<>();
        mAdapter = new BaseRecyclerAdapter<TestDataEntry.DataBean.DatasBean>(this, mItems) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.list_item_home;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, TestDataEntry.DataBean.DatasBean item) {
                if (item !=null){
                    String title = item.getTitle();
                    if (title != null) {
                        holder.setText(R.id.tv_article_title, title);
                    }
                    String author = item.getAuthor();
                    if (author == null) {
                        author = item.getShareUser();
                    }
                    holder.setText(R.id.tv_article_content, author);
                    String link = item.getLink();
                    if (link != null) {
                        holder.setText(R.id.tv_article_url, link);
                    }
                }
            }
        };
        mListHome.setLayoutManager(new LinearLayoutManager(this));
        mListHome.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        sendRequest();
    }

    //https://www.wanandroid.com/article/list/1/json
    private void sendRequest(){
        showProgressDialog();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.wanandroid.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HttpService service = retrofit.create(HttpService.class);

        Call<TestDataEntry> repos = service.listRepos(1);

        repos.enqueue(new Callback<TestDataEntry>() {
            @Override
            public void onResponse(Call<TestDataEntry> call, Response<TestDataEntry> response) {
                dismissProgressDialog();
                Log.e("---", "onResponse: " + response.body());
                TestDataEntry data = response.body();
                if (data != null){
                    TestDataEntry.DataBean dataBean = data.getData();
                    if (dataBean != null) {
                        List<TestDataEntry.DataBean.DatasBean> datasBeans = dataBean.getDatas();
                        if (datasBeans != null){
                            Log.e("---", "datasBeans: " + datasBeans.size());
                            mAdapter.removeAll(mItems);
                            mAdapter.addAll(datasBeans);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TestDataEntry> call, Throwable t) {
                dismissProgressDialog();
                Log.e("---", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onItemClick(View itemView, int pos) {

    }
}
