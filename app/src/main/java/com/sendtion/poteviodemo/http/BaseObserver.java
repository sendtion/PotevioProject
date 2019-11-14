package com.sendtion.poteviodemo.http;

import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.sendtion.poteviodemo.util.NetworkUtils;
import com.sendtion.poteviodemo.util.Utils;

import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 统一处理回调，只返回成功和失败回调
 */
public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {
    private static final String TAG = "BaseObserver";

    @Override
    public void onSubscribe(Disposable d) {
        Log.e(TAG, "onSubscribe: ");
    }

    @Override
    public void onNext(BaseResponse<T> response) {
        Log.e(TAG, "onNext: ");
        if (response.getErrorCode() == 0){
            onSuccess(response.getData());
        } else {
            onFailure(new Throwable(response.getErrorMsg()));
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError: ");
        String errorMsg;
        //如果网络连接正常，判断相对应的错误信息
        if (!NetworkUtils.isNetworkConnected(Utils.getApp())) {
            errorMsg = "无可用网络，请检查您的网络状态";
        } else if (e != null) {
            if (e instanceof SocketTimeoutException) {
                errorMsg = "连接超时,请重新连接";
            } else if (e instanceof ConnectException) {
                errorMsg = "连接异常,请检查网络后重新连接";
            } else if (e instanceof UnknownHostException) {
                errorMsg = "未知主机:检查网络环境后重试";
            } else if (e instanceof JsonSyntaxException) {
                errorMsg = "解析异常";
            } else if (e instanceof HttpException) {
                int httpErrorCode = ((HttpException) e).response().code();
                if (httpErrorCode == 401) {
                    errorMsg = "登录已过期";
                    reLogin();
                } else if (httpErrorCode == 404) {
                    errorMsg = "无效的请求路径404";
                } else if (httpErrorCode == 500) {
                    errorMsg = "服务器异常500";
                } else if (httpErrorCode > 500) {
                    errorMsg = "服务器繁忙，请稍后重试";
                } else {
                    errorMsg = e.getMessage();
                }
            } else if (e instanceof NoRouteToHostException) {
                errorMsg = "检查网络环境后重试";
            } else {//e.getMessage();
                errorMsg = "网络出现错误，请稍后重试";
                Log.e(TAG, "onError: " + e.getMessage());
            }
        } else {
            errorMsg = "e is null";
        }
        Log.e(TAG, "onError: " + errorMsg);
        Toasty.normal(Utils.getApp(), errorMsg).show();
        onFailure(new Throwable(errorMsg));
    }

    @Override
    public void onComplete() {
        Log.e(TAG, "onComplete: ");
    }

    public abstract void onSuccess(T data);

    public abstract void onFailure(Throwable e);

    private void reLogin() {
        //ActivityUtils.getInstance().finishAllActivity();
        //ActivityUtils.startActivity(LoginActivity.class);
    }
}
