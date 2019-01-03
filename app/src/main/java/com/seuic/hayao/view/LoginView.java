package com.seuic.hayao.view;

import com.seuic.hayao.view.base.IBaseView;
import com.seuic.update.UpdateInfo;

public interface LoginView extends IBaseView {

    void showProgressDialog();

    void hideProgressDialog();

    void showError(String msg);

    void onLoginSucess();

    void showUpdate(UpdateInfo info);
}
