package com.seuic.hayao.view;

import com.seuic.hayao.view.base.IBaseView;

public interface MainView extends IBaseView {

    void updataNumber(int number);

    void showErrorMsg(String msg);

    void showCorpNumberUpdate();
}
