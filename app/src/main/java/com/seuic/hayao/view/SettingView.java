package com.seuic.hayao.view;

import com.seuic.hayao.view.base.IBaseView;

public interface SettingView extends IBaseView {

    void showProgressDialog();

    void hideProgressDialog();

    void showError(String msg);

    void onLoginSucess();
}
