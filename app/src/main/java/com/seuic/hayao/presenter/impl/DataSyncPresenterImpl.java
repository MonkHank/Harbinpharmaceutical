package com.seuic.hayao.presenter.impl;

import com.seuic.hayao.data.DataManager;
import com.seuic.hayao.modelbean.LoginInfo;
import com.seuic.hayao.presenter.DataSyncPresenter;
import com.seuic.hayao.presenter.LoginPresenter;
import com.seuic.hayao.presenter.base.BasePresenter;
import com.seuic.hayao.view.DataSyncView;
import com.seuic.hayao.view.LoginView;
import com.seuic.hayao.view.StockInPreSetView;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class DataSyncPresenterImpl extends BasePresenter<DataSyncView> implements DataSyncPresenter {

    private DataManager mDataManager;
    private Subscription mSubscription;

    public DataSyncPresenterImpl(DataSyncView view) {
        mDataManager = DataManager.getInstance();
        this.attachView(view);
    }

    @Override
    public void attachView(DataSyncView view) {
        this.mMvpView = view;
    }

    @Override
    public void detachView() {
        this.mMvpView = null;
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    @Override
    public void startSyncContactCompany() {
        getMvpView().showProgress();
        mSubscription = mDataManager.syncContactCompany().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    int number = 0;

                    @Override
                    public void onCompleted() {
                        getMvpView().dissmissProgress();
                        getMvpView().showDownloadResult(number);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().showErrorInfo(e.getMessage());
                        getMvpView().dissmissProgress();
                    }

                    @Override
                    public void onNext(Integer integer) {
                        number = integer;
                        getMvpView().updateProgress(integer + "");
                    }
                });
    }
}
