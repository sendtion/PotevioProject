package com.sendtion.poteviodemo.mvp.view;

/**
 * View基类
 */
public interface BaseView<T> {

    void setPresenter(T presenter);

    /**
     * 显示加载对话框
     */
    void showLoading();

    /**
     * 隐藏加载对话框
     */
    void dismissLoading();
}
