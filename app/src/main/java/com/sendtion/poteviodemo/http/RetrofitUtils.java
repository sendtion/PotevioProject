package com.sendtion.poteviodemo.http;

import android.webkit.WebSettings;

import androidx.annotation.NonNull;

import com.sendtion.poteviodemo.util.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit封装
 */
public class RetrofitUtils {
    private static RetrofitUtils retrofitUtils;

    private RetrofitUtils(){
    }

    public static RetrofitUtils getInstance(){
        if (retrofitUtils == null) {
            synchronized (RetrofitUtils.class){
                if (retrofitUtils == null){
                    retrofitUtils = new RetrofitUtils();
                }
            }
        }
        return retrofitUtils;
    }

    public HttpService getService(){
        return initRetrofit(initOkHttp()).create(HttpService.class);
    }

    /**
     * 初始化Retrofit
     */
    @NonNull
    private Retrofit initRetrofit(OkHttpClient httpClient){
        return new Retrofit.Builder()
                .client(httpClient)//设置自定义okHttp
                .baseUrl(HttpConfig.BASE_URL)//基地址
                .addConverterFactory(GsonConverterFactory.create())//添加GSON解析
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//添加Rxjava支持
                .build();
    }

    /**
     * 初始化OkHttp
     */
    @NonNull
    private OkHttpClient initOkHttp(){
        return new OkHttpClient().newBuilder()
                .readTimeout(HttpConfig.TIME_OUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(HttpConfig.TIME_OUT, TimeUnit.SECONDS)//设置写入超时时间
                .connectTimeout(HttpConfig.TIME_OUT, TimeUnit.SECONDS)//设置请求超时时间
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
    }

    /**
     * 获取UserAgent
     */
    private static String getUserAgent() {
        StringBuilder sb = new StringBuilder();
        try {
            String userAgent;
            try {
                userAgent = WebSettings.getDefaultUserAgent(Utils.getApp());
            } catch (Exception e) {
                userAgent = System.getProperty("http.agent");
            }
            for (int i = 0, length = userAgent.length(); i < length; i++) {
                char c = userAgent.charAt(i);
                if (c <= '\u001f' || c >= '\u007f') {
                    sb.append(String.format("\\u%04x", (int) c));
                } else {
                    sb.append(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
