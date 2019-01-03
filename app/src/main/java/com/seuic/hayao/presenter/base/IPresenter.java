package com.seuic.hayao.presenter.base;

import com.seuic.hayao.view.base.IBaseView;

public interface IPresenter<V extends IBaseView> {

    void attachView(V view);

    void detachView();
}