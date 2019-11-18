package com.sendtion.poteviodemo.mvp.contract;

import com.sendtion.poteviodemo.base.BaseModel;
import com.sendtion.poteviodemo.base.BasePresenter;
import com.sendtion.poteviodemo.entry.ArticleDataEntry;
import com.sendtion.poteviodemo.base.BaseView;

public interface ArticleContract {

    interface Model extends BaseModel {
        void getArticleList(int page);
    }

    interface View extends BaseView {
        void getArticleListSuccess(ArticleDataEntry data);
    }

    interface Presenter extends BasePresenter {
        void getArticleList(int page);
        void getArticleListSuccess(ArticleDataEntry data);
    }
}
