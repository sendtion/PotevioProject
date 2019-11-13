package com.sendtion.poteviodemo.http;

import com.sendtion.poteviodemo.entry.ArticleDataEntry;
import com.sendtion.poteviodemo.entry.TestDataEntry;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HttpService {

    @GET("article/list/{page}/json")
    Call<TestDataEntry> listRepos(@Path("page") int page);

    //使用RxJava2
    @GET("article/list/{page}/json")
    Observable<TestDataEntry> listRepos1(@Path("page") int page);

    //通过BaseResponse把基础数据和需求数据分开
    @GET("article/list/{page}/json")
    Observable<BaseResponse<ArticleDataEntry>> listRepos2(@Path("page") int page);
}
