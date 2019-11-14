package com.sendtion.poteviodemo.mvp.presenter;

import androidx.lifecycle.LifecycleOwner;

import com.sendtion.poteviodemo.entry.ArticleDataEntry;
import com.sendtion.poteviodemo.mvp.contract.ArticleContract;
import com.sendtion.poteviodemo.mvp.model.ArticleModel;

public class ArticlePresenter implements ArticleContract.Presenter {
    private ArticleContract.View mView;
    private ArticleModel mModel;

    public ArticlePresenter(ArticleContract.View view){
        this.mView = view;
        mView.setPresenter(this);
        mModel = new ArticleModel(this, (LifecycleOwner) view);
    }

    @Override
    public void start() {

    }

    @Override
    public void onError(Throwable e) {
        if (mView != null) {
            mView.dismissLoading();
        }
    }

    @Override
    public void getArticleList(int page) {
        if (mView != null) {
            mView.showLoading();
        }
        if (mModel != null) {
            mModel.getArticleList(page);
        }
    }

    @Override
    public void getArticleListSuccess(ArticleDataEntry data) {
        if (mView != null) {
            mView.getArticleListSuccess(data);
            mView.dismissLoading();
        }
    }

}
