package com.seuic.hayao.presenter.impl;

import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.modelbean.LoginInfo;
import com.seuic.hayao.presenter.LoginPresenter;
import com.seuic.hayao.presenter.base.BasePresenter;
import com.seuic.hayao.view.LoginView;
import com.seuic.update.UpdateInfo;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class LoginPresenterImpl extends BasePresenter<LoginView> implements LoginPresenter {

    private DataManager mDataManager;
    private Subscription mSubscription;
    private Subscription mSubscription1;

    public LoginPresenterImpl(LoginView view) {
        super(view);
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void login(LoginInfo info) {
        getMvpView().showProgressDialog();
        mSubscription = mDataManager.login(info)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().hideProgressDialog();
                        if (e.getMessage() != null && e.getMessage().contains("用户名或密码")) {
                            getMvpView().showError("用户名密码错误");
                        } else {
                            getMvpView().showError("登陆失败：" + e.getMessage());
                        }
                    }

                    @Override
                    public void onNext(Boolean isSuccess) {
                        getMvpView().hideProgressDialog();
                        if (isSuccess) {
                            getMvpView().onLoginSucess();
                        } else {
                            getMvpView().showError("用户名密码错误");
                        }
                    }
                });
    }

    @Override
    public String getLastAccount() {
        return mDataManager.getLatestAccount();
    }

    @Override
    public String getLastPassword() {
        return mDataManager.getLatestPassword();
    }

    @Override
    public void checkUpdate() {
        mSubscription1 = mDataManager.checkUpdate().observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<UpdateInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(UpdateInfo updateInfo) {
                if (updateInfo != null) getMvpView().showUpdate(updateInfo);
            }
        });
    }

    @Override
    public int getCheckVersion() {
        return mDataManager.getUpdateCheckVersion();
    }

    @Override
    public void setCheckVesion(int version) {
        mDataManager.setUpdateCheckVersion(version);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
        if (mSubscription1 != null) mSubscription1.unsubscribe();
    }

    @Override
    public String getUpdateAddress() {
        return mDataManager.getUpdateAddress();
    }
}
