package com.sendtion.poteviodemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sendtion.poteviodemo.entry.TestDataEntry;
import com.sendtion.poteviodemo.http.HttpService;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends BaseActivity {
    @BindView(R.id.list_home)
    RecyclerView mListHome;

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

    }

    @Override
    protected void initData() {
        sendRequest();
    }

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
            }

            @Override
            public void onFailure(Call<TestDataEntry> call, Throwable t) {
                dismissProgressDialog();
                Log.e("---", "onFailure: " + t.getMessage());
            }
        });
    }
}
