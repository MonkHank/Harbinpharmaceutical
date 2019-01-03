package com.seuic.hayao.presenter.impl;

import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.presenter.SettingPresenter;
import com.seuic.hayao.presenter.base.BasePresenter;
import com.seuic.hayao.view.SettingView;

import rx.Subscription;

public class SettingPresenterPresenterImpl extends BasePresenter<SettingView> implements SettingPresenter {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    public SettingPresenterPresenterImpl(SettingView view) {
        mDataManager = DataManager.getInstance();
        this.attachView(view);
    }

    @Override
    public void attachView(SettingView view) {
        this.mMvpView = view;
    }

    @Override
    public void detachView() {
        this.mMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    @Override
    public void saveAddress(String address) {
        mDataManager.saveAddress(address);
    }

    @Override
    public String getAddress() {
        return mDataManager.getAddress();
    }

    @Override
    public void saveUpdateAddress(String address) {
        mDataManager.saveUpdateAddress(address);
    }

    @Override
    public String getUpdateAddress() {
        return mDataManager.getUpdateAddress();
    }

    @Override
    public void saveDeleteTime(int time) {
        mDataManager.saveDeleteTime(time);
    }

    @Override
    public int getDeleteTime() {
        return mDataManager.getDeleteTime();
    }
}
