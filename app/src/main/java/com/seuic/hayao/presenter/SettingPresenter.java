package com.seuic.hayao.presenter;

import com.seuic.hayao.modelbean.LoginInfo;
import com.seuic.hayao.presenter.base.IPresenter;
import com.seuic.hayao.view.SettingView;

public interface SettingPresenter extends IPresenter<SettingView> {
    void saveAddress(String address);

    String getAddress();

    void saveUpdateAddress(String address);

    String getUpdateAddress();

    void saveDeleteTime(int time);

    int getDeleteTime();

}
