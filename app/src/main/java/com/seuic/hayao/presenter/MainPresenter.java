package com.seuic.hayao.presenter;

import com.seuic.hayao.presenter.base.IPresenter;
import com.seuic.hayao.view.MainView;

public interface MainPresenter extends IPresenter<MainView> {

    void updataNumber();

    void checkCorpNumber();
}
