package com.seuic.hayao.presenter;

import com.seuic.hayao.modelbean.LoginInfo;
import com.seuic.hayao.presenter.base.IPresenter;
import com.seuic.hayao.view.LoginView;

public interface LoginPresenter  {
    void login(LoginInfo info);

    String getLastAccount();

    String getLastPassword();

    void checkUpdate();

    int getCheckVersion();

    void setCheckVesion(int version);

    String getUpdateAddress();
}
