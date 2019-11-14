package com.sendtion.poteviodemo.mvp.contract;

import com.sendtion.poteviodemo.entry.ArticleDataEntry;
import com.sendtion.poteviodemo.mvp.presenter.BasePresenter;
import com.sendtion.poteviodemo.mvp.view.BaseView;

public interface ArticleContract {

    interface View extends BaseView<Presenter>{
        void getArticleListSuccess(ArticleDataEntry data);
    }

    interface Presenter extends BasePresenter{
        void getArticleList(int page);
        void getArticleListSuccess(ArticleDataEntry data);
    }
}
