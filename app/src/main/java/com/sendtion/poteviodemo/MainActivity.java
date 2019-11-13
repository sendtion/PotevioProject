package com.sendtion.poteviodemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sendtion.poteviodemo.adapter.BaseRecyclerAdapter;
import com.sendtion.poteviodemo.adapter.RecyclerViewHolder;
import com.sendtion.poteviodemo.entry.ArticleDataEntry;
import com.sendtion.poteviodemo.entry.TestDataEntry;
import com.sendtion.poteviodemo.http.BaseObserver;
import com.sendtion.poteviodemo.http.BaseResponse;
import com.sendtion.poteviodemo.http.HttpService;
import com.sendtion.poteviodemo.http.LogInterceptor;
import com.sendtion.poteviodemo.http.RetrofitUtils;
import com.sendtion.poteviodemo.http.RxHelper;
import com.sendtion.poteviodemo.util.Utils;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.observers.BlockingBaseObserver;
import io.reactivex.schedulers.Schedulers;
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

    private BaseRecyclerAdapter<ArticleDataEntry.DatasBean> mAdapter;
    private List<ArticleDataEntry.DatasBean> mItems;

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
        mAdapter = new BaseRecyclerAdapter<ArticleDataEntry.DatasBean>(this, mItems) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.list_item_home;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, ArticleDataEntry.DatasBean item) {
                if (item != null) {
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
        HttpService service = RetrofitUtils.getInstance().getService();
//        sendRequest(service);
//        sendRequest1(service);
//        sendRequest2(service);
//        sendRequest3(service);
        sendRequest4(service);
    }

    //发送请求
    private void sendRequest(HttpService service) {
        showProgressDialog();

        Call<TestDataEntry> repos = service.listRepos(1);

        repos.enqueue(new Callback<TestDataEntry>() {
            @Override
            public void onResponse(Call<TestDataEntry> call, Response<TestDataEntry> response) {
                dismissProgressDialog();
                Log.e("---", "onResponse: " + response.body());
                TestDataEntry data = response.body();
                if (data != null) {
                    ArticleDataEntry dataBean = data.getData();
                    if (dataBean != null) {
                        List<ArticleDataEntry.DatasBean> datasBeans = dataBean.getDatas();
                        if (datasBeans != null) {
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

    private void sendRequest1(HttpService service) {
        showProgressDialog();

        service.listRepos1(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))//绑定生命周期
                .subscribe(new Observer<TestDataEntry>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("---", "onSubscribe: " );
                    }

                    @Override
                    public void onNext(TestDataEntry testDataEntry) {
                        Log.e("---", "onNext: " );
                        if (testDataEntry != null) {
                            ArticleDataEntry dataBean = testDataEntry.getData();
                            if (dataBean != null) {
                                List<ArticleDataEntry.DatasBean> datasBeans = dataBean.getDatas();
                                if (datasBeans != null) {
                                    Log.e("---", "datasBeans: " + datasBeans.size());
                                    mAdapter.removeAll(mItems);
                                    mAdapter.addAll(datasBeans);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("---", "onError: " );
                    }

                    @Override
                    public void onComplete() {
                        dismissProgressDialog();
                        Log.e("---", "onComplete: " );
                    }
                });
    }

    private void sendRequest2(HttpService service) {
        showProgressDialog();

        service.listRepos2(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))//绑定生命周期
                .subscribe(new Observer<BaseResponse<ArticleDataEntry>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("---", "onSubscribe: " );
                    }

                    @Override
                    public void onNext(BaseResponse<ArticleDataEntry> data) {
                        Log.e("---", "onNext: " );
                        if (data != null) {
                            ArticleDataEntry dataBean = data.getData();
                            if (dataBean != null) {
                                List<ArticleDataEntry.DatasBean> datasBeans = dataBean.getDatas();
                                if (datasBeans != null) {
                                    Log.e("---", "datasBeans: " + datasBeans.size());
                                    mAdapter.removeAll(mItems);
                                    mAdapter.addAll(datasBeans);
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("---", "onError: " );
                    }

                    @Override
                    public void onComplete() {
                        dismissProgressDialog();
                        Log.e("---", "onComplete: " );
                    }
                });
    }

    private void sendRequest3(HttpService service) {
        showProgressDialog();

        service.listRepos2(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))//绑定生命周期
                .subscribe(new BaseObserver<ArticleDataEntry>(){

                    @Override
                    public void onSuccess(ArticleDataEntry data) {
                        dismissProgressDialog();
                        Log.e("---", "onSuccess: " );
                        if (data != null) {
                            List<ArticleDataEntry.DatasBean> datasBeans = data.getDatas();
                            if (datasBeans != null) {
                                Log.e("---", "datasBeans: " + datasBeans.size());
                                mAdapter.removeAll(mItems);
                                mAdapter.addAll(datasBeans);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        dismissProgressDialog();
                        Log.e("---", "onFailure: " );
                    }
                });
    }

    private void sendRequest4(HttpService service) {
        showProgressDialog();

        service.listRepos2(1)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver<ArticleDataEntry>(){

                    @Override
                    public void onSuccess(ArticleDataEntry data) {
                        dismissProgressDialog();
                        Log.e("---", "onSuccess: " );
                        if (data != null) {
                            List<ArticleDataEntry.DatasBean> datasBeans = data.getDatas();
                            if (datasBeans != null) {
                                Log.e("---", "datasBeans: " + datasBeans.size());
                                mAdapter.removeAll(mItems);
                                mAdapter.addAll(datasBeans);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        dismissProgressDialog();
                        Log.e("---", "onFailure: " );
                    }
                });
    }

    @Override
    public void onItemClick(View itemView, int pos) {

    }
}
