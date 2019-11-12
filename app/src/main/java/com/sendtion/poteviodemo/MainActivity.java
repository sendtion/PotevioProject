package com.sendtion.poteviodemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sendtion.poteviodemo.adapter.BaseRecyclerAdapter;
import com.sendtion.poteviodemo.adapter.RecyclerViewHolder;
import com.sendtion.poteviodemo.entry.TestDataEntry;
import com.sendtion.poteviodemo.http.HttpService;
import com.sendtion.poteviodemo.http.LogInterceptor;
import com.sendtion.poteviodemo.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity implements BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.list_home)
    RecyclerView mListHome;

    private static final int TIME_OUT = 30; //超时时间

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

        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)//设置写入超时时间
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)//设置请求超时时间
                //.addInterceptor(new HttpLoggingInterceptor())//添加打印拦截器
                .addInterceptor(new LogInterceptor())
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    request = request.newBuilder()
                            .addHeader("User-Agent", getUserAgent())
                            .build();
                    return chain.proceed(request);
                })
                .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClient)//设置自定义okHttp
                .baseUrl("https://www.wanandroid.com")//基地址
                .addConverterFactory(GsonConverterFactory.create())//添加GSON解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加Rxjava支持
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

    //获取UserAgent
    //Mozilla/5.0 (Linux; Android 9; STF-AL00 Build/HUAWEISTF-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/74.0.3729.136 Mobile Safari/537.36
    private static String getUserAgent() {
        String userAgent;
        try {
            userAgent = WebSettings.getDefaultUserAgent(Utils.getApp());
        } catch (Exception e) {
            userAgent = System.getProperty("http.agent");
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    @Override
    public void onItemClick(View itemView, int pos) {

    }
}
