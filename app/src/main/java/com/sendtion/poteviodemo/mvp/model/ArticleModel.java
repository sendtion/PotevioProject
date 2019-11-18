package com.sendtion.poteviodemo.mvp.model;

import androidx.lifecycle.LifecycleOwner;

import com.sendtion.poteviodemo.entry.ArticleDataEntry;
import com.sendtion.poteviodemo.http.BaseObserver;
import com.sendtion.poteviodemo.mvp.contract.ArticleContract;
import com.sendtion.poteviodemo.http.RequestUtils;

public class ArticleModel implements ArticleContract.Model {
    private ArticleContract.Presenter mPresenter;
    private LifecycleOwner mOwner;

    public ArticleModel(ArticleContract.Presenter presenter, LifecycleOwner owner){
        mPresenter = presenter;
        mOwner = owner;
    }

    @Override
    public void getArticleList(int page){
        RequestUtils.getInstance(mOwner).listRepos(page, new BaseObserver<ArticleDataEntry>() {
            @Override
            public void onSuccess(ArticleDataEntry data) {
                if (mPresenter != null) {
                    mPresenter.getArticleListSuccess(data);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                if (mPresenter != null) {
                    mPresenter.onError(e);
                }
            }
        });
    }
}
