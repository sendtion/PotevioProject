package com.sendtion.poteviodemo.base;

/**
 * View基类
 */
public interface BaseView {

    /**
     * 显示加载对话框
     */
    void showLoading();

    /**
     * 隐藏加载对话框
     */
    void dismissLoading();

    /**
     * 发生错误
     */
    void onError(Throwable e);
}
