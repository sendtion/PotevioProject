package com.sendtion.poteviodemo.http;

import com.sendtion.poteviodemo.entry.TestDataEntry;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HttpService {

    @GET("article/list/{page}/json")
    Call<TestDataEntry> listRepos(@Path("page") int page);
}
